package ahodanenok.json.jp;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPointer;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

final class JsonPatchImpl implements JsonPatch {

    static final String ATTR_OP = "op";
    static final String ATTR_PATH = "path";
    static final String ATTR_VALUE = "value";
    static final String ATTR_FROM = "from";

    static final String OP_ADD = "add";
    static final String OP_REMOVE = "remove";
    static final String OP_REPLACE = "replace";
    static final String OP_MOVE = "move";
    static final String OP_COPY = "copy";
    static final String OP_TEST = "test";

    private final JsonArray operations;

    JsonPatchImpl(JsonArray operations) {
        this.operations = operations;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonStructure> T apply(T target) {
        JsonStructure currentTarget = target;
        for (int i = 0; i < operations.size(); i++) {
            JsonValue value = operations.get(i);
            if (value.getValueType() != JsonValue.ValueType.OBJECT) {
                throw new JsonException(String.format(
                    "Json patch operation at index %d is not an '%s', but '%s'",
                    i, JsonValue.ValueType.OBJECT, value.getValueType()));
            }

            JsonObject op = value.asJsonObject();
            String opcode = getString(op, ATTR_OP, i);
            if (opcode.equals(OP_ADD)) {
                currentTarget = add(currentTarget, op, i);
            } else if (opcode.equals(OP_REMOVE)) {
                currentTarget = remove(currentTarget, op, i);
            } else if (opcode.equals(OP_REPLACE)) {
                currentTarget = replace(currentTarget, op, i);
            } else if (opcode.equals(OP_MOVE)) {
                currentTarget = move(currentTarget, op, i);
            } else if (opcode.equals(OP_COPY)) {
                currentTarget = copy(currentTarget, op, i);
            } else if (opcode.equals(OP_TEST)) {
                currentTarget = test(currentTarget, op, i);
            } else {
                throw new JsonException(String.format(
                    "Json patch operation at index %d has unknown operation '%s'", i, opcode));
            }
        }

        return (T) currentTarget;
    }

    private JsonStructure add(JsonStructure target, JsonObject op, int idx) {
        JsonPointer pointer = new JsonPointerImpl(getString(op, ATTR_PATH, idx));
        JsonValue value = getValue(op, ATTR_VALUE, idx);

        return pointer.add(target, value);
    }

    private JsonStructure remove(JsonStructure target, JsonObject op, int idx) {
        JsonPointer path = new JsonPointerImpl(getString(op, ATTR_PATH, idx));
        return path.remove(target);
    }

    private JsonStructure replace(JsonStructure target, JsonObject op, int idx) {
        JsonPointer path = new JsonPointerImpl(getString(op, ATTR_PATH, idx));
        JsonValue value = getValue(op, ATTR_VALUE, idx);

        return path.replace(target, value);
    }

    private JsonStructure move(JsonStructure target, JsonObject op, int idx) {
        JsonPointer to = new JsonPointerImpl(getString(op, ATTR_PATH, idx));
        JsonPointer from = new JsonPointerImpl(getString(op, ATTR_FROM, idx));

        return to.add(from.remove(target), from.getValue(target));
    }

    private JsonStructure copy(JsonStructure target, JsonObject op, int idx) {
        JsonPointer to = new JsonPointerImpl(getString(op, ATTR_PATH, idx));
        JsonPointer from = new JsonPointerImpl(getString(op, ATTR_FROM, idx));

        return to.add(target, from.getValue(target));
    }

    private JsonStructure test(JsonStructure target, JsonObject op, int idx) {
        JsonPointer path = new JsonPointerImpl(getString(op, ATTR_PATH, idx));
        JsonValue value = getValue(op, ATTR_VALUE, idx);
        if (!value.equals(path.getValue(target))) {
            throw new JsonException(String.format(
                "Value at the location '%s' is not equal to expected value", path));
        }

        return target;
    }

    private String getString(JsonObject op, String attr, int idx) {
        JsonValue value = getValue(op, attr, idx);
        checkValueType(value, JsonValue.ValueType.STRING, attr, idx);

        return ((JsonString) value).getString();
    }

    private JsonValue getValue(JsonObject op, String attr, int idx) {
        if (!op.containsKey(attr)) {
            throw new JsonException(String.format(
                "Json patch operation at index %d is missing the '%s' attribute", idx, attr));
        }

        return op.get(attr);
    }

    private void checkValueType(JsonValue value, JsonValue.ValueType expectedType, String attr, int idx) {
        if (value.getValueType() != expectedType) {
            throw new JsonException(String.format(
                "Json patch operation at index %d has invalid '%s' attribute: expected '%s', got '%s'",
                idx, attr, expectedType, value.getValueType()));
        }
    }

    @Override
    public JsonArray toJsonArray() {
        return operations;
    }
}
