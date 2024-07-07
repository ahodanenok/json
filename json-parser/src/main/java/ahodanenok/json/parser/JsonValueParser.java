package ahodanenok.json.parser;

import java.io.Reader;

import ahodanenok.json.value.JsonValue;

public interface JsonValueParser {

    JsonValue readValue(Reader reader);
}
