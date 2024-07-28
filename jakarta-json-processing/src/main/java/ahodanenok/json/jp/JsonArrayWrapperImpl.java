package ahodanenok.json.jp;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import java.util.AbstractList;
import java.util.List;

final class JsonArrayWrapperImpl extends AbstractList<JsonValue> implements JsonArray {

    private ahodanenok.json.value.JsonArray array;

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    @Override
    public JsonValue get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public JsonObject getJsonObject(int index) {
        return null;
    }

    @Override
    public JsonArray getJsonArray(int index) {
        return null;
    }

    @Override
    public JsonNumber getJsonNumber(int index) {
        return null;
    }

    @Override
    public JsonString getJsonString(int index) {
        return null;
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
        return null;
    }

    @Override
    public String getString(int index) {
        return null;
    }

    @Override
    public String getString(int index, String defaultValue) {
        return null;
    }

    @Override
    public int getInt(int index) {
        return 0;
    }

    @Override
    public int getInt(int index, int defaultValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(int index) {
        return false;
    }

    @Override
    public boolean getBoolean(int index, boolean defaultValue) {
        return false;
    }

    @Override
    public boolean isNull(int index) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
