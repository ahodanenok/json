package ahodanenok.json.parser.pull;

import java.io.Reader;

import ahodanenok.json.parser.tokenizer.DefaultJsonTokenizer;
import ahodanenok.json.parser.tokenizer.JsonToken;
import ahodanenok.json.parser.tokenizer.JsonTokenizer;
import ahodanenok.json.parser.tokenizer.TokenType;

public final class DefaultJsonStreamingParser implements JsonStreamingParser {

    private final JsonTokenizer tokenizer;

    private EventType event;

    public DefaultJsonStreamingParser(Reader reader) {
        this.tokenizer = new DefaultJsonTokenizer(reader);
    }

    @Override
    public boolean next() {
        if (!tokenizer.advance()) {
            return false;
        }

        JsonToken token = tokenizer.currentToken();
        if (token.getType().equals(TokenType.STRING)) {
            event = EventType.STRING;
        } else if (token.getType().equals(TokenType.NUMBER)) {
            event = EventType.NUMBER;
        } else if (token.getType().equals(TokenType.NULL)) {
            event = EventType.NULL;
        } else if (token.getType().equals(TokenType.TRUE)) {
            event = EventType.BOOLEAN;
        } else if (token.getType().equals(TokenType.FALSE)) {
            event = EventType.BOOLEAN;
        } else {
            // todo: custom exception?
            throw new IllegalStateException(token.getType().name());
        }

        // todo: throw error if multiple values at the root

        return true;
    }

    @Override
    public EventType currentEvent() {
        if (event == null) {
            // todo: custom exception?
            throw new IllegalStateException("no event");
        }

        return event;
    }

    @Override
    public String getString() {
        // todo: what if not a string
        return tokenizer.currentToken().stringValue();
    }

    @Override
    public double getDouble() {
        // todo: what if not a number
        return tokenizer.currentToken().doubleValue();
    }

    @Override
    public boolean getBoolean() {
        TokenType tokenType = tokenizer.currentToken().getType();
        if (tokenType == TokenType.TRUE) {
            return true;
        } else if (tokenType == TokenType.FALSE) {
            return false;
        } else {
            // todo: what if not a boolean
            throw new IllegalStateException("not a boolean");
        }
    }

    @Override
    public boolean isNull() {
        return event == EventType.NULL;
    }
}
