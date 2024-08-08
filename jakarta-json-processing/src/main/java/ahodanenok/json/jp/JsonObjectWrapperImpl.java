package ahodanenok.json.jp;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

final class JsonObjectWrapperImpl extends AbstractMap<String, JsonValue> implements JsonObject {

    private final ahodanenok.json.value.JsonObject object;

    JsonObjectWrapperImpl(ahodanenok.json.value.JsonObject object) {
        this.object = Objects.requireNonNull(object);
    }

    @Override
    public JsonValue.ValueType getValueType() {
        return ValueType.OBJECT;
    }

    @Override
    public int size() {
        return object.size();
    }

    @Override
    public Set<Map.Entry<String, JsonValue>> entrySet() {
        return new AbstractSet<>() {

            @Override
            public Iterator<Map.Entry<String, JsonValue>> iterator() {
                return new Iterator<>() {

                    final Iterator<String> names = object.getNames().iterator();

                    @Override
                    public boolean hasNext() {
                        return names.hasNext();
                    }

                    @Override
                    public Map.Entry<String, JsonValue> next() {
                        String name = names.next();
                        return new SimpleEntry<>(name, get(name));
                    }
                };
            }

            @Override
            public int size() {
                return object.size();
            }
        };
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);
        return object.containsValue(key.toString());
    }

    @Override
    public JsonValue get(Object key) {
        Objects.requireNonNull(key);
        ahodanenok.json.value.JsonValue value = object.getValue(key.toString());
        ahodanenok.json.value.ValueType type = value.getType();
        if (type.equals(ahodanenok.json.value.ValueType.NULL)) {
            return JsonValue.NULL;
        } else if (type.equals(ahodanenok.json.value.ValueType.STRING)) {
            return new JsonStringImpl(value.asString().getValue());
        } else if (type.equals(ahodanenok.json.value.ValueType.NUMBER)) {
            return new JsonNumberDoubleImpl(value.asNumber().doubleValue());
        } else if (type.equals(ahodanenok.json.value.ValueType.BOOLEAN)) {
            return value.asBoolean().getValue() ? JsonValue.TRUE : JsonValue.FALSE;
        } else if (type.equals(ahodanenok.json.value.ValueType.ARRAY)) {
            return new JsonArrayWrapperImpl(value.asArray());
        } else if (type.equals(ahodanenok.json.value.ValueType.OBJECT)) {
            return new JsonObjectWrapperImpl(value.asObject());
        } else {
            throw new IllegalStateException(type.toString());
        }
    }

    @Override
    public JsonArray getJsonArray(String name) {
        if (!object.containsValue(name)) {
            return null;
        }

        return new JsonArrayWrapperImpl(object.getValue(name).asArray());
    }

    @Override
    public JsonObject getJsonObject(String name) {
        if (!object.containsValue(name)) {
            return null;
        }

        return new JsonObjectWrapperImpl(object.getValue(name).asObject());
    }

    @Override
    public JsonNumber getJsonNumber(String name) {
        if (!object.containsValue(name)) {
            return null;
        }

        return new JsonNumberWrapperImpl(object.getValue(name).asNumber());
    }

    @Override
    public JsonString getJsonString(String name) {
        if (!object.containsValue(name)) {
            return null;
        }

        return new JsonStringImpl(object.getValue(name).asString().getValue());
    }

    @Override
    public String getString(String name) {
        return object.getValue(name).asString().getValue();
    }

    @Override
    public String getString(String name, String defaultValue) {
        if (!object.containsValue(name)) {
            return defaultValue;
        }

        ahodanenok.json.value.JsonValue value = object.getValue(name);
        if(!value.getType().equals(ahodanenok.json.value.ValueType.STRING)) {
            return defaultValue;
        }

        return value.asString().getValue();
    }

    @Override
    public int getInt(String name) {
        return (int) object.getValue(name).asNumber().doubleValue();
    }

    @Override
    public int getInt(String name, int defaultValue) {
        if (!object.containsValue(name)) {
            return defaultValue;
        }

        ahodanenok.json.value.JsonValue value = object.getValue(name);
        if(!value.getType().equals(ahodanenok.json.value.ValueType.NUMBER)) {
            return defaultValue;
        }

        return (int) object.getValue(name).asNumber().doubleValue();
    }

    @Override
    public boolean getBoolean(String name) {
        return object.getValue(name).asBoolean().getValue();
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        if (!object.containsValue(name)) {
            return defaultValue;
        }

        ahodanenok.json.value.JsonValue value = object.getValue(name);
        if(!value.getType().equals(ahodanenok.json.value.ValueType.BOOLEAN)) {
            return defaultValue;
        }

        return object.getValue(name).asBoolean().getValue();
    }

    @Override
    public boolean isNull(String name) {
        return object.getValue(name).isNull();
    }

    @Override
    public String toString() {
        return Utils.writeValueToString(this);
    }
}
