package ahodanenok.json.jp;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonWriterFactoryImplTest {

    @Test
    public void testCreateJsonWriterFromWriter() {
        StringWriter writer = new StringWriter();
        try (JsonWriter jsonWriter = new JsonWriterFactoryImpl().createWriter(writer)) {
            jsonWriter.write(JsonValue.TRUE);
        }
        assertEquals("true", writer.toString());
    }

    @Test
    public void testCreateJsonWriterFromOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (JsonWriter jsonWriter = new JsonWriterFactoryImpl().createWriter(out)) {
            jsonWriter.write(new JsonStringImpl("привет"));
        }
        assertArrayEquals(new byte[] {
            (byte) 0x22,
            (byte) 0xd0,
            (byte) 0xbf,
            (byte) 0xd1,
            (byte) 0x80,
            (byte) 0xd0,
            (byte) 0xb8,
            (byte) 0xd0,
            (byte) 0xb2,
            (byte) 0xd0,
            (byte) 0xb5,
            (byte) 0xd1,
            (byte) 0x82,
            (byte) 0x22
        }, out.toByteArray());
    }

    @Test
    public void testCreateJsonWriterFromOutputStreamWithEncoding() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (JsonWriter jsonWriter = new JsonWriterFactoryImpl().createWriter(out, StandardCharsets.US_ASCII)) {
            jsonWriter.write(new JsonStringImpl("hello"));
        }
        assertArrayEquals(new byte[] {
            0x22,
            0x68,
            0x65,
            0x6C,
            0x6C,
            0x6F,
            0x22
        }, out.toByteArray());
    }
}