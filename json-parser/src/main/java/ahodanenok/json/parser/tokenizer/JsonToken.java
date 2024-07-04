package ahodanenok.json.parser.tokenizer;

public class JsonToken {

    static final JsonToken BEGIN_ARRAY = new JsonToken(TokenType.BEGIN_ARRAY);
    static final JsonToken END_ARRAY = new JsonToken(TokenType.END_ARRAY);
    static final JsonToken BEGIN_OBJECT = new JsonToken(TokenType.BEGIN_OBJECT);
    static final JsonToken END_OBJECT = new JsonToken(TokenType.END_OBJECT);
    static final JsonToken NAME_SEPARATOR = new JsonToken(TokenType.NAME_SEPARATOR);
    static final JsonToken VALUE_SEPARATOR = new JsonToken(TokenType.VALUE_SEPARATOR);
    static final JsonToken TRUE = new JsonToken(TokenType.TRUE);
    static final JsonToken FALSE = new JsonToken(TokenType.FALSE);
    static final JsonToken NULL = new JsonToken(TokenType.NULL);

    private final TokenType type;

    JsonToken(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }
}
