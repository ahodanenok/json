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

final class JsonArrayWrapperImpl extends AbstractList<JsonValue> implements JsonArray {

    private final ahodanenok.json.value.JsonArray array;

    JsonArrayWrapperImpl(ahodanenok.json.value.JsonArray array) {
        this.array = Objects.requireNonNull(array);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    @Override
    public JsonValue get(int index) {
        ahodanenok.json.value.JsonValue value = array.getItem(index);
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
    public int size() {
        return array.size();
    }

    @Override
    public JsonObject getJsonObject(int index) {
        return new JsonObjectWrapperImpl(array.getItem(index).asObject());
    }

    @Override
    public JsonArray getJsonArray(int index) {
        return new JsonArrayWrapperImpl(array.getItem(index).asArray());
    }

    @Override
    public JsonNumber getJsonNumber(int index) {
        return new JsonNumberDoubleImpl(array.getItem(index).asNumber().doubleValue());
    }

    @Override
    public JsonString getJsonString(int index) {
        return new JsonStringImpl(array.getItem(index).asString().getValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
        return (List<T>) Collections.unmodifiableList(this);
    }

    @Override
    public String getString(int index) {
        return array.getItem(index).asString().getValue();
    }

    @Override
    public String getString(int index, String defaultValue) {
        if (index < 0 || index >= array.size()) {
            return defaultValue;
        }

        ahodanenok.json.value.JsonValue value = array.getItem(index);
        if (!value.getType().equals(ahodanenok.json.value.ValueType.STRING)) {
            return defaultValue;
        }

        return value.asString().getValue();
    }

    @Override
    public int getInt(int index) {
        return (int) array.getItem(index).asNumber().doubleValue();
    }

    @Override
    public int getInt(int index, int defaultValue) {
        if (index < 0 || index >= array.size()) {
            return defaultValue;
        }

        ahodanenok.json.value.JsonValue value = array.getItem(index);
        if (!value.getType().equals(ahodanenok.json.value.ValueType.NUMBER)) {
            return defaultValue;
        }

        return (int) value.asNumber().doubleValue();
    }

    @Override
    public boolean getBoolean(int index) {
        return array.getItem(index).asBoolean().getValue();
    }

    @Override
    public boolean getBoolean(int index, boolean defaultValue) {
        if (index < 0 || index >= array.size()) {
            return defaultValue;
        }

        ahodanenok.json.value.JsonValue value = array.getItem(index);
        if (!value.getType().equals(ahodanenok.json.value.ValueType.BOOLEAN)) {
            return defaultValue;
        }

        return (boolean) value.asBoolean().getValue();
    }

    @Override
    public boolean isNull(int index) {
        return array.getItem(index).isNull();
    }

    @Override
    public String toString() {
        return null;
    }
}
