package ahodanenok.json.writer;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonBoolean;
import ahodanenok.json.value.JsonNull;
import ahodanenok.json.value.JsonNumber;
import ahodanenok.json.value.JsonString;
import ahodanenok.json.value.JsonValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultJsonValueWriterTest {

    @Test
    public void testWriteNull() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonNull();
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals("null", writer.toString());
    }

    @Test
    public void testWriteTrue() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonBoolean(true);
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals("true", writer.toString());
    }

    @Test
    public void testWriteFalse() {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonBoolean(false);
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
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
        JsonValue value = new JsonString(s);
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
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
    public void testWriteNumber(Double n, String expected) {
        StringWriter writer = new StringWriter();
        JsonValue value = new JsonNumber(n);
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteEmptyArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonArray.builder().build();
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals("[]", writer.toString());
    }

    @Test
    public void testWriteOneElementArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonArray.builder().add(new JsonBoolean(true)).build();
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals("[true]", writer.toString());
    }

    @Test
    public void testWriteMultipleElementArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonArray.builder()
            .add(new JsonNull())
            .add(new JsonString("data"))
            .add(new JsonBoolean(true))
            .build();
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals("[null,\"data\",true]", writer.toString());
    }

    @Test
    public void testWriteOneLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonArray.builder()
            .add(new JsonString("test"))
            .add(JsonArray.builder()
                .add(new JsonNumber(3.25))
                .add(new JsonBoolean(false))
                .build())
            .add(new JsonNumber(123))
            .build();
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals("[\"test\",[3.25,false],123.0]", writer.toString());
    }

    @Test
    public void testWriteMultipleLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonValue value = JsonArray.builder()
            .add(new JsonString("123"))
            .add(JsonArray.builder()
                .add(new JsonNumber(200))
                .add(JsonArray.builder()
                    .add(new JsonString("result"))
                    .add(new JsonNull())
                    .build())
                .add(JsonArray.builder().build())
                .add(JsonArray.builder()
                    .add(JsonArray.builder()
                        .add(JsonArray.builder()
                            .add(new JsonBoolean(true))
                            .add(new JsonNumber(-67.89))
                            .build())
                        .build())
                    .build())
                .build())
            .add(new JsonNumber(500))
            .build();
        JsonValueWriter jsonWriter = new DefaultJsonValueWriter();
        jsonWriter.writeValue(value, writer);
        assertEquals("[\"123\",[200.0,[\"result\",null],[],[[[true,-67.89]]]],500.0]", writer.toString());
    }
}
