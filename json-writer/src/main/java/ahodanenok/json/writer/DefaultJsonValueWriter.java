package ahodanenok.json.writer;

import java.io.IOException;
import java.util.Iterator;

import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonObject;
import ahodanenok.json.value.JsonValue;
import ahodanenok.json.value.ValueType;

public final class DefaultJsonValueWriter implements JsonValueWriter {

    @Override
    public void writeValue(JsonValue value, JsonOutput writer) {
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

    private void doWriteValue(JsonValue value, JsonOutput writer) throws IOException {
        ValueType type = value.getType();
        if (type.equals(ValueType.STRING)) {
            writer.writeString(value.asString().getValue());
        } else if (type.equals(ValueType.NUMBER)) {
            writer.writeNumber(value.asNumber().doubleValue());
        } else if (type.equals(ValueType.BOOLEAN)) {
            writer.writeBoolean(value.asBoolean().getValue());
        } else if (type.equals(ValueType.NULL)) {
            writer.writeNull();
        } else if (type.equals(ValueType.ARRAY)) {
            writeArray(value.asArray(), writer);
        } else if (type.equals(ValueType.OBJECT)) {
            writeObject(value.asObject(), writer);
        } else {
            throw new IllegalStateException(String.format("Unsupported value type '%s'", type));
        }
    }

    private void writeArray(JsonArray array, JsonOutput writer) throws IOException {
        writer.writeBeginArray();
        int i = 0;
        int n = array.size();
        if (i < n) {
            doWriteValue(array.getItem(i), writer);
            i++;
        }
        while (i < n) {
            writer.writeValueSeparator();
            doWriteValue(array.getItem(i), writer);
            i++;
        }
        writer.writeEndArray();
    }

    private void writeObject(JsonObject object, JsonOutput writer) throws IOException {
        writer.writeBeginObject();
        Iterator<String> names = object.getNames().iterator();
        String name;
        if (names.hasNext()) {
            name = names.next();
            writer.writeString(name);
            writer.writeNameSeparator();
            doWriteValue(object.getValue(name), writer);
        }
        while (names.hasNext()) {
            writer.writeValueSeparator();
            name = names.next();
            writer.writeString(name);
            writer.writeNameSeparator();
            doWriteValue(object.getValue(name), writer);
        }
        writer.writeEndObject();
    }
}
