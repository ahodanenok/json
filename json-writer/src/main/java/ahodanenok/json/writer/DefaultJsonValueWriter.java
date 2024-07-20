package ahodanenok.json.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonObject;
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
           writeString(value.asString().getValue(), writer);
        } else if (type.equals(ValueType.NUMBER)) {
            writer.write(Double.toString(value.asNumber().doubleValue())); // todo: does it format according to rfc
        } else if (type.equals(ValueType.BOOLEAN)) {
            writer.write(value.asBoolean().getValue() ? "true" : "false");
        } else if (type.equals(ValueType.NULL)) {
            writer.write("null");
        } else if (type.equals(ValueType.ARRAY)) {
            writeArray(value.asArray(), writer);
        } else if (type.equals(ValueType.OBJECT)) {
            writeObject(value.asObject(), writer);
        } else {
            throw new IllegalStateException(String.format("Unsupported value type '%s'", type));
        }
    }

    private void writeString(String string, Writer writer) throws IOException {
        writer.write('"');
        writer.write(string); // todo: escape
        writer.write('"');
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

    private void writeObject(JsonObject object, Writer writer) throws IOException {
        writer.write('{');
        Iterator<String> names = object.getNames().iterator();
        String name;
        if (names.hasNext()) {
            name = names.next();
            writeString(name, writer);
            writer.write(':');
            doWriteValue(object.getValue(name), writer);
        }
        while (names.hasNext()) {
            writer.write(',');
            name = names.next();
            writeString(name, writer);
            writer.write(':');
            doWriteValue(object.getValue(name), writer);
        }
        writer.write('}');
    }
}
