package ahodanenok.json.jp;

import java.util.AbstractMap;
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
        return 0;
    }

    @Override
    public Set<Map.Entry<String, JsonValue>> entrySet() {
        return null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public JsonValue get(Object key) {
        return null;
    }

    @Override
    public JsonArray getJsonArray(String name) {
        return null;
    }

    @Override
    public JsonObject getJsonObject(String name) {
        return null;
    }

    @Override
    public JsonNumber getJsonNumber(String name) {
        return null;
    }

    @Override
    public JsonString getJsonString(String name) {
        return null;
    }

    @Override
    public String getString(String name) {
        return null;
    }

    @Override
    public String getString(String name, String defaultValue) {
        return null;
    }

    @Override
    public int getInt(String name) {
        return 0;
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(String name) {
        return false;
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        return false;
    }

    @Override
    public boolean isNull(String name) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
