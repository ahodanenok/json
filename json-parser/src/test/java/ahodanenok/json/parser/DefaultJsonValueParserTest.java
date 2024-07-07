package ahodanenok.json.parser;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonBoolean;
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
        assertEquals(expected, assertInstanceOf(JsonNumber.class, value).doubleValue());
    }

    @Test
    public void testParseNull() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("null"));
        assertEquals(value.getType(), ValueType.NULL);
        assertInstanceOf(JsonNull.class, value);
    }

    @Test
    public void testParseTrue() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("true"));
        assertEquals(value.getType(), ValueType.BOOLEAN);
        assertEquals(true, assertInstanceOf(JsonBoolean.class, value).getValue());
    }

    @Test
    public void testParseFalse() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("false"));
        assertEquals(value.getType(), ValueType.BOOLEAN);
        assertEquals(false, assertInstanceOf(JsonBoolean.class, value).getValue());
    }

    @Test
    public void testParseEmptyArray() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("[]"));
        assertEquals(value.getType(), ValueType.ARRAY);
        JsonArray array = assertInstanceOf(JsonArray.class, value);
        assertEquals(0, array.size());
    }

    @Test
    public void testParseArrayOneLevel() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("[1.23, true, \"true\", null]"));
        assertEquals(value.getType(), ValueType.ARRAY);
        JsonArray array = assertInstanceOf(JsonArray.class, value);
        assertEquals(4, array.size());
        assertEquals(ValueType.NUMBER, array.getItem(0).getType());
        assertEquals(1.23, assertInstanceOf(JsonNumber.class, array.getItem(0)).doubleValue());
        assertEquals(ValueType.BOOLEAN, array.getItem(1).getType());
        assertEquals(true, assertInstanceOf(JsonBoolean.class, array.getItem(1)).getValue());
        assertEquals(ValueType.STRING, array.getItem(2).getType());
        assertEquals("true", assertInstanceOf(JsonString.class, array.getItem(2)).getValue());
        assertEquals(ValueType.NULL, array.getItem(3).getType());
    }

    @Test
    public void testParseArrayNested() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("""
            [
                false,
                [
                    [500],
                    null
                ],
                [],
                true,
                [
                    "x",
                    [
                        \"yz\",
                        [
                            [
                                [-2.34]
                            ]
                        ]
                    ]
                ]
            ]
        """));
        assertEquals(value.getType(), ValueType.ARRAY);
        JsonArray array = assertInstanceOf(JsonArray.class, value);
        assertEquals(5, array.size());
        assertEquals(ValueType.BOOLEAN, array.getItem(0).getType());
        assertEquals(false, assertInstanceOf(JsonBoolean.class, array.getItem(0)).getValue());
        assertEquals(ValueType.ARRAY, array.getItem(1).getType());
        assertEquals(2, ((JsonArray) array.getItem(1)).size());
        assertEquals(1, ((JsonArray) ((JsonArray) array.getItem(1)).getItem(0)).size());
        assertEquals(ValueType.NUMBER, ((JsonArray) ((JsonArray) array.getItem(1)).getItem(0)).getItem(0).getType());
        assertEquals(500, ((JsonNumber) ((JsonArray) ((JsonArray) array.getItem(1)).getItem(0)).getItem(0)).doubleValue());
        assertEquals(ValueType.NULL, ((JsonArray) array.getItem(1)).getItem(1).getType());
        assertEquals(0, ((JsonArray) array.getItem(2)).size());
        assertEquals(ValueType.BOOLEAN, array.getItem(3).getType());
        assertEquals(true, ((JsonBoolean) array.getItem(3)).getValue());
        assertEquals(ValueType.ARRAY, array.getItem(4).getType());
        assertEquals(2, ((JsonArray) array.getItem(4)).size());
        assertEquals(ValueType.STRING, ((JsonArray) array.getItem(4)).getItem(0).getType());
        assertEquals("x", ((JsonString) ((JsonArray) array.getItem(4)).getItem(0)).getValue());
        assertEquals(ValueType.ARRAY, ((JsonArray) array.getItem(4)).getItem(1).getType());
        assertEquals(2, ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).size());
        assertEquals(ValueType.STRING, ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(0).getType());
        assertEquals("yz", ((JsonString) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(0)).getValue());
        assertEquals(ValueType.ARRAY, ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1).getType());
        assertEquals(1, ((JsonArray) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1)).size());
        assertEquals(ValueType.ARRAY, ((JsonArray) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1)).getItem(0).getType());
        assertEquals(1, ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1)).getItem(0)).size());
        assertEquals(ValueType.ARRAY, ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1)).getItem(0)).getItem(0).getType());
        assertEquals(1, ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1)).getItem(0)).getItem(0)).size());
        assertEquals(ValueType.NUMBER, ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1)).getItem(0)).getItem(0)).getItem(0).getType());
        assertEquals(-2.34, ((JsonNumber) ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) ((JsonArray) array.getItem(4)).getItem(1)).getItem(1)).getItem(0)).getItem(0)).getItem(0)).doubleValue());
    }
}
