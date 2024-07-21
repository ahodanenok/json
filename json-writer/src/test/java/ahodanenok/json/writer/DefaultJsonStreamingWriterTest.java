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
    public void testWriteFalse(String s, String expected) {
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
    public void testWriteFalse(double n, String expected) {
        StringWriter writer = new StringWriter();
        JsonStreamingWriter jsonWriter = new DefaultJsonStreamingWriter(new DefaultJsonOutput(writer));
        jsonWriter.writeNumber(n);
        assertEquals(expected, writer.toString());
    }
}