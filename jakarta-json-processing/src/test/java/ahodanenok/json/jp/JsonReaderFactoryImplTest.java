package ahodanenok.json.jp;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import jakarta.json.JsonNumber;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonReaderFactoryImplTest {

    @Test
    public void testCreateFromReader() {
        JsonReader reader = new JsonReaderFactoryImpl().createReader(new StringReader("123"));
        assertEquals(123, ((JsonNumber) reader.readValue()).intValue());
    }

    @Test
    public void testCreateFromInputStreamWithDefaultEncoding() throws Exception {
        byte[] data = {
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
        };
        JsonReader reader = new JsonReaderFactoryImpl().createReader(new ByteArrayInputStream(data));
        assertEquals("привет", ((JsonString) reader.readValue()).getString());
    }

    @Test
    public void testCreateFromInputStreamWithEncoding() throws Exception {
        byte[] data = {
            0x22,
            0x68,
            0x65,
            0x6C,
            0x6C,
            0x6F,
            0x22
        };
        JsonReader reader = new JsonReaderFactoryImpl().createReader(
            new ByteArrayInputStream(data), StandardCharsets.US_ASCII);
        assertEquals("hello", ((JsonString) reader.readValue()).getString());
    }
}
