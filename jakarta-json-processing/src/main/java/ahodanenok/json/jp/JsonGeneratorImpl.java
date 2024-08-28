package ahodanenok.json.jp;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonNumber;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerationException;
import jakarta.json.stream.JsonGenerator;

import ahodanenok.json.writer.DefaultJsonStreamingWriter;
import ahodanenok.json.writer.JsonOutput;
import ahodanenok.json.writer.JsonStreamingWriter;
import ahodanenok.json.writer.JsonWriteException;
import ahodanenok.json.writer.JsonWriteIOException;

final class JsonGeneratorImpl implements JsonGenerator {

    private final JsonOutput out;
    private final JsonStreamingWriter writer;

    JsonGeneratorImpl(JsonOutput out) {
        this.out = out;
        this.writer = new DefaultJsonStreamingWriter(out);
    }

    @Override
    public JsonGenerator writeStartObject() {
        execute(() -> writer.writeBeginObject());
        return this;
    }

    @Override
    public JsonGenerator writeStartObject(String name) {
        execute(() -> {
            writer.writeName(name);
            writer.writeBeginObject();
        });
        return this;
    }

    @Override
    public JsonGenerator writeKey(String name) {
        execute(() -> writer.writeName(name));
        return this;
    }

    @Override
    public JsonGenerator writeStartArray() {
        execute(() -> writer.writeBeginArray());
        return this;
    }

    @Override
    public JsonGenerator writeStartArray(String name) {
        execute(() -> {
            writer.writeName(name);
            writer.writeBeginArray();
        });
        return this;
    }

    @Override
    public JsonGenerator write(String name, JsonValue value) {
        execute(() -> writer.writeName(name));
        return write(value);
    }

    @Override
    public JsonGenerator write(String name, String value) {
        execute(() -> {
            writer.writeName(name);
            writer.writeString(value);
        });
        return this;
    }

    @Override
    public JsonGenerator write(String name, BigInteger value) {
        execute(() -> {
            writer.writeName(name);
            writer.writeNumber(value);
        });
        return this;
    }

    @Override
    public JsonGenerator write(String name, BigDecimal value) {
        execute(() -> {
            writer.writeName(name);
            writer.writeNumber(value);
        });
        return this;
    }

    @Override
    public JsonGenerator write(String name, int value) {
        execute(() -> {
            writer.writeName(name);
            writer.writeNumber(value);
        });
        return this;
    }

    @Override
    public JsonGenerator write(String name, long value) {
        execute(() -> {
            writer.writeName(name);
            writer.writeNumber(value);
        });
        return this;
    }

    @Override
    public JsonGenerator write(String name, double value) {
        execute(() -> {
            writer.writeName(name);
            writer.writeNumber(value);
        });
        return this;
    }

    @Override
    public JsonGenerator write(String name, boolean value) {
        execute(() -> {
            writer.writeName(name);
            writer.writeBoolean(value);
        });
        return this;
    }

    @Override
    public JsonGenerator writeNull(String name) {
        execute(() -> {
            writer.writeName(name);
            writer.writeNull();
        });
        return this;
    }

    @Override
    public JsonGenerator writeEnd() {
        execute(() -> writer.writeEnd());
        return this;
    }

    @Override
    public JsonGenerator write(JsonValue value) {
        JsonValue.ValueType type = value.getValueType();
        if (type == JsonValue.ValueType.ARRAY) {
            JsonArray array = value.asJsonArray();
            writeStartArray();
            for (JsonValue item : array) {
                write(item);
            }
            return writeEnd();
        } else if (type == JsonValue.ValueType.OBJECT) {
            JsonObject object = value.asJsonObject();
            writeStartObject();
            for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
                write(entry.getKey(), entry.getValue());
            }
            return writeEnd();
        } else if (type == JsonValue.ValueType.NUMBER) {
            return write(((JsonNumber) value).bigDecimalValue());
        } else if (type == JsonValue.ValueType.STRING) {
            return write(((JsonString) value).getString());
        } else if (type == JsonValue.ValueType.TRUE) {
            return write(true);
        } else if (type == JsonValue.ValueType.FALSE) {
            return write(false);
        } else if (type == JsonValue.ValueType.NULL) {
            return writeNull();
        } else {
            throw new IllegalStateException("Unknown value type: " + type);
        }
    }

    @Override
    public JsonGenerator write(String value) {
        execute(() -> writer.writeString(value));
        return this;
    }

    @Override
    public JsonGenerator write(BigDecimal value) {
        execute(() -> writer.writeNumber(value));
        return this;
    }

    @Override
    public JsonGenerator write(BigInteger value) {
        execute(() -> writer.writeNumber(value));
        return this;
    }

    @Override
    public JsonGenerator write(int value) {
        execute(() -> writer.writeNumber(value));
        return this;
    }

    @Override
    public JsonGenerator write(long value) {
        execute(() -> writer.writeNumber(value));
        return this;
    }

    @Override
    public JsonGenerator write(double value) {
        execute(() -> writer.writeNumber(value));
        return this;
    }

    @Override
    public JsonGenerator write(boolean value) {
        execute(() -> writer.writeBoolean(value));
        return this;
    }

    @Override
    public JsonGenerator writeNull() {
        execute(() -> writer.writeNull());
        return this;
    }

    private void execute(Runnable action) {
        try {
            action.run();
        } catch (JsonWriteIOException e) {
            throw new JsonException(e.getMessage(), e.getCause());
        } catch (JsonWriteException e) {
            if (e.getCause() instanceof IOException) {
                throw new JsonException(e.getMessage(), e.getCause());
            } else {
                throw new JsonGenerationException(e.getMessage(), e.getCause());
            }
        }
    }

    @Override
    public void close() {
        execute(() -> writer.close());
    }

    @Override
    public void flush() {
        try {
            out.flush();
        } catch (IOException e) {
            throw new JsonException("Falied to flush output", e);
        }
    }
}
