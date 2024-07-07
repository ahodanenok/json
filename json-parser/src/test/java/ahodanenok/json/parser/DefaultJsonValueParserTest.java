package ahodanenok.json.parser;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ahodanenok.json.value.JsonNull;
import ahodanenok.json.value.JsonNumber;
import ahodanenok.json.value.JsonString;
import ahodanenok.json.value.JsonValue;
import ahodanenok.json.value.ValueType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefaultJsonValueParserTest {

    @Test
    public void testParseNoContent() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader(""));
        assertNull(value);
    }

    @Test
    public void testParseEmptyString() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("\"\""));
        assertEquals(value.getType(), ValueType.STRING);
        assertEquals("", assertInstanceOf(JsonString.class, value).getValue());
    }

    @ParameterizedTest
    @CsvSource({
        "\"abc\",     abc",
        "\"hello world\",     hello world"
    })
    public void testParseString(String s, String expected) {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader(s));
        assertEquals(value.getType(), ValueType.STRING);
        assertEquals(expected, assertInstanceOf(JsonString.class, value).getValue());
    }

    @ParameterizedTest
    @CsvSource({
        "0.00,     0.0",
        "-12345.6789,     -12345.6789",
        "37e+3,     37000",
        "-5E-4,     -0.0005"
    })
    public void testParseNumber(String s, double expected) {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader(s));
        assertEquals(value.getType(), ValueType.NUMBER);
        assertEquals(expected, assertInstanceOf(JsonNumber.class, value).getValue());
    }

    @Test
    public void testParseNull() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("null"));
        assertEquals(value.getType(), ValueType.NULL);
        assertInstanceOf(JsonNull.class, value);
    }
}
