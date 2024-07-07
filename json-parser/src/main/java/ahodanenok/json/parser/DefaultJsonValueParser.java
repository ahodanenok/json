package ahodanenok.json.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ahodanenok.json.parser.tokenizer.DefaultJsonTokenizer;
import ahodanenok.json.parser.tokenizer.JsonToken;
import ahodanenok.json.parser.tokenizer.JsonTokenizer;
import ahodanenok.json.parser.tokenizer.TokenType;
import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonBoolean;
import ahodanenok.json.value.JsonNull;
import ahodanenok.json.value.JsonNumber;
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

        return readValue(tokenizer, true);
    }

    private JsonValue readValue(JsonTokenizer tokenizer, boolean advance) {
        if (advance && !tokenizer.advance()) {
            return null; // todo: exception?
        }

        JsonToken token = tokenizer.currentToken();
        if (token.getType().equals(TokenType.STRING)) {
            return new JsonString(token.stringValue());
        } else if (token.getType().equals(TokenType.NUMBER)) {
            return new JsonNumber(token.doubleValue());
        } else if (token.getType().equals(TokenType.NULL)) {
            return new JsonNull();
        } else if (token.getType().equals(TokenType.TRUE)) {
            return new JsonBoolean(true);
        } else if (token.getType().equals(TokenType.FALSE)) {
            return new JsonBoolean(false);
        } else if (token.getType().equals(TokenType.BEGIN_ARRAY)) {
            return readArray(tokenizer);
        } else {
            // todo: throw JsonParseException
            throw new IllegalStateException("Unknown token: " + token.getType());
        }
    }

    private JsonArray readArray(JsonTokenizer tokenizer) {
        List<JsonValue> items = new ArrayList<>();
        while (true) {
            if (!tokenizer.advance()) {
                // todo: throw JsonParseException
                throw new IllegalStateException("Unexpected end of the array");
            }

            JsonToken token = tokenizer.currentToken();
            if (token.getType().equals(TokenType.END_ARRAY)) {
                break;
            } else if (!items.isEmpty() && !token.getType().equals(TokenType.VALUE_SEPARATOR)) {
                // todo: throw JsonParseException
                throw new IllegalStateException(String.format(
                    "Expected '%s' but '%s' was encountered",
                    TokenType.VALUE_SEPARATOR,
                    tokenizer.currentToken().getType()));
            }

            JsonValue item = readValue(tokenizer, !items.isEmpty());
            if (item == null) {
                // todo: throw JsonParseException
                throw new IllegalStateException("Unexpected end of the array");
            }

            items.add(item);
        }

        return new JsonArray(items);
    }
}
