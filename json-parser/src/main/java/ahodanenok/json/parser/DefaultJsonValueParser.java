package ahodanenok.json.parser;

import java.io.Reader;

import ahodanenok.json.parser.tokenizer.DefaultJsonTokenizer;
import ahodanenok.json.parser.tokenizer.JsonToken;
import ahodanenok.json.parser.tokenizer.JsonTokenizer;
import ahodanenok.json.parser.tokenizer.TokenType;
import ahodanenok.json.value.JsonString;
import ahodanenok.json.value.JsonValue;

public final class DefaultJsonValueParser implements JsonValueParser {

    // todo: config

    public DefaultJsonValueParser() {

    }

    @Override
    public JsonValue readValue(Reader reader) {
        // todo: how to customize a tokenizer? maybe with a factory?
        JsonTokenizer tokenizer = new DefaultJsonTokenizer(reader);

        return readValue(tokenizer);
    }

    private JsonValue readValue(JsonTokenizer tokenizer) {
        if (!tokenizer.advance()) {
            return null; // todo: exception?
        }

        JsonToken token = tokenizer.currentToken();
        if (token.getType().equals(TokenType.STRING)) {
            return new JsonString(token.stringValue());
        } else {
            throw new IllegalStateException("Unknown token: " + token.getType());
        }
    }
}