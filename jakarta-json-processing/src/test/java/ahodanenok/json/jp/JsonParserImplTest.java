package ahodanenok.json.jp;

import java.io.StringReader;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonNumber;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonParserImplTest {

    // @ParameterizedTest
    // @CsvSource({
    //     "\"abc\" true,     true",
    //     "\"hello world\" \"123\",     \"123\"",
    //     "300 false,     false",
    //     "null 20213,      20213",
    //     "[2] {},     {",
    //     "{} [],     ["
    // })
    // public void testErrorWhenMultipleValues(String s, String expected) {
    //     JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
    //     JsonParseException e = assertThrows(
    //         JsonParseException.class, () -> { while (parser.next()); });
    //     assertEquals(String.format("Unexpected token '%s' after the value", expected), e.getMessage());
    // }

    @Test
    public void testParseNoContent() {
        JsonParser parser = new JsonParserImpl(new StringReader(""));
        assertFalse(parser.hasNext());
        assertThrows(NoSuchElementException.class, () -> parser.next());
    }

    @Test
    public void testParseEmptyString() {
        JsonParser parser = new JsonParserImpl(new StringReader("\"\""));
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("", parser.getString());
        assertFalse(parser.hasNext());
        assertThrows(NoSuchElementException.class, () -> parser.next());
    }

    @ParameterizedTest
    @CsvSource({
        "\"abc\",     abc",
        "\"hello world\",     hello world"
    })
    public void testParseString(String s, String expected) {
        JsonParser parser = new JsonParserImpl(new StringReader(s));
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals(expected, parser.getString());
        assertThrows(NoSuchElementException.class, () -> parser.next());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0.00",
        "-12345.6789",
        "37e+3",
        "-5E-4"
    })
    public void testParseNumber(String s) {
        JsonParser parser = new JsonParserImpl(new StringReader(s));
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(Double.parseDouble(s), parser.getBigDecimal().doubleValue());
        assertEquals(s, parser.getString());
        assertThrows(NoSuchElementException.class, () -> parser.next());
    }

    @Test
    public void testParseNull() {
        JsonParser parser = new JsonParserImpl(new StringReader("null"));
        assertTrue(parser.hasNext());
        assertEquals(null, parser.currentEvent());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.next());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.currentEvent());
        assertThrows(NoSuchElementException.class, () -> parser.next());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseTrue() {
        JsonParser parser = new JsonParserImpl(new StringReader("true"));
        assertEquals(null, parser.currentEvent());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.next());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.currentEvent());
        assertThrows(NoSuchElementException.class, () -> parser.next());
        assertThrows(NoSuchElementException.class, () -> parser.next());
    }

    @Test
    public void testParseFalse() {
        JsonParser parser = new JsonParserImpl(new StringReader("false"));
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_FALSE, parser.next());
        assertEquals(JsonParser.Event.VALUE_FALSE, parser.currentEvent());
        assertFalse(parser.hasNext());
        assertFalse(parser.hasNext());
    }

    // @ParameterizedTest
    // @ValueSource(strings = { "[", "[100", "[true,", "[true,{\"a\":2}" })
    // public void testErrorUnexpectedEndOfArray(String s) {
    //     JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
    //     JsonParseException e = assertThrows(
    //         JsonParseException.class, () -> { while (parser.next()); });
    //     assertEquals("Unexpected end of an array", e.getMessage());
    // }

    // @ParameterizedTest
    // @CsvSource(delimiterString = "---", value = {
    //     "[1 2 --- 2",
    //     "[[] true] --- true",
    //     "[null, 4.00 \"abc\", --- \"abc\"",
    //     "[3:2:1] --- :"
    // })
    // public void testErrorUnexpectedTokenInArray(String s, String expected) {
    //     JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
    //     JsonParseException e = assertThrows(
    //         JsonParseException.class, () -> { while (parser.next()); });
    //     assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    // }

    @Test
    public void testParseEmptyArray() {
        JsonParser parser = new JsonParserImpl(new StringReader("[]"));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseArrayOneLevel() {
        JsonParser parser = new JsonParserImpl(new StringReader("[1.23, true, \"true\", null]"));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(1.23, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.next());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.currentEvent());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("true", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.next());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseArrayNested() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
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
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_FALSE, parser.next());
        assertEquals(JsonParser.Event.VALUE_FALSE, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(500, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.next());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.next());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("x", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("yz", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(-2.34, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertFalse(parser.hasNext());
    }

    // @ParameterizedTest
    // @ValueSource(strings = { "{", "{\"a\":", "{\"a\":1,", "{\"a\":1,\"b\":true" })
    // public void testErrorUnexpectedEndOfObject(String s) {
    //     JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
    //     JsonParseException e = assertThrows(
    //         JsonParseException.class, () -> { while (parser.next()); });
    //     assertEquals("Unexpected end of an object", e.getMessage());
    // }

    // @ParameterizedTest
    // @CsvSource(delimiterString = "---", value = {
    //     "{\"ddd\": 300 : --- :",
    //     "{\"x\": {} [] --- ["
    // })
    // public void testErrorUnexpectedValueSeparator(String s, String expected) {
    //     JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
    //     JsonParseException e = assertThrows(
    //         JsonParseException.class, () -> { while (parser.next()); });
    //     assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    // }

    // @Test
    // public void testErrorExpectedName() {
    //     JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("{\"abc\":false, 521"));
    //     JsonParseException e = assertThrows(
    //         JsonParseException.class, () -> { while (parser.next()); });
    //     assertEquals("Expected name, but '521' was encountered", e.getMessage());
    // }

    // @Test
    // public void testErrorExpectedNameSeparator() {
    //     JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("{\"abc\" false}"));
    //     JsonParseException e = assertThrows(
    //         JsonParseException.class, () -> { while (parser.next()); });
    //     assertEquals("Expected ':', but 'false' was encountered", e.getMessage());
    // }

    @Test
    public void testParseEmptyObject() {
        JsonParser parser = new JsonParserImpl(new StringReader("{}"));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseObjectWithEmptyName() {
        JsonParser parser = new JsonParserImpl(new StringReader("{ \"\": 1 }"));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(1, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseObjectOneLevel() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            {
                "status": "ok",
                "num": -321,
                "items": ["a", "b"],
                "true": false,
                "???": null
            }
            """));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("status", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("ok", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("num", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(-321, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("items", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("a", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("b", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("true", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_FALSE, parser.next());
        assertEquals(JsonParser.Event.VALUE_FALSE, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("???", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.next());
        assertEquals(JsonParser.Event.VALUE_NULL, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseObjectNested() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
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
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("a", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("a1", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.next());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("a2", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(4, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("a3", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("aaaaaa", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(-10, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("b", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.next());
        assertEquals(JsonParser.Event.VALUE_TRUE, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("c", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("ac", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("result", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(100, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("response", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("list", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("x", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("y", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("z", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.currentEvent());
        assertEquals("???", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("bc", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(300, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("cc", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("acc", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("1", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(2, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(3, parser.getBigDecimal().doubleValue());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("null", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.currentEvent());
        assertEquals("d", parser.getString());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonValue() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            [123, true]
        """));
        parser.next();
        JsonValue value = parser.getValue();
        assertEquals(2, value.asJsonArray().size());
        assertEquals(123, value.asJsonArray().getInt(0));
        assertEquals(true, value.asJsonArray().getBoolean(1));
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonValueStream() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            [123, true]
        """));
        // parser.next();
        Stream<JsonValue> stream = parser.getValueStream();
        assertTrue(parser.hasNext());
        JsonValue[] values = stream.toArray(JsonValue[]::new);
        assertEquals(1, values.length);
        assertEquals(2, values[0].asJsonArray().size());
        assertEquals(123, values[0].asJsonArray().getInt(0));
        assertEquals(true, values[0].asJsonArray().getBoolean(1));
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonArray() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            {\"a\": [1, 2, 3] }
        """));
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        JsonArray array = parser.getArray();
        assertEquals(3, array.size());
        assertEquals(1, array.getInt(0));
        assertEquals(2, array.getInt(1));
        assertEquals(3, array.getInt(2));
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonArrayStream() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            {\"a\": [1, 2, 3, [true], {\"x\":false},\"abc\"] }
        """));
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        Stream<JsonValue> stream = parser.getArrayStream();
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        JsonValue[] values = stream.toArray(JsonValue[]::new);
        assertEquals(6, values.length);
        assertEquals(1, ((JsonNumber) values[0]).intValue());
        assertEquals(2, ((JsonNumber) values[1]).intValue());
        assertEquals(3, ((JsonNumber) values[2]).intValue());
        assertEquals(1, values[3].asJsonArray().size());
        assertEquals(true, values[3].asJsonArray().getBoolean(0));
        assertEquals(1, values[4].asJsonObject().size());
        assertEquals(false, values[4].asJsonObject().getBoolean("x"));
        assertEquals("abc", ((JsonString) values[5]).getString());
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonArrayStreamSkipArray() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            {\"a\": [1, 2, 3, [true], {\"x\":false},\"abc\"] }
        """));
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        Stream<JsonValue> stream = parser.getArrayStream();
        assertEquals(JsonParser.Event.START_ARRAY, parser.currentEvent());
        JsonValue[] values = stream.limit(2).toArray(JsonValue[]::new);
        assertEquals(2, values.length);
        assertEquals(1, ((JsonNumber) values[0]).intValue());
        assertEquals(2, ((JsonNumber) values[1]).intValue());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(2, parser.getInt());
        parser.skipArray();
        assertEquals(JsonParser.Event.END_ARRAY, parser.currentEvent());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonObject() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            [{\"a\":11,\"b\":12,\"c\":{\"cc\":13},\"d\":14}, 100]
        """));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        JsonObject object = parser.getObject();
        assertEquals(4, object.size());
        assertEquals(11, object.getInt("a"));
        assertEquals(12, object.getInt("b"));
        assertEquals(1, object.getJsonObject("c").size());
        assertEquals(13, object.getJsonObject("c").getInt("cc"));
        assertEquals(14, object.getInt("d"));
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(100, parser.getInt());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonObjectStream() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            [{\"a\":11,\"b\":12,\"c\":{\"cc\":13},\"d\":14}, 100]
        """));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        Stream<Map.Entry<String, JsonValue>> stream = parser.getObjectStream();
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        @SuppressWarnings("unchecked")
        Map.Entry<String, JsonValue>[] values = stream
            .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
            .toArray(Map.Entry[]::new);
        assertEquals(4, values.length);
        assertEquals("a", values[0].getKey());
        assertEquals(11, ((JsonNumber) values[0].getValue()).intValue());
        assertEquals("b", values[1].getKey());
        assertEquals(12, ((JsonNumber) values[1].getValue()).intValue());
        assertEquals(1, values[2].getValue().asJsonObject().size());
        assertEquals(13, values[2].getValue().asJsonObject().getInt("cc"));
        assertEquals("d", values[3].getKey());
        assertEquals(14, ((JsonNumber) values[3].getValue()).intValue());
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(100, parser.getInt());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseJsonObjectStreamSkipObject() {
        JsonParser parser = new JsonParserImpl(new StringReader("""
            [{\"a\":11,\"b\":12,\"c\":{\"cc\":13},\"d\":14}, 100]
        """));
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        Stream<Map.Entry<String, JsonValue>> stream = parser.getObjectStream();
        assertEquals(JsonParser.Event.START_OBJECT, parser.currentEvent());
        @SuppressWarnings("unchecked")
        Map.Entry<String, JsonValue>[] values = stream
            .limit(2)
            .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
            .toArray(Map.Entry[]::new);
        assertEquals(2, values.length);
        assertEquals("a", values[0].getKey());
        assertEquals(11, ((JsonNumber) values[0].getValue()).intValue());
        assertEquals("b", values[1].getKey());
        assertEquals(12, ((JsonNumber) values[1].getValue()).intValue());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.currentEvent());
        assertEquals(12, parser.getInt());
        parser.skipObject();
        assertEquals(JsonParser.Event.END_OBJECT, parser.currentEvent());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(100, parser.getInt());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertFalse(parser.hasNext());
    }
}
