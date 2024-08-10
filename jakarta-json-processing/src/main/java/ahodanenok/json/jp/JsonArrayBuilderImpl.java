package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonArrayBuilderImpl implements JsonArrayBuilder {

    private final List<JsonValue> values;

    JsonArrayBuilderImpl() {
        this.values = new ArrayList<>();
    }

    @Override
    public JsonArrayBuilder add(JsonValue value) {
        Objects.requireNonNull(value);
        values.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(String value) {
        Objects.requireNonNull(value);
        values.add(new JsonStringImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(BigDecimal value) {
        Objects.requireNonNull(value);
        values.add(new JsonNumberBigDecimalImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(BigInteger value) {
        Objects.requireNonNull(value);
        values.add(new JsonNumberBigIntegerImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int value) {
        values.add(new JsonNumberIntegerImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(long value) {
        values.add(new JsonNumberLongImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(double value) {
        Utils.checkDouble(value);
        values.add(new JsonNumberDoubleImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(boolean value) {
        values.add(value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    @Override
    public JsonArrayBuilder addNull() {
        values.add(JsonValue.NULL);
        return this;
    }

    @Override
    public JsonArrayBuilder add(JsonObjectBuilder builder) {
        Objects.requireNonNull(builder);
        values.add(builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder add(JsonArrayBuilder builder) {
        Objects.requireNonNull(builder);
        values.add(builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder addAll(JsonArrayBuilder builder) {
        Objects.requireNonNull(builder);
        values.addAll(builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonValue value) {
        Objects.requireNonNull(value);
        values.add(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, String value) {
        Objects.requireNonNull(value);
        values.add(index, new JsonStringImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, BigDecimal value) {
        Objects.requireNonNull(value);
        values.add(index, new JsonNumberBigDecimalImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, BigInteger value) {
        Objects.requireNonNull(value);
        values.add(index, new JsonNumberBigIntegerImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, int value) {
        values.add(index, new JsonNumberIntegerImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, long value) {
        values.add(index, new JsonNumberLongImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, double value) {
        Utils.checkDouble(value);
        values.add(index, new JsonNumberDoubleImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, boolean value) {
        values.add(index, value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    @Override
    public JsonArrayBuilder addNull(int index) {
        values.add(index, JsonValue.NULL);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonObjectBuilder builder) {
        Objects.requireNonNull(builder);
        values.add(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonArrayBuilder builder) {
        Objects.requireNonNull(builder);
        values.add(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonValue value) {
        Objects.requireNonNull(value);
        values.set(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, String value) {
        Objects.requireNonNull(value);
        values.set(index, new JsonStringImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, BigDecimal value) {
        Objects.requireNonNull(value);
        values.set(index, new JsonNumberBigDecimalImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, BigInteger value) {
        Objects.requireNonNull(value);
        values.set(index, new JsonNumberBigIntegerImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, int value) {
        values.set(index, new JsonNumberIntegerImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, long value) {
        values.set(index, new JsonNumberLongImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, double value) {
        Utils.checkDouble(value);
        values.set(index, new JsonNumberDoubleImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, boolean value) {
        values.set(index, value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    @Override
    public JsonArrayBuilder setNull(int index) {
        values.set(index, JsonValue.NULL);
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonObjectBuilder builder) {
        Objects.requireNonNull(builder);
        values.set(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonArrayBuilder builder) {
        Objects.requireNonNull(builder);
        values.set(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder remove(int index) {
        values.remove(index);
        return this;
    }

    @Override
    public JsonArray build() {
        return new JsonArrayImpl(new ArrayList<>(values));
    }
}
