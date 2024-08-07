package ahodanenok.json.jp;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;

import ahodanenok.json.writer.DefaultJsonOutput;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonWriterImplTest {

    @Test
    public void testWriteNull() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonValue.NULL;
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("null", writer.toString());
    }

    @Test
    public void testWriteTrue() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonValue.TRUE;
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("true", writer.toString());
    }

    @Test
    public void testWriteFalse() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonValue.FALSE;
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("false", writer.toString());
    }

    @ParameterizedTest
    @CsvSource({
        "'',     \"\"",
        "abc,     \"abc\"",
        "'hello world',     \"hello world\""
    })
    public void testWriteString(String s, String expected) {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonStringImpl(s);
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
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
    public void testWriteNumber(double n, String expected) {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonNumberDoubleImpl(n);
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteEmptyArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonArrayImpl(List.of());
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("[]", writer.toString());
    }

    @Test
    public void testWriteOneElementArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonArrayImpl(List.of(JsonValue.TRUE));
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("[true]", writer.toString());
    }

    @Test
    public void testWriteMultipleElementArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonArrayImpl(List.of(
            JsonValue.NULL,
            new JsonStringImpl("data"),
            JsonValue.TRUE
        ));
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("[null,\"data\",true]", writer.toString());
    }

    @Test
    public void testWriteOneLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonArrayImpl(List.of(
            new JsonStringImpl("test"),
            new JsonArrayImpl(List.of(
                new JsonNumberDoubleImpl(3.25),
                JsonValue.FALSE)),
            new JsonNumberDoubleImpl(123)
        ));
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("[\"test\",[3.25,false],123.0]", writer.toString());
    }

    @Test
    public void testWriteMultipleLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonArrayImpl(List.of(
            new JsonStringImpl("123"),
            new JsonArrayImpl(List.of(
                new JsonNumberIntegerImpl(200),
                new JsonArrayImpl(List.of(
                    new JsonStringImpl("result"),
                    JsonValue.NULL)),
                new JsonArrayImpl(List.of()),
                new JsonArrayImpl(List.of(
                    new JsonArrayImpl(List.of(
                        new JsonArrayImpl(List.of(
                            JsonValue.TRUE,
                            new JsonNumberDoubleImpl(-67.89)
                        ))
                    ))
                ))
            )),
            new JsonNumberIntegerImpl(500)
        ));
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("[\"123\",[200.0,[\"result\",null],[],[[[true,-67.89]]]],500.0]", writer.toString());
    }

    @Test
    public void testWriteEmptyObject() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonObjectImpl(Map.of());
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("{}", writer.toString());
    }

    @Test
    public void testWriteOneElementObject() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonObjectImpl(Map.of(
            "result", JsonValue.TRUE
        ));
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("{\"result\":true}", writer.toString());
    }

    @Test
    public void testWriteMultipleElementsObject() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonObjectImpl(new java.util.LinkedHashMap<>() {{
            put("result", JsonValue.TRUE);
            put("num", new JsonNumberIntegerImpl(20923));
            put("data", JsonValue.NULL);
            put("status", new JsonStringImpl("ok"));
        }});
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("{\"result\":true,\"num\":20923.0,\"data\":null,\"status\":\"ok\"}", writer.toString());
    }

    @Test
    public void testWriteNestedObject() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonObjectImpl(new LinkedHashMap<>() {{
            put("data", new JsonObjectImpl(new LinkedHashMap<>() {{
                put("null", JsonValue.NULL);
                put("", new JsonObjectImpl(Map.of()));
                put("result", new JsonObjectImpl(new LinkedHashMap<>() {{
                    put("abc", new JsonObjectImpl(new LinkedHashMap<>() {{
                        put("a", JsonValue.TRUE);
                        put("b", new JsonStringImpl("c"));
                    }}));
                }}));
            }}));
            put("test", new JsonNumberIntegerImpl(321));
            put("response", new JsonObjectImpl(new LinkedHashMap<>() {{
                put("status", new JsonNumberIntegerImpl(200));
                put("message", new JsonStringImpl("OK"));
            }}));
            put("x", new JsonObjectImpl(Map.of(
                "y", new JsonObjectImpl(Map.of(
                    "z", JsonValue.FALSE
                ))
            )));
        }});
        JsonWriter jsonWriter = new JsonWriterImpl(new DefaultJsonOutput(writer));
        jsonWriter.write(value);
        assertEquals("{\"data\":{\"null\":null,\"\":{},\"result\":{\"abc\":{\"a\":true,\"b\":\"c\"}}},\"test\":321.0,\"response\":{\"status\":200.0,\"message\":\"OK\"},\"x\":{\"y\":{\"z\":false}}}", writer.toString());
    }
}
