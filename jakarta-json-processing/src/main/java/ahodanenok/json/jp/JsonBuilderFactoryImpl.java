package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

final class JsonBuilderFactoryImpl implements JsonBuilderFactory {

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return new JsonObjectBuilderImpl();
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(JsonObject object) {
        Objects.requireNonNull(object);

        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        return builder;
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(Map<String, Object> object) {
        Objects.requireNonNull(object);

        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                builder.addNull(entry.getKey());
            } else if (value instanceof JsonValue v) {
                builder.add(entry.getKey(), v);
            } else if (value instanceof Integer n) {
                builder.add(entry.getKey(), n.intValue());
            } else if (value instanceof Long n) {
                builder.add(entry.getKey(), n.longValue());
            } else if (value instanceof Float n) {
                builder.add(entry.getKey(), n.doubleValue());
            } else if (value instanceof Double n) {
                builder.add(entry.getKey(), n.doubleValue());
            } else if (value instanceof BigInteger n) {
                builder.add(entry.getKey(), n);
            } else if (value instanceof BigDecimal n) {
                builder.add(entry.getKey(), n);
            } else if (value instanceof Number n) {
                builder.add(entry.getKey(), n.intValue());
            } else if (value instanceof String s) {
                builder.add(entry.getKey(), s);
            } else if (value instanceof Boolean b) {
                builder.add(entry.getKey(), b);
            } else {
                throw new IllegalStateException(String.format(
                    "Adding value of type %s is not supported", value.getClass()));
            }

        }

        return builder;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return new JsonArrayBuilderImpl();
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(JsonArray array) {
        Objects.requireNonNull(array);

        JsonArrayBuilder builder = new JsonArrayBuilderImpl();
        for (JsonValue value : array) {
            builder.add(value);
        }

        return builder;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(Collection<?> collection) {
        Objects.requireNonNull(collection);

        JsonArrayBuilder builder = new JsonArrayBuilderImpl();
        for (Object value : collection) {
            if (value == null) {
                builder.addNull();
            } else if (value instanceof JsonValue v) {
                builder.add(v);
            } else if (value instanceof Integer n) {
                builder.add(n.intValue());
            } else if (value instanceof Long n) {
                builder.add(n.longValue());
            } else if (value instanceof Float n) {
                builder.add(n.doubleValue());
            } else if (value instanceof Double n) {
                builder.add(n.doubleValue());
            } else if (value instanceof BigInteger n) {
                builder.add(n);
            } else if (value instanceof BigDecimal n) {
                builder.add(n);
            } else if (value instanceof Number n) {
                builder.add(n.intValue());
            } else if (value instanceof String s) {
                builder.add(s);
            } else if (value instanceof Boolean b) {
                builder.add(b);
            } else {
                throw new IllegalStateException(String.format(
                    "Adding value of type %s is not supported", value.getClass()));
            }

        }

        return builder;
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Map.of();
    }
}
