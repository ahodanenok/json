package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonObjectBuilderImpl implements JsonObjectBuilder {

    private final Map<String, JsonValue> values;

    JsonObjectBuilderImpl() {
        this.values = new LinkedHashMap<>();
    }

    JsonObjectBuilderImpl(JsonObject object) {
        this.values = new LinkedHashMap<>(object);
    }

    @Override
    public JsonObjectBuilder add(String name, JsonValue value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        values.put(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, String value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        values.put(name, new JsonStringImpl(value));
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, BigInteger value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        values.put(name, new JsonNumberBigIntegerImpl(value));
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, BigDecimal value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        values.put(name, new JsonNumberBigDecimalImpl(value));
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, int value) {
        Objects.requireNonNull(name);
        values.put(name, new JsonNumberIntegerImpl(value));
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, long value) {
        Objects.requireNonNull(name);
        values.put(name, new JsonNumberLongImpl(value));
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, double value) {
        Objects.requireNonNull(name);
        Utils.checkDouble(value);
        values.put(name, new JsonNumberDoubleImpl(value));
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, boolean value) {
        Objects.requireNonNull(name);
        values.put(name, value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    @Override
    public JsonObjectBuilder addNull(String name) {
        Objects.requireNonNull(name);
        values.put(name, JsonValue.NULL);
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, JsonObjectBuilder builder) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(builder);
        values.put(name, builder.build());
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, JsonArrayBuilder builder) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(builder);
        values.put(name, builder.build());
        return this;
    }

    @Override
    public JsonObjectBuilder addAll(JsonObjectBuilder builder) {
        Objects.requireNonNull(builder);
        for (Map.Entry<String, JsonValue> entry : builder.build().entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public JsonObjectBuilder remove(String name) {
        Objects.requireNonNull(name);
        values.remove(name);
        return this;
    }

    @Override
    public JsonObject build() {
        Map<String, JsonValue> valuesLocal = new LinkedHashMap<>(values);
        values.clear();
        return new JsonObjectImpl(valuesLocal);
    }
}
