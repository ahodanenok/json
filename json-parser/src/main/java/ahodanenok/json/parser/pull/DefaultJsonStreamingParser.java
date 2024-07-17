package ahodanenok.json.parser.pull;

import java.io.Reader;
import java.util.LinkedList;

import ahodanenok.json.parser.tokenizer.DefaultJsonTokenizer;
import ahodanenok.json.parser.tokenizer.JsonToken;
import ahodanenok.json.parser.tokenizer.JsonTokenizer;
import ahodanenok.json.parser.tokenizer.TokenType;

public final class DefaultJsonStreamingParser implements JsonStreamingParser {

    private class EventContext {

        boolean isArray;
        boolean isObject;
        int valuePos;
    }

    private final JsonTokenizer tokenizer;

    private EventType event;
    private LinkedList<EventContext> contexts;

    public DefaultJsonStreamingParser(Reader reader) {
        this.tokenizer = new DefaultJsonTokenizer(reader);
        this.contexts = new LinkedList<>();
    }

    @Override
    public boolean next() {
        if (!tokenizer.advance()) {
            return false;
        }

        EventContext context;
        if (contexts.size() > 0) {
            context = contexts.peek();
        } else {
            context = null;
        }

        JsonToken token = tokenizer.currentToken();
        if (token.getType().equals(TokenType.STRING)
                && context != null && context.isObject && event != EventType.OBJECT_KEY) {
            event = EventType.OBJECT_KEY;
        } else if (token.getType().equals(TokenType.STRING)) {
            event = EventType.STRING;
            if (context != null) {
                context.valuePos++;
            }
        } else if (token.getType().equals(TokenType.NUMBER)) {
            event = EventType.NUMBER;
            if (context != null) {
                context.valuePos++;
            }
        } else if (token.getType().equals(TokenType.NULL)) {
            event = EventType.NULL;
            if (context != null) {
                context.valuePos++;
            }
        } else if (token.getType().equals(TokenType.TRUE)) {
            event = EventType.BOOLEAN;
            if (context != null) {
                context.valuePos++;
            }
        } else if (token.getType().equals(TokenType.FALSE)) {
            event = EventType.BOOLEAN;
            if (context != null) {
                context.valuePos++;
            }
        } else if (token.getType().equals(TokenType.VALUE_SEPARATOR)
                && context != null && context.valuePos > 0) {
            return next();
        } else if (token.getType().equals(TokenType.BEGIN_ARRAY)) {
            event = EventType.BEGIN_ARRAY;
            if (context != null) {
                context.valuePos++;
            }

            EventContext arrayContext = new EventContext();
            arrayContext.isArray = true;
            contexts.push(arrayContext);
        } else if (token.getType().equals(TokenType.END_ARRAY)
                && context != null && context.isArray) {
            event = EventType.END_ARRAY;
            contexts.pop();
        } else if (token.getType().equals(TokenType.BEGIN_OBJECT)) {
            event = EventType.BEGIN_OBJECT;
            if (context != null) {
                context.valuePos++;
            }

            EventContext objectContext = new EventContext();
            objectContext.isObject = true;
            contexts.push(objectContext);
        } else if (token.getType().equals(TokenType.END_OBJECT)
                && context != null && context.isObject) {
            event = EventType.END_OBJECT;
            contexts.pop();
        } else if (token.getType().equals(TokenType.NAME_SEPARATOR)
                && event == EventType.OBJECT_KEY) {
            return next();
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
