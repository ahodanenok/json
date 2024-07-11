package ahodanenok.json.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import ahodanenok.json.parser.tokenizer.DefaultJsonTokenizer;
import ahodanenok.json.parser.tokenizer.JsonToken;
import ahodanenok.json.parser.tokenizer.JsonTokenizer;
import ahodanenok.json.parser.tokenizer.TokenType;
import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonBoolean;
import ahodanenok.json.value.JsonNull;
import ahodanenok.json.value.JsonNumber;
import ahodanenok.json.value.JsonObject;
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
        JsonValue value = readValue(tokenizer, true);
        if (tokenizer.advance()) {
            throw new JsonParseException(
                String.format("Unexpected token '%s' after the value", tokenizer.currentToken().getRepresentation()),
                tokenizer.halt());
        }

        return value;
    }

    private JsonValue readValue(JsonTokenizer tokenizer, boolean advance) {
        if (advance && !tokenizer.advance()) {
            return null;
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
        } else if (token.getType().equals(TokenType.BEGIN_OBJECT)) {
            return readObject(tokenizer);
        } else {
            throw new JsonParseException(
                String.format("Unexpected token '%s'", token.getRepresentation()),
                tokenizer.halt());
        }
    }

    private JsonArray readArray(JsonTokenizer tokenizer) {
        List<JsonValue> items = new ArrayList<>();
        while (true) {
            if (!tokenizer.advance()) {
                throw new JsonParseException("Unexpected end of an array", tokenizer.halt());
            }

            JsonToken token = tokenizer.currentToken();
            if (token.getType().equals(TokenType.END_ARRAY)) {
                break;
            } else if (!items.isEmpty() && !token.getType().equals(TokenType.VALUE_SEPARATOR)) {
                throw new JsonParseException(
                    String.format("Expected ',' but '%s' was encountered", token.getRepresentation()),
                    tokenizer.halt());
            }

            JsonValue item = readValue(tokenizer, !items.isEmpty());
            if (item == null) {
                throw new JsonParseException("Unexpected end of an array", tokenizer.halt());
            }

            items.add(item);
        }

        return new JsonArray(items);
    }

    private JsonObject readObject(JsonTokenizer tokenizer) {
        Map<String, JsonValue> values = new LinkedHashMap<>();
        while (true) {
            if (!tokenizer.advance()) {
                throw new JsonParseException("Unexpected end of an object", tokenizer.halt());
            }

            JsonToken token = tokenizer.currentToken();
            if (token.getType().equals(TokenType.END_OBJECT)) {
                break;
            }

            if (!values.isEmpty()) {
                if (!token.getType().equals(TokenType.VALUE_SEPARATOR)) {
                    throw new JsonParseException(
                        String.format("Expected ',' but '%s' was encountered", token.getRepresentation()),
                        tokenizer.halt());
                }

                if (!tokenizer.advance()) {
                    throw new JsonParseException("Unexpected end of an object", tokenizer.halt());
                }
            }

            token = tokenizer.currentToken();
            if (!token.getType().equals(TokenType.STRING)) {
                throw new JsonParseException(
                    String.format("Expected name, but '%s' was encountered", token.getRepresentation()),
                    tokenizer.halt());
            }

            String name = token.stringValue();

            if (!tokenizer.advance()) {
                throw new JsonParseException("Unexpected end of an object", tokenizer.halt());
            }

            token = tokenizer.currentToken();
            if (!token.getType().equals(TokenType.NAME_SEPARATOR)) {
                throw new JsonParseException(
                    String.format("Expected ':', but '%s' was encountered", token.getRepresentation()),
                    tokenizer.halt());
            }

            JsonValue value = readValue(tokenizer, true);
            if (value == null) {
                throw new JsonParseException("Unexpected end of an object", tokenizer.halt());
            }

            // todo: strategy for handling duplicate names
            values.put(name, value);
        }

        return new JsonObject(values);
    }
}
