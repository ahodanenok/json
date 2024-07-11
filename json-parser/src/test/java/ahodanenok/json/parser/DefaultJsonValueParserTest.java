package ahodanenok.json.parser;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import ahodanenok.json.value.JsonArray;
import ahodanenok.json.value.JsonObject;
import ahodanenok.json.value.JsonValue;
import ahodanenok.json.value.ValueType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultJsonValueParserTest {

    @Test
    public void testErrorWhenMultipleValues() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> parser.readValue(new StringReader("300 true")));
        assertEquals("Unexpected token 'true' after the value", e.getMessage());
    }

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
        assertEquals("", value.asString().getValue());
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
        assertEquals(expected, value.asString().getValue());
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
        assertEquals(expected, value.asNumber().doubleValue());
    }

    @Test
    public void testParseNull() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("null"));
        assertEquals(value.getType(), ValueType.NULL);
        assertTrue(value.isNull());
    }

    @Test
    public void testParseTrue() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("true"));
        assertEquals(value.getType(), ValueType.BOOLEAN);
        assertEquals(true, value.asBoolean().getValue());
    }

    @Test
    public void testParseFalse() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("false"));
        assertEquals(value.getType(), ValueType.BOOLEAN);
        assertEquals(false, value.asBoolean().getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = { "[", "[100", "[true,", "[true,{\"a\":2}" })
    public void testErrorUnexpectedEndOfArray(String s) {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> parser.readValue(new StringReader(s)));
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
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> parser.readValue(new StringReader(s)));
        assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    }

    @Test
    public void testParseEmptyArray() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("[]"));
        assertEquals(value.getType(), ValueType.ARRAY);
        assertEquals(0, value.asArray().size());
    }

    @Test
    public void testParseArrayOneLevel() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("[1.23, true, \"true\", null]"));
        assertEquals(value.getType(), ValueType.ARRAY);
        JsonArray array = assertInstanceOf(JsonArray.class, value);
        assertEquals(4, array.size());
        assertEquals(ValueType.NUMBER, array.getItem(0).getType());
        assertEquals(1.23, array.getItem(0).asNumber().doubleValue());
        assertEquals(ValueType.BOOLEAN, array.getItem(1).getType());
        assertEquals(true, array.getItem(1).asBoolean().getValue());
        assertEquals(ValueType.STRING, array.getItem(2).getType());
        assertEquals("true", array.getItem(2).asString().getValue());
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
        JsonArray array = value.asArray();
        assertEquals(5, array.size());
        assertEquals(ValueType.BOOLEAN, array.getItem(0).getType());
        assertEquals(false, array.getItem(0).asBoolean().getValue());
        assertEquals(ValueType.ARRAY, array.getItem(1).getType());
        assertEquals(2, array.getItem(1).asArray().size());
        assertEquals(1, array.getItem(1).asArray().getItem(0).asArray().size());
        assertEquals(ValueType.NUMBER, array.getItem(1).asArray().getItem(0).asArray().getItem(0).getType());
        assertEquals(500, array.getItem(1).asArray().getItem(0).asArray().getItem(0).asNumber().doubleValue());
        assertEquals(ValueType.NULL,  array.getItem(1).asArray().getItem(1).getType());
        assertEquals(0, array.getItem(2).asArray().size());
        assertEquals(ValueType.BOOLEAN, array.getItem(3).getType());
        assertEquals(true, array.getItem(3).asBoolean().getValue());
        assertEquals(ValueType.ARRAY, array.getItem(4).getType());
        assertEquals(2, array.getItem(4).asArray().size());
        assertEquals(ValueType.STRING, array.getItem(4).asArray().getItem(0).getType());
        assertEquals("x", array.getItem(4).asArray().getItem(0).asString().getValue());
        assertEquals(ValueType.ARRAY, array.getItem(4).asArray().getItem(1).getType());
        assertEquals(2, array.getItem(4).asArray().getItem(1).asArray().size());
        assertEquals(ValueType.STRING, array.getItem(4).asArray().getItem(1).asArray().getItem(0).getType());
        assertEquals("yz", array.getItem(4).asArray().getItem(1).asArray().getItem(0).asString().getValue());
        assertEquals(ValueType.ARRAY, array.getItem(4).asArray().getItem(1).asArray().getItem(1).getType());
        assertEquals(1, array.getItem(4).asArray().getItem(1).asArray().getItem(1).asArray().size());
        assertEquals(ValueType.ARRAY, array.getItem(4).asArray().getItem(1).asArray().getItem(1).asArray().getItem(0).getType());
        assertEquals(1, array.getItem(4).asArray().getItem(1).asArray().getItem(1).asArray().getItem(0).asArray().size());
        assertEquals(ValueType.ARRAY, array.getItem(4).asArray().getItem(1).asArray().getItem(1).asArray().getItem(0).asArray().getItem(0).getType());
        assertEquals(1, array.getItem(4).asArray().getItem(1).asArray().getItem(1).asArray().getItem(0).asArray().getItem(0).asArray().size());
        assertEquals(ValueType.NUMBER, array.getItem(4).asArray().getItem(1).asArray().getItem(1).asArray().getItem(0).asArray().getItem(0).asArray().getItem(0).getType());
        assertEquals(-2.34, array.getItem(4).asArray().getItem(1).asArray().getItem(1).asArray().getItem(0).asArray().getItem(0).asArray().getItem(0).asNumber().doubleValue());
    }

    @ParameterizedTest
    @ValueSource(strings = { "{", "{\"a\":", "{\"a\":1,", "{\"a\":1,\"b\":true" })
    public void testErrorUnexpectedEndOfObject(String s) {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> parser.readValue(new StringReader(s)));
        assertEquals("Unexpected end of an object", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "---", value = {
        "{\"ddd\": 300 : --- :",
        "{\"x\": {} [] --- ["
    })
    public void testErrorExpectedValueSeparator(String s, String expected) {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> parser.readValue(new StringReader(s)));
        assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    }

    @Test
    public void testErrorExpectedName() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> parser.readValue(new StringReader("{\"abc\":false, 521")));
        assertEquals("Expected name, but '521' was encountered", e.getMessage());
    }

    @Test
    public void testErrorExpectedNameSeparator() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonParseException e = assertThrows(
            JsonParseException.class, () -> parser.readValue(new StringReader("{\"abc\" false}")));
        assertEquals("Expected ':', but 'false' was encountered", e.getMessage());
    }

    @Test
    public void testParseEmptyObject() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("{}"));
        assertEquals(value.getType(), ValueType.OBJECT);
        JsonObject obj = assertInstanceOf(JsonObject.class, value);
        assertEquals(0, obj.size());
    }

    @Test
    public void testParseObjectWithEmptyName() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("{ \"\": 1 }"));
        assertEquals(value.getType(), ValueType.OBJECT);
        JsonObject obj = assertInstanceOf(JsonObject.class, value);
        assertEquals(1, obj.size());
        assertTrue(obj.containsValue(""));
        assertEquals(ValueType.NUMBER, obj.getValue("").getType());
        assertEquals(1, obj.getValue("").asNumber().doubleValue());
    }

    @Test
    public void testParseObjectOneLevel() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("""
            {
                "status": "ok",
                "num": -321,
                "items": ["a", "b"],
                "true": false,
                "???": null
            }
            """));
        assertEquals(value.getType(), ValueType.OBJECT);
        JsonObject obj = assertInstanceOf(JsonObject.class, value);
        assertEquals(5, obj.size());
        assertTrue(obj.containsValue("status"));
        assertEquals(ValueType.STRING, obj.getValue("status").getType());
        assertEquals("ok", obj.getValue("status").asString().getValue());
        assertTrue(obj.containsValue("num"));
        assertEquals(ValueType.NUMBER, obj.getValue("num").getType());
        assertEquals(-321, obj.getValue("num").asNumber().doubleValue());
        assertTrue(obj.containsValue("items"));
        assertEquals(ValueType.ARRAY, obj.getValue("items").getType());
        assertEquals(2, obj.getValue("items").asArray().size());
        assertEquals(ValueType.STRING, obj.getValue("items").asArray().getItem(0).getType());
        assertEquals("a", obj.getValue("items").asArray().getItem(0).asString().getValue());
        assertEquals(ValueType.STRING, obj.getValue("items").asArray().getItem(1).getType());
        assertEquals("b", obj.getValue("items").asArray().getItem(1).asString().getValue());
        assertTrue(obj.containsValue("true"));
        assertEquals(ValueType.BOOLEAN, obj.getValue("true").getType());
        assertEquals(false, obj.getValue("true").asBoolean().getValue());
        assertTrue(obj.containsValue("???"));
        assertEquals(ValueType.NULL, obj.getValue("???").getType());
    }

    @Test
    public void testParseObjectNested() {
        JsonValueParser parser = new DefaultJsonValueParser();
        JsonValue value = parser.readValue(new StringReader("""
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
        assertEquals(value.getType(), ValueType.OBJECT);
        JsonObject obj = assertInstanceOf(JsonObject.class, value);
        assertEquals(4, obj.size());
        assertTrue(obj.containsValue("a"));
        assertEquals(ValueType.OBJECT, obj.getValue("a").getType());
        assertEquals(3, obj.getValue("a").asObject().size());
        assertTrue(obj.getValue("a").asObject().containsValue("a1"));
        assertEquals(ValueType.BOOLEAN, obj.getValue("a").asObject().getValue("a1").getType());
        assertEquals(true, obj.getValue("a").asObject().getValue("a1").asBoolean().getValue());
        assertTrue(obj.getValue("a").asObject().containsValue("a2"));
        assertEquals(ValueType.NUMBER, obj.getValue("a").asObject().getValue("a2").getType());
        assertEquals(4, obj.getValue("a").asObject().getValue("a2").asNumber().doubleValue());
        assertTrue(obj.getValue("a").asObject().containsValue("a3"));
        assertEquals(ValueType.OBJECT, obj.getValue("a").asObject().getValue("a3").getType());
        assertEquals(1, obj.getValue("a").asObject().getValue("a3").asObject().size());
        assertTrue(obj.getValue("a").asObject().getValue("a3").asObject().containsValue("aaaaaa"));
        assertEquals(ValueType.NUMBER, obj.getValue("a").asObject().getValue("a3").asObject().getValue("aaaaaa").getType());
        assertEquals(-10, obj.getValue("a").asObject().getValue("a3").asObject().getValue("aaaaaa").asNumber().doubleValue());
        assertTrue(obj.containsValue("b"));
        assertEquals(ValueType.BOOLEAN, obj.getValue("b").getType());
        assertEquals(true, obj.getValue("b").asBoolean().getValue());
        assertTrue(obj.containsValue("c"));
        assertEquals(ValueType.OBJECT, obj.getValue("c").getType());
        assertEquals(3, obj.getValue("c").asObject().size());
        assertTrue(obj.getValue("c").asObject().containsValue("ac"));
        assertEquals(ValueType.OBJECT, obj.getValue("c").asObject().getValue("ac").getType());
        assertEquals(2, obj.getValue("c").asObject().getValue("ac").asObject().size());
        assertTrue(obj.getValue("c").asObject().getValue("ac").asObject().containsValue("result"));
        assertEquals(ValueType.NUMBER, obj.getValue("c").asObject().getValue("ac").asObject().getValue("result").getType());
        assertEquals(100, obj.getValue("c").asObject().getValue("ac").asObject().getValue("result").asNumber().doubleValue());
        assertTrue(obj.getValue("c").asObject().getValue("ac").asObject().containsValue("response"));
        assertEquals(ValueType.OBJECT, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").getType());
        assertEquals(2, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().size());
        assertTrue(obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().containsValue("list"));
        assertEquals(ValueType.ARRAY, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("list").getType());
        assertEquals(0, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("list").asArray().size());
        assertTrue(obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().containsValue("x"));
        assertEquals(ValueType.OBJECT, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").getType());
        assertEquals(1, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").asObject().size());
        assertTrue(obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").asObject().containsValue("y"));
        assertEquals(ValueType.OBJECT, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").asObject().getValue("y").getType());
        assertEquals(1, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").asObject().getValue("y").asObject().size());
        assertTrue(obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").asObject().getValue("y").asObject().containsValue("z"));
        assertEquals(ValueType.STRING, obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").asObject().getValue("y").asObject().getValue("z").getType());
        assertEquals("???", obj.getValue("c").asObject().getValue("ac").asObject().getValue("response").asObject().getValue("x").asObject().getValue("y").asObject().getValue("z").asString().getValue());
        assertTrue(obj.getValue("c").asObject().containsValue("bc"));
        assertEquals(ValueType.NUMBER, obj.getValue("c").asObject().getValue("bc").getType());
        assertEquals(300, obj.getValue("c").asObject().getValue("bc").asNumber().doubleValue());
        assertTrue(obj.getValue("c").asObject().containsValue("cc"));
        assertEquals(ValueType.OBJECT, obj.getValue("c").asObject().getValue("cc").getType());
        assertTrue(obj.getValue("c").asObject().getValue("cc").asObject().containsValue("acc"));
        assertEquals(1, obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().size());
        assertTrue(obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().containsValue("1"));
        assertEquals(ValueType.ARRAY, obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().getValue("1").getType());
        assertEquals(2, obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().getValue("1").asArray().size());
        assertEquals(ValueType.NUMBER, obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().getValue("1").asArray().getItem(0).getType());
        assertEquals(2, obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().getValue("1").asArray().getItem(0).asNumber().doubleValue());
        assertEquals(ValueType.NUMBER, obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().getValue("1").asArray().getItem(1).getType());
        assertEquals(3, obj.getValue("c").asObject().getValue("cc").asObject().getValue("acc").asObject().getValue("1").asArray().getItem(1).asNumber().doubleValue());
        assertTrue(obj.getValue("c").asObject().getValue("cc").asObject().containsValue("null"));
        assertEquals(ValueType.OBJECT, obj.getValue("c").asObject().getValue("cc").asObject().getValue("null").getType());
        assertEquals(0, obj.getValue("c").asObject().getValue("cc").asObject().getValue("null").asObject().size());
        assertTrue(obj.containsValue("d"));
        assertEquals(ValueType.OBJECT, obj.getValue("d").getType());
        assertEquals(0, obj.getValue("d").asObject().size());
    }
}
