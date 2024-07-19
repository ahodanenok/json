package ahodanenok.json.writer;

import java.io.IOException;
import java.io.Writer;

import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonValue;
import ahodanenok.json.value.ValueType;

public final class DefaultJsonValueWriter implements JsonValueWriter {

    @Override
    public void writeValue(JsonValue value, Writer writer) {
        if (value == null) {
            return;
        }

        try {
            doWriteValue(value, writer);
        } catch (IOException e) {
            // todo: custom exception?
            throw new RuntimeException(e);
        }
    }

    private void doWriteValue(JsonValue value, Writer writer) throws IOException {
        ValueType type = value.getType();
        if (type.equals(ValueType.STRING)) {
            writer.write('"');
            writer.write(value.asString().getValue()); // todo: escape
            writer.write('"');
        } else if (type.equals(ValueType.NUMBER)) {
            writer.write(Double.toString(value.asNumber().doubleValue())); // todo: does it format according to rfc
        } else if (type.equals(ValueType.BOOLEAN)) {
            writer.write(value.asBoolean().getValue() ? "true" : "false");
        } else if (type.equals(ValueType.NULL)) {
            writer.write("null");
        } else if (type.equals(ValueType.ARRAY)) {
            writeArray(value.asArray(), writer);
        } else {
            throw new IllegalStateException(String.format("Unsupported value type '%s'", type));
        }
    }

    private void writeArray(JsonArray array, Writer writer) throws IOException {
        writer.write('[');
        for (int i = 0, n = array.size(); i < n; i++) {
            if (i > 0) {
                writer.write(',');
            }
            doWriteValue(array.getItem(i), writer);
        }
        writer.write(']');
    }
}
