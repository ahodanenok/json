package ahodanenok.json.parser.tokenizer;

import java.math.BigDecimal;

public abstract sealed class JsonToken permits JsonLiteralToken, JsonNumberToken, JsonStringToken {

    static final JsonToken BEGIN_ARRAY = new JsonLiteralToken(TokenType.BEGIN_ARRAY, "[");
    static final JsonToken END_ARRAY = new JsonLiteralToken(TokenType.END_ARRAY, "]");
    static final JsonToken BEGIN_OBJECT = new JsonLiteralToken(TokenType.BEGIN_OBJECT, "{");
    static final JsonToken END_OBJECT = new JsonLiteralToken(TokenType.END_OBJECT, "}");
    static final JsonToken NAME_SEPARATOR = new JsonLiteralToken(TokenType.NAME_SEPARATOR, ":");
    static final JsonToken VALUE_SEPARATOR = new JsonLiteralToken(TokenType.VALUE_SEPARATOR, ",");
    static final JsonToken TRUE = new JsonLiteralToken(TokenType.TRUE, "true");
    static final JsonToken FALSE = new JsonLiteralToken(TokenType.FALSE, "false");
    static final JsonToken NULL = new JsonLiteralToken(TokenType.NULL, "null");

    private final TokenType type;

    JsonToken(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    public int intValue() {
        throw new UnsupportedOperationException(
            String.format("Token of type '%s' is not a number", type));
    }

    public long longValue() {
        throw new UnsupportedOperationException(
            String.format("Token of type '%s' is not a number", type));
    }

    public double doubleValue() {
        throw new UnsupportedOperationException(
            String.format("Token of type '%s' is not a number", type));
    }

    public BigDecimal bigDecimalValue() {
        throw new UnsupportedOperationException(
            String.format("Token of type '%s' is not a number", type));
    }

    public String stringValue() {
        throw new UnsupportedOperationException(
            String.format("Token of type '%s' is not a string", type));
    }

    public abstract String getRepresentation();
}
