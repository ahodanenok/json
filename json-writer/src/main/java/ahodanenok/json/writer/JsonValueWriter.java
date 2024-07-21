package ahodanenok.json.writer;

import ahodanenok.json.value.JsonValue;

public interface JsonValueWriter {

    void writeValue(JsonValue value, JsonOutput writer);
}
