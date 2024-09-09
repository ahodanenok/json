package ahodanenok.json.jp;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;

final class JsonPatchBuilderImpl implements JsonPatchBuilder {

    private final List<JsonValue> operations;

    JsonPatchBuilderImpl() {
        this.operations = new ArrayList<>();
    }

    JsonPatchBuilderImpl(JsonArray operations) {
        this.operations = new ArrayList<>(operations);
    }

    @Override
    public JsonPatchBuilder add(String path, JsonValue value) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(value);
        operations.add(new JsonObjectImpl(Map.of(
            JsonPatchImpl.ATTR_OP, new JsonStringImpl(JsonPatchImpl.OP_ADD),
            JsonPatchImpl.ATTR_PATH, new JsonStringImpl(path),
            JsonPatchImpl.ATTR_VALUE, value
        )));

        return this;
    }

    @Override
    public JsonPatchBuilder add(String path, String value) {
        Objects.requireNonNull(value);
        add(path, new JsonStringImpl(value));

        return this;
    }

    @Override
    public JsonPatchBuilder add(String path, int value) {
        add(path, new JsonNumberIntegerImpl(value));
        return this;
    }

    @Override
    public JsonPatchBuilder add(String path, boolean value) {
        if (value) {
            add(path, JsonValue.TRUE);
        } else {
            add(path, JsonValue.FALSE);
        }

        return this;
    }

    @Override
    public JsonPatchBuilder remove(String path) {
        Objects.requireNonNull(path);
        operations.add(new JsonObjectImpl(Map.of(
            JsonPatchImpl.ATTR_OP, new JsonStringImpl(JsonPatchImpl.OP_REMOVE),
            JsonPatchImpl.ATTR_PATH, new JsonStringImpl(path)
        )));

        return this;
    }

    @Override
    public JsonPatchBuilder replace(String path, JsonValue value) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(value);
        operations.add(new JsonObjectImpl(Map.of(
            JsonPatchImpl.ATTR_OP, new JsonStringImpl(JsonPatchImpl.OP_REPLACE),
            JsonPatchImpl.ATTR_PATH, new JsonStringImpl(path),
            JsonPatchImpl.ATTR_VALUE, value
        )));

        return this;
    }

    @Override
    public JsonPatchBuilder replace(String path, String value) {
        Objects.requireNonNull(value);
        replace(path, new JsonStringImpl(value));

        return this;
    }

    @Override
    public JsonPatchBuilder replace(String path, int value) {
        replace(path, new JsonNumberIntegerImpl(value));
        return this;
    }

    @Override
    public JsonPatchBuilder replace(String path, boolean value) {
        if (value) {
            replace(path, JsonValue.TRUE);
        } else {
            replace(path, JsonValue.FALSE);
        }

        return this;
    }

    @Override
    public JsonPatchBuilder move(String path, String from) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(from);
        operations.add(new JsonObjectImpl(Map.of(
            JsonPatchImpl.ATTR_OP, new JsonStringImpl(JsonPatchImpl.OP_MOVE),
            JsonPatchImpl.ATTR_PATH, new JsonStringImpl(path),
            JsonPatchImpl.ATTR_FROM, new JsonStringImpl(from)
        )));

        return this;
    }

    @Override
    public JsonPatchBuilder copy(String path, String from) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(from);
        operations.add(new JsonObjectImpl(Map.of(
            JsonPatchImpl.ATTR_OP, new JsonStringImpl(JsonPatchImpl.OP_COPY),
            JsonPatchImpl.ATTR_PATH, new JsonStringImpl(path),
            JsonPatchImpl.ATTR_FROM, new JsonStringImpl(from)
        )));

        return this;
    }

    @Override
    public JsonPatchBuilder test(String path, JsonValue value) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(value);
        operations.add(new JsonObjectImpl(Map.of(
            JsonPatchImpl.ATTR_OP, new JsonStringImpl(JsonPatchImpl.OP_TEST),
            JsonPatchImpl.ATTR_PATH, new JsonStringImpl(path),
            JsonPatchImpl.ATTR_VALUE, value
        )));

        return this;
    }

    @Override
    public JsonPatchBuilder test(String path, String value) {
        Objects.requireNonNull(value);
        test(path, new JsonStringImpl(value));

        return this;
    }

    @Override
    public JsonPatchBuilder test(String path, int value) {
        test(path, new JsonNumberIntegerImpl(value));
        return this;
    }

    @Override
    public JsonPatchBuilder test(String path, boolean value) {
        if (value) {
            test(path, JsonValue.TRUE);
        } else {
            test(path, JsonValue.FALSE);
        }

        return this;
    }

    @Override
    public JsonPatch build() {
        return new JsonPatchImpl(new JsonArrayImpl(new ArrayList<>(operations)));
    }
}
