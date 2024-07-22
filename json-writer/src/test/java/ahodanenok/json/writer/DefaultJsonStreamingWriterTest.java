package ahodanenok.json.writer;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        jsonWriter.writeNumber(123);
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
        assertEquals("[[],\"list\",[true,[[null,[],\"123\"]],null],500.0,[[\"a\",[false],\"b\",\"c\"]]]", writer.toString());
    }
}
