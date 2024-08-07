package ahodanenok.json.jp;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;
import jakarta.json.JsonWriter;

import ahodanenok.json.writer.DefaultJsonStreamingWriter;
import ahodanenok.json.writer.JsonOutput;
import ahodanenok.json.writer.JsonStreamingWriter;
import ahodanenok.json.writer.JsonWriteException;

final class JsonWriterImpl implements JsonWriter {

    private final JsonOutput out;
    private boolean usable;

    JsonWriterImpl(JsonOutput out) {
        this.out = out;
        this.usable = true;
    }

    @Override
    public void writeArray(JsonArray array) {
        if (!usable) {
            throw new IllegalStateException("Writer is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingWriter writer = new DefaultJsonStreamingWriter(out);
        makeWrite(() -> doWriteArray(array, writer));
    }

    @Override
    public void writeObject(JsonObject object) {
        if (!usable) {
            throw new IllegalStateException("Writer is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingWriter writer = new DefaultJsonStreamingWriter(out);
        makeWrite(() -> doWriteObject(object, writer));
    }

    @Override
    public void write(JsonStructure value) {
        if (!usable) {
            throw new IllegalStateException("Writer is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingWriter writer = new DefaultJsonStreamingWriter(out);
        if (value.getValueType() == ValueType.ARRAY) {
            makeWrite(() -> doWriteArray(value.asJsonArray(), writer));
        } else if (value.getValueType() == ValueType.OBJECT) {
            makeWrite(() -> doWriteObject(value.asJsonObject(), writer));
        } else {
            throw new IllegalStateException(value.getValueType() + "");
        }
    }

    @Override
    public void write(JsonValue value) {
        if (!usable) {
            throw new IllegalStateException("Writer is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingWriter writer = new DefaultJsonStreamingWriter(out);
        makeWrite(() -> doWriteValue(value, writer));
    }

    private void doWriteValue(JsonValue value, JsonStreamingWriter writer) {
        ValueType type = value.getValueType();
        if (type.equals(ValueType.STRING)) {
            writer.writeString(((JsonString) value).getString());
        } else if (type.equals(ValueType.NUMBER)) {
            writer.writeNumber(((JsonNumber) value).doubleValue());
        } else if (type.equals(ValueType.TRUE)) {
            writer.writeBoolean(true);
        } else if (type.equals(ValueType.FALSE)) {
            writer.writeBoolean(false);
        } else if (type.equals(ValueType.NULL)) {
            writer.writeNull();
        } else if (type.equals(ValueType.ARRAY)) {
            doWriteArray(value.asJsonArray(), writer);
        } else if (type.equals(ValueType.OBJECT)) {
            doWriteObject(value.asJsonObject(), writer);
        } else {
            throw new IllegalStateException(type.name());
        }
    }

    private void doWriteArray(JsonArray array, JsonStreamingWriter writer) {
        writer.writeBeginArray();
        for (JsonValue value : array) {
            doWriteValue(value, writer);
        }
        writer.writeEnd();
    }

    private void doWriteObject(JsonObject object, JsonStreamingWriter writer) {
        writer.writeBeginObject();
        for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
            writer.writeName(entry.getKey());
            doWriteValue(entry.getValue(), writer);
        }
        writer.writeEnd();
    }

    private void makeWrite(Runnable action) {
        try {
            action.run();
        } catch (JsonWriteException e) {
            throw new JsonException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void close() {
        usable = false;
        try {
            out.close();
        } catch (IOException e) {
            throw new JsonException("Failed to close the writer", e);
        }
    }
}
