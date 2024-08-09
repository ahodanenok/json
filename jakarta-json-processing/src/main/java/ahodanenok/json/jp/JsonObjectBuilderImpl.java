package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonObjectBuilderImpl implements JsonObjectBuilder {

    @Override
    public JsonObjectBuilder add(String name, JsonValue value) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, String value) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, BigInteger value) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, BigDecimal value) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, int value) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, long value) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, double value) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, boolean value) {
        return null;
    }

    @Override
    public JsonObjectBuilder addNull(String name) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, JsonObjectBuilder builder) {
        return null;
    }

    @Override
    public JsonObjectBuilder add(String name, JsonArrayBuilder builder) {
        return null;
    }

    @Override
    public JsonObjectBuilder addAll(JsonObjectBuilder builder) {
        return null;
    }

    @Override
    public JsonObjectBuilder remove(String name) {
        return null;
    }

    @Override
    public JsonObject build() {
        return new JsonObjectImpl(Map.of());
    }
}
