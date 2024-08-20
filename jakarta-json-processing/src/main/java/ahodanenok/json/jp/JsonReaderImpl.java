package ahodanenok.json.jp;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonConfig;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParsingException;

import ahodanenok.json.parser.DefaultJsonStreamingParser;
import ahodanenok.json.parser.EventType;
import ahodanenok.json.parser.JsonStreamingParser;

final class JsonReaderImpl implements JsonReader {

    private final Reader reader;
    private JsonConfig.KeyStrategy keyStrategy;
    private boolean usable;

    JsonReaderImpl(Reader reader) {
        this.reader = reader;
        this.keyStrategy = JsonConfig.KeyStrategy.LAST;
        this.usable = true;
    }

    void setKeyStrategy(JsonConfig.KeyStrategy keyStrategy) {
        this.keyStrategy = keyStrategy;
    }

    @Override
    public JsonStructure read() {
        if (!usable) {
            throw new IllegalStateException("Reader is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingParser parser = new DefaultJsonStreamingParser(reader);
        parserAdvance(parser, "No content");

        JsonStructure value;
        EventType event = parser.currentEvent();
        if (event == EventType.BEGIN_ARRAY) {
            value = doReadArray(parser);
        } else if (event == EventType.BEGIN_OBJECT) {
            value = doReadObject(parser);
        } else {
            throw new JsonParsingException("todo", new JsonLocationImpl());
        }
        parserAdvance(parser, false, null);

        return value;
    }

    @Override
    public JsonObject readObject() {
        if (!usable) {
            throw new IllegalStateException("Reader is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingParser parser = new DefaultJsonStreamingParser(reader);
        parserAdvance(parser, "No content");

        JsonObject value = doReadObject(parser);
        parserAdvance(parser, false, null);

        return value;
    }

    @Override
    public JsonArray readArray() {
        if (!usable) {
            throw new IllegalStateException("Reader is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingParser parser = new DefaultJsonStreamingParser(reader);
        parserAdvance(parser, "No content");

        JsonArray value = doReadArray(parser);
        parserAdvance(parser, false, null);

        return value;
    }

    @Override
    public JsonValue readValue() {
        if (!usable) {
            throw new IllegalStateException("Reader is closed or has been used to read a value");
        }
        usable = false;

        JsonStreamingParser parser = new DefaultJsonStreamingParser(reader);
        parserAdvance(parser, "No content");

        JsonValue value = doReadValue(parser);
        parserAdvance(parser, false, null);

        return value;
    }

    private JsonValue doReadValue(JsonStreamingParser parser) {
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
            return doReadObject(parser);
        } else if (event == EventType.BEGIN_ARRAY) {
            return doReadArray(parser);
        } else {
            throw new JsonParsingException("todo", new JsonLocationImpl());
        }
    }

    private JsonObject doReadObject(JsonStreamingParser parser) {
        Map<String, JsonValue> values = new LinkedHashMap<>();
        while (true) {
            parserAdvance(parser, "todo");
            if (parser.currentEvent() == EventType.END_OBJECT) {
                break;
            } else if (parser.currentEvent() == EventType.OBJECT_KEY) {
                String name = parser.getString();
                if (keyStrategy == JsonConfig.KeyStrategy.NONE && values.containsKey(name)) {
                    throw new JsonParsingException(
                        String.format("Duplicate key '%s'", name), new JsonLocationImpl());
                }

                parserAdvance(parser, "todo");
                JsonValue value = doReadValue(parser);
                if (keyStrategy == JsonConfig.KeyStrategy.FIRST && values.containsKey(name)) {
                    continue;
                }

                values.put(name, value);
            } else {
                throw new JsonParsingException("todo", new JsonLocationImpl());
            }
        }

        return new JsonObjectImpl(values);
    }

    private JsonArray doReadArray(JsonStreamingParser parser) {
        List<JsonValue> values = new ArrayList<>();
        while (true) {
            parserAdvance(parser, "todo");
            if (parser.currentEvent() == EventType.END_ARRAY) {
                break;
            } else {
                values.add(doReadValue(parser));
            }
        }

        return new JsonArrayImpl(values);
    }

    private void parserAdvance(JsonStreamingParser parser, String errorMsg) {
        parserAdvance(parser, true, errorMsg);
    }

    private void parserAdvance(JsonStreamingParser parser, boolean required, String errorMsg) {
        try {
            if (!parser.next() && required) {
                throw new JsonParsingException(errorMsg, new JsonLocationImpl());
            }
        } catch (ahodanenok.json.parser.JsonParseException e) {
            if (e.getCause() instanceof IOException) {
                throw new JsonException(e.getMessage(), e.getCause());
            } else {
                // todo: use state
                JsonParsingException pe = new JsonParsingException(e.getMessage(), new JsonLocationImpl());
                if (e.getCause() != null) {
                    pe.initCause(e.getCause());
                }

                throw pe;
            }
        }
    }

    @Override
    public void close() {
        usable = false;
        try {
            reader.close();
        } catch (IOException e) {
            throw new JsonException("Failed to close the reader", e);
        }
    }
}
