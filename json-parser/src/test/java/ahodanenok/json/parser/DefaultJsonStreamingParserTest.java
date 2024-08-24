package ahodanenok.json.parser;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultJsonStreamingParserTest {

    @ParameterizedTest
    @CsvSource({
        "\"abc\" true,     true",
        "\"hello world\" \"123\",     \"123\"",
        "300 false,     false",
        "null 20213,      20213",
        "[2] {},     {",
        "{} [],     ["
    })
    public void testErrorWhenMultipleValues(String s, String expected) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> { while (parser.next()); });
        assertEquals(String.format("Unexpected token '%s' after the value", expected), e.getMessage());
    }

    @Test
    public void testParseNoContent() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(""));
        assertFalse(parser.hasNext());
        assertFalse(parser.hasNext());
        assertFalse(parser.next());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseEmptyString() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("\"\""));
        assertTrue(parser.hasNext());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("", parser.getString());
        assertFalse(parser.next());
        assertFalse(parser.hasNext());
    }

    @ParameterizedTest
    @CsvSource({
        "\"abc\",     abc",
        "\"hello world\",     hello world"
    })
    public void testParseString(String s, String expected) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals(expected, parser.getString());
        assertFalse(parser.next());
    }

    @ParameterizedTest
    @CsvSource({
        "0.00,     0.0",
        "-12345.6789,     -12345.6789",
        "37e+3,     37000",
        "-5E-4,     -0.0005"
    })
    public void testParseNumber(String s, double expected) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(expected, parser.getDouble());
        assertFalse(parser.next());
    }

    @Test
    public void testParseNull() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("null"));
        assertTrue(parser.next());
        assertEquals(EventType.NULL, parser.currentEvent());
        assertTrue(parser.isNull());
        assertFalse(parser.next());
    }

    @Test
    public void testParseTrue() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("true"));
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertFalse(parser.next());
    }

    @Test
    public void testParseFalse() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("false"));
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(false, parser.getBoolean());
        assertFalse(parser.next());
    }

    @ParameterizedTest
    @ValueSource(strings = { "[", "[100", "[true,", "[true,{\"a\":2}" })
    public void testErrorUnexpectedEndOfArray(String s) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> { while (parser.next()); });
        assertEquals("Unexpected end of an array", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "---", value = {
        "[1 2 --- 2",
        "[[] true] --- true",
        "[null, 4.00 \"abc\", --- \"abc\"",
        "[3:2:1] --- :"
    })
    public void testErrorUnexpectedTokenInArray(String s, String expected) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> { while (parser.next()); });
        assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    }

    @Test
    public void testParseEmptyArray() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("[]"));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertFalse(parser.hasNext());
        assertFalse(parser.next());
    }

    @Test
    public void testParseArrayOneLevel() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("[1.23, true, \"true\", null]"));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(1.23, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("true", parser.getString());
        assertTrue(parser.hasNext());
        assertTrue(parser.next());
        assertEquals(EventType.NULL, parser.currentEvent());
        assertEquals(true, parser.isNull());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertFalse(parser.hasNext());
        assertFalse(parser.next());
    }

    @Test
    public void testParseArrayNested() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("""
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
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(false, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(500, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NULL, parser.currentEvent());
        assertEquals(true, parser.isNull());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("x", parser.getString());
        assertTrue(parser.hasNext());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("yz", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(-2.34, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertFalse(parser.next());
    }

    @ParameterizedTest
    @ValueSource(strings = { "{", "{\"a\":", "{\"a\":1,", "{\"a\":1,\"b\":true" })
    public void testErrorUnexpectedEndOfObject(String s) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> { while (parser.next()); });
        assertEquals("Unexpected end of an object", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "---", value = {
        "{\"ddd\": 300 : --- :",
        "{\"x\": {} [] --- ["
    })
    public void testErrorUnexpectedValueSeparator(String s, String expected) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> { while (parser.next()); });
        assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    }

    @Test
    public void testErrorExpectedName() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("{\"abc\":false, 521"));
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> { while (parser.next()); });
        assertEquals("Expected name, but '521' was encountered", e.getMessage());
    }

    @Test
    public void testErrorExpectedNameSeparator() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("{\"abc\" false}"));
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> { while (parser.next()); });
        assertEquals("Expected ':', but 'false' was encountered", e.getMessage());
    }

    @Test
    public void testParseEmptyObject() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("{}"));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertFalse(parser.next());
    }

    @Test
    public void testParseObjectWithEmptyName() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("{ \"\": 1 }"));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(1, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertFalse(parser.next());
    }

    @Test
    public void testParseObjectOneLevel() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("""
            {
                "status": "ok",
                "num": -321,
                "items": ["a", "b"],
                "true": false,
                "???": null
            }
            """));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("status", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("ok", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("num", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(-321, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("items", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("a", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("b", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("true", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(false, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("???", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NULL, parser.currentEvent());
        assertTrue(parser.isNull());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertFalse(parser.next());
    }

    @Test
    public void testParseObjectNested() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("""
            {
                "a": {
                    "a1": true,
                    "a2": 4,
                    "a3": {
                        "aaaaaa": -10
                    }
                },
                "b": true,
                "c": {
                    "ac": {
                        "result": 100,
                        "response": {
                            "list": [],
                            "x": {
                                "y": {
                                    "z": "???"
                                }
                            }
                        }
                    },
                    "bc": 300,
                    "cc": {
                        "acc": {
                            "1": [2, 3]
                        },
                        "null": { }
                    }
                },
                "d": {}
            }
            """));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("a", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("a1", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("a2", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(4, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("a3", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("aaaaaa", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(-10, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("b", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("c", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("ac", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("result", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(100, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("response", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("list", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("x", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("y", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("z", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("???", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("bc", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(300, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("cc", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("acc", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("1", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(2, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(3, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("null", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.OBJECT_KEY, parser.currentEvent());
        assertEquals("d", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_OBJECT, parser.currentEvent());
        assertFalse(parser.next());
    }
}
