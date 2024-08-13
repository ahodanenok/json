package ahodanenok.json.jp;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonPointer;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;

public final class JsonProviderImpl extends JsonProvider {

    private final JsonReaderFactory defaultJsonReaderFactory;
    private final JsonWriterFactory defaultJsonWriterFactory;
    private final JsonBuilderFactory defaultJsonBuilderFactory;

    public JsonProviderImpl() {
        defaultJsonReaderFactory = new JsonReaderFactoryImpl();
        defaultJsonWriterFactory = new JsonWriterFactoryImpl();
        defaultJsonBuilderFactory = new JsonBuilderFactoryImpl();
    }

    @Override
    public JsonParser createParser(Reader reader) {
        return null;
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return null;
    }

    @Override
    public JsonParserFactory createParserFactory(Map<String, ?> config) {
        return null;
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return null;
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return null;
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
        return null;
    }

    @Override
    public JsonReader createReader(Reader reader) {
        return defaultJsonReaderFactory.createReader(reader);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return defaultJsonReaderFactory.createReader(in);
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        return defaultJsonWriterFactory.createWriter(writer);
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        return defaultJsonWriterFactory.createWriter(out);
    }

    @Override
    public JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        return defaultJsonWriterFactory;
    }

    @Override
    public JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        return defaultJsonReaderFactory;
    }

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return defaultJsonBuilderFactory.createObjectBuilder();
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(JsonObject object) {
        return defaultJsonBuilderFactory.createObjectBuilder(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObjectBuilder createObjectBuilder(Map<String, ?> map) {
        return defaultJsonBuilderFactory.createObjectBuilder((Map<String, Object>) map);
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return defaultJsonBuilderFactory.createArrayBuilder();
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(JsonArray array) {
        return defaultJsonBuilderFactory.createArrayBuilder(array);
    }

    @Override
    public JsonPointer createPointer(String jsonPointer) {
        return null;
    }

    @Override
    public JsonPatchBuilder createPatchBuilder() {
        return null;
    }

    @Override
    public JsonPatchBuilder createPatchBuilder(JsonArray array) {
        return null;
    }

    @Override
    public JsonPatch createPatch(JsonArray array) {
        return null;
    }

    @Override
    public JsonPatch createDiff(JsonStructure source, JsonStructure target) {
        return null;
    }

    @Override
    public JsonMergePatch createMergePatch(JsonValue patch) {
        return null;
    }

    @Override
    public JsonMergePatch createMergeDiff(JsonValue source, JsonValue target) {
        return null;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(Collection<?> collection) {
        return defaultJsonBuilderFactory.createArrayBuilder(collection);
    }

    @Override
    public JsonBuilderFactory createBuilderFactory(Map<String, ?> config) {
        return defaultJsonBuilderFactory;
    }

    @Override
    public JsonString createValue(String value) {
        return new JsonStringImpl(value);
    }

    @Override
    public JsonNumber createValue(int value) {
        return new JsonNumberIntegerImpl(value);
    }

    @Override
    public JsonNumber createValue(long value) {
        return new JsonNumberLongImpl(value);
    }

    @Override
    public JsonNumber createValue(double value) {
        return new JsonNumberDoubleImpl(value);
    }

    @Override
    public JsonNumber createValue(BigDecimal value) {
        return new JsonNumberBigDecimalImpl(value);
    }

    @Override
    public JsonNumber createValue(BigInteger value) {
        return new JsonNumberBigIntegerImpl(value);
    }

    @Override
    public JsonNumber createValue(Number number) {
        if (number instanceof Byte) {
            return new JsonNumberIntegerImpl(number.intValue());
        } else if (number instanceof Short) {
            return new JsonNumberIntegerImpl(number.intValue());
        } else {
            return super.createValue(number);
        }
    }
}