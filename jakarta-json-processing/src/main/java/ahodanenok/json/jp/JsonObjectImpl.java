package ahodanenok.json.jp;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

final class JsonObjectImpl extends AbstractMap<String, JsonValue> implements JsonObject {

    private final Map<String, JsonValue> values;

    JsonObjectImpl(Map<String, JsonValue> values) {
        this.values = Objects.requireNonNull(values);
    }

    @Override
    public JsonValue.ValueType getValueType() {
        return ValueType.OBJECT;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public Set<Map.Entry<String, JsonValue>> entrySet() {
        return Collections.unmodifiableSet(values.entrySet());
    }

    @Override
    public boolean containsKey(Object key) {
        return values.containsKey(key);
    }

    @Override
    public JsonValue get(Object key) {
        return values.get(key);
    }

    @Override
    public JsonArray getJsonArray(String name) {
        return (JsonArray) values.get(name);
    }

    @Override
    public JsonObject getJsonObject(String name) {
        return (JsonObject) values.get(name);
    }

    @Override
    public JsonNumber getJsonNumber(String name) {
        return (JsonNumber) values.get(name);
    }

    @Override
    public JsonString getJsonString(String name) {
        return (JsonString) values.get(name);
    }

    @Override
    public String getString(String name) {
        return ((JsonString) values.get(name)).getString();
    }

    @Override
    public String getString(String name, String defaultValue) {
        if (!values.containsKey(name)) {
            return defaultValue;
        }

        JsonValue value = values.get(name);
        if(!value.getValueType().equals(ValueType.STRING)) {
            return defaultValue;
        }

        return ((JsonString) values.get(name)).getString();
    }

    @Override
    public int getInt(String name) {
        return ((JsonNumber) values.get(name)).intValue();
    }

    @Override
    public int getInt(String name, int defaultValue) {
        if (!values.containsKey(name)) {
            return defaultValue;
        }

        JsonValue value = values.get(name);
        if(!value.getValueType().equals(ValueType.NUMBER)) {
            return defaultValue;
        }

        return ((JsonNumber) values.get(name)).intValue();
    }

    @Override
    public boolean getBoolean(String name) {
        JsonValue value = values.get(name);
        Objects.requireNonNull(value);
        if (value == JsonValue.TRUE) {
            return true;
        } else if (value == JsonValue.FALSE) {
            return false;
        } else {
            throw new ClassCastException(String.format(
                "The value of type %s at the name '%s' is not JsonValue.TRUE or JsonValue.FALSE",
                value.getClass(), name));
        }
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        if (!values.containsKey(name)) {
            return defaultValue;
        }

        JsonValue value = values.get(name);
        if (value == JsonValue.TRUE) {
            return true;
        } else if (value == JsonValue.FALSE) {
            return false;
        } else {
            return defaultValue;
        }
    }

    @Override
    public boolean isNull(String name) {
        return Objects.requireNonNull(values.get(name)) == JsonValue.NULL;
    }

    @Override
    public String toString() {
        return Utils.writeValueToString(this);
    }
}
