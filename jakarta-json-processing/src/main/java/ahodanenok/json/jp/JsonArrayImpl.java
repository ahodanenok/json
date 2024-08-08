package ahodanenok.json.jp;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class JsonArrayImpl extends AbstractList<JsonValue> implements JsonArray {

    private final List<JsonValue> values;

    JsonArrayImpl(List<JsonValue> values) {
        this.values = values;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    @Override
    public JsonValue get(int index) {
        return values.get(index);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public JsonObject getJsonObject(int index) {
        return values.get(index).asJsonObject();
    }

    @Override
    public JsonArray getJsonArray(int index) {
        return values.get(index).asJsonArray();
    }

    @Override
    public JsonNumber getJsonNumber(int index) {
        return (JsonNumber) values.get(index);
    }

    @Override
    public JsonString getJsonString(int index) {
        return (JsonString) values.get(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
        return (List<T>) this;
    }

    @Override
    public String getString(int index) {
        return ((JsonString) values.get(index)).getString();
    }

    @Override
    public String getString(int index, String defaultValue) {
        if (index < 0 || index >= values.size()) {
            return defaultValue;
        }

        JsonValue value = values.get(index);
        if (!value.getValueType().equals(ValueType.STRING)) {
            return defaultValue;
        }

        return ((JsonString) values.get(index)).getString();
    }

    @Override
    public int getInt(int index) {
        return ((JsonNumber) values.get(index)).intValue();
    }

    @Override
    public int getInt(int index, int defaultValue) {
        if (index < 0 || index >= values.size()) {
            return defaultValue;
        }

        JsonValue value = values.get(index);
        if (!value.getValueType().equals(ValueType.NUMBER)) {
            return defaultValue;
        }

        return ((JsonNumber) values.get(index)).intValue();
    }

    @Override
    public boolean getBoolean(int index) {
        JsonValue value = values.get(index);
        if (value == JsonValue.TRUE) {
            return true;
        } else if (value == JsonValue.FALSE) {
            return false;
        } else {
            throw new ClassCastException(String.format(
                "The value of type %s at the position %d is not JsonValue.TRUE or JsonValue.FALSE",
                value.getClass(), index));
        }
    }

    @Override
    public boolean getBoolean(int index, boolean defaultValue) {
        if (index < 0 || index >= values.size()) {
            return defaultValue;
        }

        JsonValue value = values.get(index);
        if (value == JsonValue.TRUE) {
            return true;
        } else if (value == JsonValue.FALSE) {
            return false;
        } else {
            return defaultValue;
        }
    }

    @Override
    public boolean isNull(int index) {
        return values.get(index) == JsonValue.NULL;
    }

    @Override
    public String toString() {
        return Utils.writeValueToString(this);
    }
}
