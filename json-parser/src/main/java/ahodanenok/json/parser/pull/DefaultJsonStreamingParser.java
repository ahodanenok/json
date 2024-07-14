package ahodanenok.json.parser.pull;

import java.io.Reader;

import ahodanenok.json.parser.tokenizer.DefaultJsonTokenizer;
import ahodanenok.json.parser.tokenizer.JsonTokenizer;
import ahodanenok.json.parser.tokenizer.TokenType;

public final class DefaultJsonStreamingParser implements JsonStreamingParser {

    private final JsonTokenizer tokenizer;
    // private final Reader reader;

    private EventType event;

    public DefaultJsonStreamingParser(Reader reader) {
        this.tokenizer = new DefaultJsonTokenizer(reader);
        // this.reader = reader;
    }

    @Override
    public boolean next() {
        if (!tokenizer.advance()) {
            return false;
        }

        // todo: determine event, skip tokens as needed

        return false;
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
        return tokenizer.currentToken().getType() == TokenType.NULL;
    }
}
