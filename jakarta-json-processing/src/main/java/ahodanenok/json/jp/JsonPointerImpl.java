package ahodanenok.json.jp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

final class JsonPointerImpl implements JsonPointer {

    private static final String REF_ARRAY_AFTER_LAST = "-";

    final List<String> tokens;

    JsonPointerImpl(String pointer) {
        char ch;
        int pos = 0;
        List<String> tokens = new ArrayList<>();
        while (pos < pointer.length()) {
            if (pointer.charAt(pos) != '/') {
                throw new JsonException("Pointer is not valid: reference token must start with a '/'");
            }

            pos++;
            StringBuilder sb = new StringBuilder();
            while (pos < pointer.length() && (ch = pointer.charAt(pos)) != '/') {
                if (ch == '~' && pos < pointer.length()) {
                    if (pointer.charAt(pos + 1) == '0') {
                        sb.append('~');
                        pos += 2;
                        continue;
                    } else if (pointer.charAt(pos + 1) == '1') {
                        sb.append('/');
                        pos += 2;
                        continue;
                    }
                }

                sb.append(ch);
                pos++;
            }

            tokens.add(sb.toString());
        }

        this.tokens = tokens;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonStructure> T add(T target, JsonValue value) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(value);

        if (tokens.isEmpty()) {
            if (target.getClass().isAssignableFrom(value.getClass())) {
                return (T) value;
            } else {
                throw new JsonException(String.format(
                    "Value of type '%s' is not assignable to the target of type '%s'",
                    value.getClass().getCanonicalName(),
                    target.getClass().getCanonicalName()));
            }
        }

        if (tokens.size() == 1) {
            return (T) add(target, tokens.get(0), value);
        }

        List<JsonStructure> path = resolveTarget(target);
        JsonStructure newTarget = add(path.get(path.size() - 1), tokens.get(tokens.size() - 1), value);
        for (int i = path.size() - 2; i >= 0; i--) {
            newTarget = replace(path.get(i), tokens.get(i), newTarget);
        }

        return (T) newTarget;
    }

    private JsonStructure add(JsonStructure target, String ref, JsonValue value) {
        if (target instanceof JsonObject) {
            Map<String, JsonValue> newValues = new LinkedHashMap<>(target.asJsonObject());
            newValues.put(ref, value);

            return new JsonObjectImpl(newValues);
        } else if (target instanceof JsonArray) {
            List<JsonValue> newValues = new ArrayList<>(target.asJsonArray());
            if (ref.equals(REF_ARRAY_AFTER_LAST)) {
                newValues.add(value);
            } else {
                int idx = getArrayIndex(ref);
                if (idx > newValues.size()) {
                    throw new JsonException(String.format("Array index is out of range: %s > %s", idx, newValues.size()));
                }

                newValues.add(idx, value);
            }

            return new JsonArrayImpl(newValues);
        } else {
           throw new IllegalStateException(
                "Unsupported json structure: " + target.getClass());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonStructure> T remove(T target) {
        Objects.requireNonNull(target);
        if (tokens.isEmpty()) {
            throw new JsonException("Referenced value is the target itself");
        }

        List<JsonStructure> path = resolveTarget(target);
        JsonStructure newTarget = remove(path.get(path.size() - 1), tokens.get(tokens.size() - 1));
        for (int i = path.size() - 2; i >= 0; i--) {
            newTarget = replace(path.get(i), tokens.get(i), newTarget);
        }

        return (T) newTarget;
    }

    private JsonStructure remove(JsonStructure target, String ref) {
        if (target instanceof JsonObject object) {
            Map<String, JsonValue> newValues = new LinkedHashMap<>(object);
            newValues.remove(ref);

            return new JsonObjectImpl(newValues);
        } else if (target instanceof JsonArray array) {
            List<JsonValue> newValues = new ArrayList<>(array);
            if (ref.equals(REF_ARRAY_AFTER_LAST)) {
                throw new JsonException(String.format("Value at index '%s' doesn't exist", REF_ARRAY_AFTER_LAST));
            } else {
                int idx = getArrayIndex(ref);
                if (idx >= newValues.size()) {
                    throw new JsonException(String.format("Array index is out of range: %s >= %s", idx, array.size()));
                }

                newValues.remove(idx);
            }

            return new JsonArrayImpl(newValues);
        } else {
           throw new IllegalStateException(
                "Unsupported json structure: " + target.getClass());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonStructure> T replace(T target, JsonValue value) {
        Objects.requireNonNull(target);
        if (tokens.isEmpty()) {
            throw new JsonException("Referenced value is the target itself");
        }

        List<JsonStructure> path = resolveTarget(target);
        JsonStructure newTarget = replace(path.get(path.size() - 1), tokens.get(tokens.size() - 1), value);
        for (int i = path.size() - 2; i >= 0; i--) {
            newTarget = replace(path.get(i), tokens.get(i), newTarget);
        }

        return (T) newTarget;
    }

    private JsonStructure replace(JsonStructure target, String ref, JsonValue value) {
        if (target instanceof JsonObject) {
            Map<String, JsonValue> newValues = new LinkedHashMap<>(target.asJsonObject());
            newValues.put(ref, value);

            return new JsonObjectImpl(newValues);
        } else if (target instanceof JsonArray) {
            List<JsonValue> newValues = new ArrayList<>(target.asJsonArray());
            if (ref.equals(REF_ARRAY_AFTER_LAST)) {
                throw new JsonException(String.format("Value at index '%s' doesn't exist", REF_ARRAY_AFTER_LAST));
            } else {
                int idx = getArrayIndex(ref);
                if (idx >= newValues.size()) {
                    throw new JsonException(String.format("Array index is out of range: %s >= %s", idx, newValues.size()));
                }

                newValues.set(idx, value);
            }

            return new JsonArrayImpl(newValues);
        } else {
            throw new IllegalStateException(
                "Unsupported json structure: " + target.getClass());
        }
    }

    @Override
    public boolean containsValue(JsonStructure target) {
        if (target == null) {
            return false;
        }

        if (tokens.isEmpty()) {
            return true;
        }

        List<JsonStructure> path = resolveTarget(target);
        String ref = tokens.get(tokens.size() - 1);
        JsonStructure currentTarget = path.get(path.size() - 1);
        if (currentTarget instanceof JsonObject object) {
            return object.containsKey(ref);
        } else if (currentTarget instanceof JsonArray array) {
            if (REF_ARRAY_AFTER_LAST.equals(ref)) {
                return false;
            }

            int idx;
            try {
                idx = Integer.parseInt(ref);
            } catch (NumberFormatException e) {
                return false;
            }

            if (idx < 0 || idx >= array.size()) {
                return false;
            }

            return true;
        } else {
            throw new IllegalStateException(
                "Unsupported json structure: " + currentTarget.getClass());
        }
    }

    @Override
    public JsonValue getValue(JsonStructure target) {
        Objects.requireNonNull(target);
        if (tokens.isEmpty()) {
            return target;
        }

        List<JsonStructure> path = resolveTarget(target);
        String ref = tokens.get(tokens.size() - 1);
        JsonStructure currentTarget = path.get(path.size() - 1);
        if (currentTarget instanceof JsonObject object) {
            JsonValue value = object.get(ref);
            if (value == null) {
                throw new JsonException("Referenced value doesn't exist");
            }

            return value;
        } else if (currentTarget instanceof JsonArray array) {
            if (REF_ARRAY_AFTER_LAST.equals(ref)) {
                throw new JsonException("Referenced value doesn't exist");
            }

            int idx = getArrayIndex(ref);
            if (idx >= array.size()) {
                throw new JsonException("Referenced value doesn't exist");
            }

            return array.get(idx);
        } else {
            throw new IllegalStateException(
                "Unsupported json structure: " + currentTarget.getClass());
        }
    }

    private List<JsonStructure> resolveTarget(JsonStructure target) {
        List<JsonStructure> path = new ArrayList<>(tokens.size());
        JsonValue currentTarget = target;
        Iterator<String> tokenIterator = tokens.iterator();
        while (tokenIterator.hasNext()) {
            String token = tokenIterator.next();
            if (currentTarget instanceof JsonObject object) {
                path.add(object);
                if (tokenIterator.hasNext()) {
                    currentTarget = object.get(token);
                }
            } else if (currentTarget instanceof JsonArray array) {
                path.add(array);
                if (tokenIterator.hasNext()) {
                    if (token.equals(REF_ARRAY_AFTER_LAST)) {
                        throw new JsonException(String.format("Value at index '%s' doesn't exist", REF_ARRAY_AFTER_LAST));
                    }

                    int idx = getArrayIndex(token);
                    if (idx >= array.size()) {
                        throw new JsonException(String.format("Array index is out of range: %s >= %s", idx, array.size()));
                    }

                    currentTarget = array.get(idx);
                }
            } else {
                throw new IllegalStateException(
                    "Unsupported json structure: " + currentTarget.getClass());
            }
        }

        return path;
    }

    private int getArrayIndex(String ref) {
        if (ref.length() > 1 && ref.charAt(0) == '0') {
            throw new JsonException("Array index can't start with 0");
        }

        try {
            int idx = Integer.parseInt(ref);
            if (idx < 0) {
                throw new JsonException(String.format("Array index is out of range: %s < 0", idx));
            }

            return idx;
        } catch (NumberFormatException e) {
            throw new JsonException(String.format("Invalid array index: '%s'", ref));
        }
    }
}
