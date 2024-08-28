package ahodanenok.json.jp;

import java.io.StringWriter;
import java.util.List;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerationException;
import jakarta.json.stream.JsonGenerator;

import ahodanenok.json.writer.DefaultJsonOutput;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteNull() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeNull();
        assertEquals("null", writer.toString());
    }

    @Test
    public void testWriteTrue() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.write(true);
        assertEquals("true", writer.toString());
    }

    @Test
    public void testWriteFalse() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.write(false);
        assertEquals("false", writer.toString());
    }

    @ParameterizedTest
    @CsvSource({
        "'',     \"\"",
        "abc,     \"abc\"",
        "'hello world',     \"hello world\""
    })
    public void testwrite(String s, String expected) {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.write(s);
        assertEquals(expected, writer.toString());
    }

    @ParameterizedTest
    @CsvSource({
        "0,     0.0",
        "1,    1.0",
        "-1,     -1.0",
        "12.34,    12.34",
        "-523.7812,     -523.7812"
    })
    public void testwrite(double n, String expected) {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.write(n);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteEmptyArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartArray();
        generator.writeEnd();
        assertEquals("[]", writer.toString());
    }

    @Test
    public void testWriteOneElementArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartArray();
        generator.write(true);
        generator.writeEnd();
        assertEquals("[true]", writer.toString());
    }

    @Test
    public void testWriteMultipleElementArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartArray();
        generator.write("test string");
        generator.writeNull();
        generator.write(321.5678);
        generator.write(false);
        generator.writeEnd();
        assertEquals("[\"test string\",null,321.5678,false]", writer.toString());
    }

    @Test
    public void testWriteOneLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartArray();
        generator.write(true);
        generator.writeStartArray();
        generator.writeNull();
        generator.write(123f);
        generator.writeEnd();
        generator.writeEnd();
        assertEquals("[true,[null,123.0]]", writer.toString());
    }

    @Test
    public void testWriteMultipleLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartArray();
        generator.writeStartArray();
        generator.writeEnd();
        generator.write("list");
        generator.writeStartArray();
        generator.write(true);
        generator.writeStartArray();
        generator.writeStartArray();
        generator.writeNull();
        generator.writeStartArray();
        generator.writeEnd();
        generator.write("123");
        generator.writeEnd();
        generator.writeEnd();
        generator.writeNull();
        generator.writeEnd();
        generator.write(500);
        generator.writeStartArray();
        generator.writeStartArray();
        generator.write("a");
        generator.writeStartArray();
        generator.write(false);
        generator.writeEnd();
        generator.write("b");
        generator.write("c");
        generator.writeEnd();
        generator.writeEnd();
        generator.writeEnd();
        assertEquals("[[],\"list\",[true,[[null,[],\"123\"]],null],500,[[\"a\",[false],\"b\",\"c\"]]]", writer.toString());
    }

    @Test
    public void testWriteEmptyObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartObject();
        generator.writeEnd();
        assertEquals("{}", writer.toString());
    }

    @Test
    public void testWriteOneElementObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartObject();
        generator.writeKey("request");
        generator.write(true);
        generator.writeEnd();
        assertEquals("{\"request\":true}", writer.toString());
    }

    @Test
    public void testWriteMultipleElementObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartObject();
        generator.writeKey("result");
        generator.write(true);
        generator.write("num", 20923f);
        generator.writeNull("data");
        generator.writeKey("status");
        generator.write("ok");
        generator.writeEnd();
        assertEquals("{\"result\":true,\"num\":20923.0,\"data\":null,\"status\":\"ok\"}", writer.toString());
    }

    @Test
    public void testWriteNestedObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartObject();
        generator.writeKey("data");
        generator.writeStartObject();
        generator.writeKey("null");
        generator.writeNull();
        generator.writeKey("");
        generator.writeStartObject();
        generator.writeEnd();
        generator.writeKey("result");
        generator.writeStartObject();
        generator.writeKey("abc");
        generator.writeStartObject();
        generator.writeKey("a");
        generator.write(true);
        generator.writeKey("b");
        generator.write("c");
        generator.writeEnd();
        generator.writeEnd();
        generator.writeEnd();
        generator.writeKey("test");
        generator.write(321f);
        generator.writeKey("response");
        generator.writeStartObject();
        generator.writeKey("status");
        generator.write(200);
        generator.writeKey("message");
        generator.write("OK");
        generator.writeEnd();
        generator.writeKey("x");
        generator.writeStartObject();
        generator.writeKey("y");
        generator.writeStartObject();
        generator.writeKey("z");
        generator.write(false);
        generator.writeEnd();
        generator.writeEnd();
        generator.writeEnd();
        assertEquals("{\"data\":{\"null\":null,\"\":{},\"result\":{\"abc\":{\"a\":true,\"b\":\"c\"}}},\"test\":321.0,\"response\":{\"status\":200,\"message\":\"OK\"},\"x\":{\"y\":{\"z\":false}}}", writer.toString());
    }

    @Test
    public void testWriteJsonValue() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(writer));
        generator.writeStartArray();
        generator.write(new JsonObjectImpl(new LinkedHashMap<>() {{
            put("a", new JsonNumberDoubleImpl(1.0));
            put("b", new JsonArrayImpl(List.of(JsonValue.TRUE, new JsonNumberIntegerImpl(234))));
            put("c", new JsonObjectImpl(new LinkedHashMap<>() {{
                put("cc1", JsonValue.FALSE);
                put("cc2", new JsonStringImpl("test string"));
            }}));
            put("d", JsonValue.NULL);
        }}));
        generator.writeEnd();
        generator.close();
        assertEquals("[{\"a\":1.0,\"b\":[true,234],\"c\":{\"cc1\":false,\"cc2\":\"test string\"},\"d\":null}]", writer.toString());
    }

    @Test
    public void testWriteErrorWhenMultipleValuesRoot() {
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(new StringWriter()));
        JsonGenerationException e = assertThrows(JsonGenerationException.class, () -> {
            generator.write(true);
            generator.write("test");
        });
        assertEquals("There can be only one value at the root", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenNoArrayToEnd() {
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(new StringWriter()));
        JsonGenerationException e = assertThrows(JsonGenerationException.class, () -> {
            generator.writeStartArray();
            generator.writeEnd();
            generator.writeEnd();
        });
        assertEquals("There is no open object or array to end", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenNoObjectToEnd() {
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(new StringWriter()));
        JsonGenerationException e = assertThrows(JsonGenerationException.class, () -> {
            generator.writeStartObject();
            generator.writeEnd();
            generator.writeEnd();
        });
        assertEquals("There is no open object or array to end", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenTwoNames() {
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(new StringWriter()));
        JsonGenerationException e = assertThrows(JsonGenerationException.class, () -> {
            generator.writeStartObject();
            generator.writeKey("a");
            generator.writeKey("b");
        });
        assertEquals("Can't write two names in a row without a value between them", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenNameNotInObject() {
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(new StringWriter()));
        JsonGenerationException e = assertThrows(JsonGenerationException.class, () -> {
            generator.writeStartArray();
            generator.writeKey("c");
        });
        assertEquals("Must be in an object context, but current is array", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenValueWithoutName() {
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(new StringWriter()));
        JsonGenerationException e = assertThrows(JsonGenerationException.class, () -> {
            generator.writeStartObject();
            generator.write(1);
        });
        assertEquals("Each object member must begin with a name", e.getMessage());
    }

    @Test
    public void testErrorWhenCloseOnUnfinishedJson() {
        JsonGenerator generator = new JsonGeneratorImpl(new DefaultJsonOutput(new StringWriter()));
        JsonGenerationException e = assertThrows(JsonGenerationException.class, () -> {
            generator.writeStartObject();
            generator.write("a", 100);
            generator.close();
        });
        assertEquals("Incomplete json", e.getMessage());
    }
}
