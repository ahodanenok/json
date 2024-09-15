package ahodanenok.json.writer;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultJsonStreamingWriterTest {

    @Test
    public void testWriteNull() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeNull();
        assertEquals("null", writer.toString());
    }

    @Test
    public void testWriteTrue() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBoolean(true);
        assertEquals("true", writer.toString());
    }

    @Test
    public void testWriteFalse() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBoolean(false);
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
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeString(s);
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
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeNumber(n);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteEmptyArray() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginArray();
        jsonWriter.writeEnd();
        assertEquals("[]", writer.toString());
    }

    @Test
    public void testWriteOneElementArray() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginArray();
        jsonWriter.writeBoolean(true);
        jsonWriter.writeEnd();
        assertEquals("[true]", writer.toString());
    }

    @Test
    public void testWriteMultipleElementArray() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginArray();
        jsonWriter.writeString("test string");
        jsonWriter.writeNull();
        jsonWriter.writeNumber(321.5678);
        jsonWriter.writeBoolean(false);
        jsonWriter.writeEnd();
        assertEquals("[\"test string\",null,321.5678,false]", writer.toString());
    }

    @Test
    public void testWriteOneLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginArray();
        jsonWriter.writeBoolean(true);
        jsonWriter.writeBeginArray();
        jsonWriter.writeNull();
        jsonWriter.writeNumber(123f);
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        assertEquals("[true,[null,123.0]]", writer.toString());
    }

    @Test
    public void testWriteMultipleLevelNestedtArray() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginArray();
        jsonWriter.writeBeginArray();
        jsonWriter.writeEnd();
        jsonWriter.writeString("list");
        jsonWriter.writeBeginArray();
        jsonWriter.writeBoolean(true);
        jsonWriter.writeBeginArray();
        jsonWriter.writeBeginArray();
        jsonWriter.writeNull();
        jsonWriter.writeBeginArray();
        jsonWriter.writeEnd();
        jsonWriter.writeString("123");
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        jsonWriter.writeNull();
        jsonWriter.writeEnd();
        jsonWriter.writeNumber(500);
        jsonWriter.writeBeginArray();
        jsonWriter.writeBeginArray();
        jsonWriter.writeString("a");
        jsonWriter.writeBeginArray();
        jsonWriter.writeBoolean(false);
        jsonWriter.writeEnd();
        jsonWriter.writeString("b");
        jsonWriter.writeString("c");
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        assertEquals("[[],\"list\",[true,[[null,[],\"123\"]],null],500,[[\"a\",[false],\"b\",\"c\"]]]", writer.toString());
    }

    @Test
    public void testWriteEmptyObject() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginObject();
        jsonWriter.writeEnd();
        assertEquals("{}", writer.toString());
    }

    @Test
    public void testWriteOneElementObject() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("request");
        jsonWriter.writeBoolean(true);
        jsonWriter.writeEnd();
        assertEquals("{\"request\":true}", writer.toString());
    }

    @Test
    public void testWriteMultipleElementObject() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("result");
        jsonWriter.writeBoolean(true);
        jsonWriter.writeName("num");
        jsonWriter.writeNumber(20923f);
        jsonWriter.writeName("data");
        jsonWriter.writeNull();
        jsonWriter.writeName("status");
        jsonWriter.writeString("ok");
        jsonWriter.writeEnd();
        assertEquals("{\"result\":true,\"num\":20923.0,\"data\":null,\"status\":\"ok\"}", writer.toString());
    }

    @Test
    public void testWriteNestedObject() {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("data");
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("null");
        jsonWriter.writeNull();
        jsonWriter.writeName("");
        jsonWriter.writeBeginObject();
        jsonWriter.writeEnd();
        jsonWriter.writeName("result");
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("abc");
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("a");
        jsonWriter.writeBoolean(true);
        jsonWriter.writeName("b");
        jsonWriter.writeString("c");
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        jsonWriter.writeName("test");
        jsonWriter.writeNumber(321f);
        jsonWriter.writeName("response");
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("status");
        jsonWriter.writeNumber(200);
        jsonWriter.writeName("message");
        jsonWriter.writeString("OK");
        jsonWriter.writeEnd();
        jsonWriter.writeName("x");
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("y");
        jsonWriter.writeBeginObject();
        jsonWriter.writeName("z");
        jsonWriter.writeBoolean(false);
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        jsonWriter.writeEnd();
        assertEquals("{\"data\":{\"null\":null,\"\":{},\"result\":{\"abc\":{\"a\":true,\"b\":\"c\"}}},\"test\":321.0,\"response\":{\"status\":200,\"message\":\"OK\"},\"x\":{\"y\":{\"z\":false}}}", writer.toString());
    }

    @Test
    public void testWriteErrorWhenMultipleValuesRoot() {
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(new StringWriter()));
        JsonWriteException e = assertThrows(JsonWriteException.class, () -> {
            jsonWriter.writeBoolean(true);
            jsonWriter.writeString("test");
        });
        assertEquals("There can be only one value at the root", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenNoArrayToEnd() {
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(new StringWriter()));
        JsonWriteException e = assertThrows(JsonWriteException.class, () -> {
            jsonWriter.writeBeginArray();
            jsonWriter.writeEnd();
            jsonWriter.writeEnd();
        });
        assertEquals("There is no open object or array to end", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenNoObjectToEnd() {
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(new StringWriter()));
        JsonWriteException e = assertThrows(JsonWriteException.class, () -> {
            jsonWriter.writeBeginObject();
            jsonWriter.writeEnd();
            jsonWriter.writeEnd();
        });
        assertEquals("There is no open object or array to end", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenTwoNames() {
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(new StringWriter()));
        JsonWriteException e = assertThrows(JsonWriteException.class, () -> {
            jsonWriter.writeBeginObject();
            jsonWriter.writeName("a");
            jsonWriter.writeName("b");
        });
        assertEquals("Can't write two names in a row without a value between them", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenNameNotInObject() {
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(new StringWriter()));
        JsonWriteException e = assertThrows(JsonWriteException.class, () -> {
            jsonWriter.writeBeginArray();
            jsonWriter.writeName("c");
        });
        assertEquals("Must be in 'OBJECT' context, but current context is 'ARRAY'", e.getMessage());
    }

    @Test
    public void testWriteErrorWhenValueWithoutName() {
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(new StringWriter()));
        JsonWriteException e = assertThrows(JsonWriteException.class, () -> {
            jsonWriter.writeBeginObject();
            jsonWriter.writeNumber(1);
        });
        assertEquals("Each object member must begin with a name", e.getMessage());
    }
}
