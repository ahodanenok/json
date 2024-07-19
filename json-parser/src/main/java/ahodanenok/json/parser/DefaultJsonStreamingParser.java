package ahodanenok.json.parser;

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
        boolean valueSeparatorSeen;
        boolean nameSeparatorSeen;
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
        EventContext context;
        if (contexts.size() > 0) {
            context = contexts.peek();
        } else {
            context = null;
        }

        if (!tokenizer.advance()) {
            if (context != null && context.isArray) {
                throw new JsonParseException("Unexpected end of an array", tokenizer.halt());
            } else if (context != null && context.isObject) {
                throw new JsonParseException("Unexpected end of an object", tokenizer.halt());
            }

            return false;
        }

        JsonToken token = tokenizer.currentToken();
        if (context == null && event != null) {
            throw new JsonParseException(
                String.format("Unexpected token '%s' after the value", token.getRepresentation()),
                tokenizer.halt());
        } else if (token.getType().equals(TokenType.STRING)
                && context != null && context.isObject && event != EventType.OBJECT_KEY) {
            event = EventType.OBJECT_KEY;
        } else if (token.getType().equals(TokenType.STRING)) {
            if (context != null) {
                processContextOnValue(context, token);
            }
            event = EventType.STRING;
        } else if (token.getType().equals(TokenType.NUMBER)) {
            if (context != null) {
                processContextOnValue(context, token);
            }
            event = EventType.NUMBER;
        } else if (token.getType().equals(TokenType.NULL)) {
            if (context != null) {
                processContextOnValue(context, token);
            }

            event = EventType.NULL;
        } else if (token.getType().equals(TokenType.TRUE)) {
            if (context != null) {
                processContextOnValue(context, token);
            }

            event = EventType.BOOLEAN;
        } else if (token.getType().equals(TokenType.FALSE)) {
            if (context != null) {
                processContextOnValue(context, token);
            }

            event = EventType.BOOLEAN;
        } else if (token.getType().equals(TokenType.VALUE_SEPARATOR)
                && context != null && context.valuePos > 0) {
            context.valueSeparatorSeen = true;
            return next();
        } else if (token.getType().equals(TokenType.BEGIN_ARRAY)) {
            if (context != null) {
                processContextOnValue(context, token);
            }
            event = EventType.BEGIN_ARRAY;

            EventContext arrayContext = new EventContext();
            arrayContext.isArray = true;
            contexts.push(arrayContext);
        } else if (token.getType().equals(TokenType.END_ARRAY)
                && context != null && context.isArray) {
            event = EventType.END_ARRAY;
            contexts.pop();
        } else if (token.getType().equals(TokenType.BEGIN_OBJECT)) {
            if (context != null) {
                processContextOnValue(context, token);
            }
            event = EventType.BEGIN_OBJECT;

            EventContext objectContext = new EventContext();
            objectContext.isObject = true;
            contexts.push(objectContext);
        } else if (token.getType().equals(TokenType.END_OBJECT)
                && context != null && context.isObject) {
            event = EventType.END_OBJECT;
            contexts.pop();
        } else if (token.getType().equals(TokenType.NAME_SEPARATOR)
                && event == EventType.OBJECT_KEY) {
            context.nameSeparatorSeen = true;
            return next();
        } else if (token.getType().equals(TokenType.NAME_SEPARATOR)
                && context != null) {
            throw new JsonParseException(
                String.format("Expected ',' but '%s' was encountered", token.getRepresentation()),
                tokenizer.halt());
        } else {
            throw new JsonParseException(
                String.format("Unexpected token '%s'", token.getRepresentation()),
                tokenizer.halt());
        }

        return true;
    }

    private void processContextOnValue(EventContext context, JsonToken token) {
        if (!context.valueSeparatorSeen && context.valuePos > 0) {
            throw new JsonParseException(
                String.format("Expected ',' but '%s' was encountered", token.getRepresentation()),
                tokenizer.halt());
        }

        if (context.isObject && event != EventType.OBJECT_KEY) {
            throw new JsonParseException(
                String.format("Expected name, but '%s' was encountered", token.getRepresentation()),
                tokenizer.halt());
        }

        if (context.isObject && !context.nameSeparatorSeen) {
            throw new JsonParseException(
                String.format("Expected ':', but '%s' was encountered", token.getRepresentation()),
                tokenizer.halt());
        }

        context.valuePos++;
        context.valueSeparatorSeen = false;
        context.nameSeparatorSeen = false;
    }

    @Override
    public EventType currentEvent() {
        if (event == null) {
            throw new IllegalStateException(
                "There is no current event. This method must be called only after `next()` returned true");
        }

        return event;
    }

    @Override
    public String getString() {
        if (event != EventType.STRING && event != EventType.OBJECT_KEY) {
            throw new IllegalStateException(String.format(
                "Current event must be '%s' or '%s', but was '%s'",
                EventType.STRING, EventType.OBJECT_KEY, event));
        }

        return tokenizer.currentToken().stringValue();
    }

    @Override
    public double getDouble() {
        if (event != EventType.NUMBER) {
            throw new IllegalStateException(String.format(
                "Current event must be '%s', but was '%s'",
                EventType.NUMBER, event));
        }

        return tokenizer.currentToken().doubleValue();
    }

    @Override
    public boolean getBoolean() {
        if (event != EventType.BOOLEAN) {
            throw new IllegalStateException(String.format(
                "Current event must be '%s', but was '%s'",
                EventType.BOOLEAN, event));
        }

        TokenType tokenType = tokenizer.currentToken().getType();
        if (tokenType == TokenType.TRUE) {
            return true;
        } else if (tokenType == TokenType.FALSE) {
            return false;
        } else {
            throw new IllegalStateException(String.format(
                "Unexpected token '%' during '%s' event", tokenType, event));
        }
    }

    @Override
    public boolean isNull() {
        return event == EventType.NULL;
    }
}