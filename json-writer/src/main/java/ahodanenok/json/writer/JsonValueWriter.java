package ahodanenok.json.writer;

import java.io.Writer;

import ahodanenok.json.value.JsonValue;

public interface JsonValueWriter {

    void writeValue(JsonValue value, Writer writer);
}
