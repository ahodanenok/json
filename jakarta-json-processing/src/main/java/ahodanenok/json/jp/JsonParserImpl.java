package ahodanenok.json.jp;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonLocation;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParsingException;

import ahodanenok.json.parser.DefaultJsonStreamingParser;
import ahodanenok.json.parser.EventType;
import ahodanenok.json.parser.JsonStreamingParser;

final class JsonParserImpl implements JsonParser {

    private final Reader reader;
    private final JsonStreamingParser parser;
    private int arrayContext;
    private int objectContext;

    JsonParserImpl(Reader reader) {
        this.reader = reader;
        this.parser = new DefaultJsonStreamingParser(reader);
        this.arrayContext = 0;
        this.objectContext = 0;
    }

    @Override
    public boolean hasNext() {
        return parser.hasNext();
    }

    @Override
    public JsonParser.Event next() {
        if (!advance()) {
            throw new NoSuchElementException("No more elements");
        }

        return translateEvent(parser.currentEvent());
    }

    private boolean advance() {
        try {
            boolean advanced = parser.next();
            if (!advanced) {
                return false;
            }

            EventType event = parser.currentEvent();
            if (event == EventType.BEGIN_ARRAY) {
                arrayContext++;
            } else if (event == EventType.END_ARRAY) {
                arrayContext--;
            } else if (event == EventType.BEGIN_OBJECT) {
                objectContext++;
            } else if (event == EventType.END_OBJECT) {
                objectContext--;
            }

            return true;
        } catch (ahodanenok.json.parser.JsonParseException e) {
            if (e.getCause() instanceof IOException) {
                throw new JsonException(e.getMessage(), e.getCause());
            } else {
                throw new JsonParsingException(e.getMessage(), e.getCause(), new JsonLocationImpl());
            }
        }
    }

    @Override
    public JsonParser.Event currentEvent() {
        try {
            return translateEvent(parser.currentEvent());
        } catch (IllegalStateException e) {
            return null;
        }
    }

    private JsonParser.Event translateEvent(EventType eventType) {
        if (eventType == EventType.BEGIN_OBJECT) {
            return JsonParser.Event.START_OBJECT;
        } else if (eventType == EventType.END_OBJECT) {
            return JsonParser.Event.END_OBJECT;
        } else if (eventType == EventType.OBJECT_KEY) {
            return JsonParser.Event.KEY_NAME;
        } else if (eventType == EventType.BEGIN_ARRAY) {
            return JsonParser.Event.START_ARRAY;
        } else if (eventType == EventType.END_ARRAY) {
            return JsonParser.Event.END_ARRAY;
        } else if (eventType == EventType.STRING) {
            return JsonParser.Event.VALUE_STRING;
        } else if (eventType == EventType.NUMBER) {
            return JsonParser.Event.VALUE_NUMBER;
        } else if (eventType == EventType.BOOLEAN) {
            if (parser.getBoolean()) {
                return JsonParser.Event.VALUE_TRUE;
            } else {
                return JsonParser.Event.VALUE_FALSE;
            }
        } else if (eventType == EventType.NULL) {
            return JsonParser.Event.VALUE_NULL;
        } else {
            throw new IllegalStateException("Unknown event type: " + eventType);
        }
    }

    @Override
    public String getString() {
        EventType currentEvent = parser.currentEvent();
        if (currentEvent != EventType.STRING
                 && currentEvent != EventType.NUMBER
                 && currentEvent != EventType.OBJECT_KEY) {
            throw new IllegalStateException(String.format(
                "Current event must be '%s', '%s' or '%s', but was '%s'",
                JsonParser.Event.VALUE_STRING,
                JsonParser.Event.VALUE_NUMBER,
                JsonParser.Event.KEY_NAME,
                currentEvent));
        }

        if (parser.currentEvent() == EventType.NUMBER) {
            return parser.getToken();
        } else {
            return parser.getString();
        }
    }

    @Override
    public boolean isIntegralNumber() {
        return parser.getBigDecimal().scale() <= 0;
    }

    @Override
    public int getInt() {
        return parser.getInt();
    }

    @Override
    public long getLong() {
        return parser.getLong();
    }

    @Override
    public BigDecimal getBigDecimal() {
        return parser.getBigDecimal();
    }

    @Override
    public JsonLocation getLocation() {
        return new JsonLocationImpl();
    }

    @Override
    public JsonObject getObject() {
        if (parser.currentEvent() != EventType.BEGIN_OBJECT) {
            throw new IllegalStateException("Current event is not START_OBJECT");
        }

        return getValue().asJsonObject();
    }

    @Override
    public JsonValue getValue() {
        if (parser.currentEvent() == EventType.END_ARRAY
                || parser.currentEvent() == EventType.END_OBJECT) {
            throw new IllegalStateException("Parser state is END_OBJECT or END_ARRAY");
        }

        return doReadValue();
    }

    private JsonValue doReadValue() {
        EventType event = parser.currentEvent();
        if (event == EventType.STRING) {
            return new JsonStringImpl(parser.getString());
        } else if (event == EventType.NUMBER) {
            return new JsonNumberBigDecimalImpl(parser.getBigDecimal());
        } else if (event == EventType.BOOLEAN) {
            return parser.getBoolean() ? JsonValue.TRUE : JsonValue.FALSE;
        } else if (event == EventType.NULL) {
            return JsonValue.NULL;
        } else if (event == EventType.BEGIN_OBJECT) {
            return doReadObject();
        } else if (event == EventType.BEGIN_ARRAY) {
            return doReadArray();
        } else {
            throw new JsonParsingException(event + "", new JsonLocationImpl());
        }
    }

    private JsonObject doReadObject() {
        Map<String, JsonValue> values = new LinkedHashMap<>();
        while (true) {
            if (!advance()) {
                throw new JsonParsingException("Unpexcted end of an object", new JsonLocationImpl());
            }

            if (parser.currentEvent() == EventType.END_OBJECT) {
                break;
            } else if (parser.currentEvent() == EventType.OBJECT_KEY) {
                String name = parser.getString();
                if (!advance()) {
                    throw new JsonParsingException("Unpexcted end of an object", new JsonLocationImpl());
                }

                JsonValue value = doReadValue();
                values.put(name, value);
            } else {
                throw new JsonParsingException("todo", new JsonLocationImpl());
            }
        }

        return new JsonObjectImpl(values);
    }

    private JsonArray doReadArray() {
        List<JsonValue> values = new ArrayList<>();
        while (true) {
            if (!advance()) {
                throw new JsonParsingException("Unpexcted end of an array", new JsonLocationImpl());
            }

            if (parser.currentEvent() == EventType.END_ARRAY) {
                break;
            } else {
                values.add(doReadValue());
            }
        }

        return new JsonArrayImpl(values);
    }

    @Override
    public JsonArray getArray() {
        if (parser.currentEvent() != EventType.BEGIN_ARRAY) {
            throw new IllegalStateException("Current event is not START_ARRAY");
        }

        return doReadArray();
    }

    @Override
    public Stream<JsonValue> getArrayStream() {
        if (parser.currentEvent() != EventType.BEGIN_ARRAY) {
            throw new IllegalStateException("Current event is not START_ARRAY");
        }

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                new Iterator<>() {

                    boolean moveForward = true;

                    @Override
                    public boolean hasNext() {
                        boolean hasNext = JsonParserImpl.this.hasNext();
                        if (!hasNext) {
                            return false;
                        }

                        advance();
                        moveForward = false;
                        if (parser.currentEvent() == EventType.END_ARRAY) {
                            return false;
                        }

                        return true;
                    }

                    @Override
                    public JsonValue next() {
                        if (moveForward) {
                            if (!advance()) {
                                throw new NoSuchElementException("No more elements");
                            }
                        }
                        moveForward = true;
                        return doReadValue();
                    }
                }, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.NONNULL
            ),
            false);
    }

    @Override
    public Stream<Map.Entry<String, JsonValue>> getObjectStream() {
        if (parser.currentEvent() != EventType.BEGIN_OBJECT) {
            throw new IllegalStateException("Current event is not START_OBJECT");
        }

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                new Iterator<>() {

                    boolean moveForward = true;

                    @Override
                    public boolean hasNext() {
                        boolean hasNext = JsonParserImpl.this.hasNext();
                        if (!hasNext) {
                            return false;
                        }

                        advance();
                        moveForward = false;
                        if (parser.currentEvent() == EventType.END_OBJECT) {
                            return false;
                        }

                        return true;
                    }

                    @Override
                    public Map.Entry<String, JsonValue> next() {
                        if (moveForward) {
                            if (!advance()) {
                                throw new NoSuchElementException("No more elements");
                            }
                        }
                        String name = parser.getString();
                        moveForward = true;
                        advance();
                        return new SimpleEntry<>(name, doReadValue());
                    }
                },
                Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.NONNULL
            ),
            false);
    }

    @Override
    public Stream<JsonValue> getValueStream() {
        if (objectContext > 0 || arrayContext > 0) {
            throw new IllegalStateException("Parser is in an array or object");
        }

        return StreamSupport.stream(
            Spliterators.spliterator(
                new Iterator<>() {

                    @Override
                    public boolean hasNext() {
                        return JsonParserImpl.this.hasNext();
                    }

                    @Override
                    public JsonValue next() {
                        if (!advance()) {
                            throw new NoSuchElementException("No more elements");
                        }

                        return getValue();
                    }
                },
                1,
                Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.NONNULL
            ),
            false);
    }

    @Override
    public void skipArray() {
        while (arrayContext > 0) {
            advance();
        }
    }

    @Override
    public void skipObject() {
        while (objectContext > 0) {
            advance();
        }
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new JsonException("Failed to close parser", e);
        }
    }
}
