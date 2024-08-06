package ahodanenok.json.jp;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;
import jakarta.json.stream.JsonParsingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonReaderImplTest {

    @Test
    public void testErrorWhenMultipleValues() {
         JsonReader parser = new JsonReaderImpl(new StringReader("300 true"));
        JsonParsingException e = assertThrows(
            JsonParsingException.class, () -> parser.readValue());
        assertEquals("Unexpected token 'true' after the value", e.getMessage());
    }

    @Test
    public void testErrorNoContent() {
        JsonReader parser = new JsonReaderImpl(new StringReader(""));
        JsonParsingException e = assertThrows(JsonParsingException.class, () -> parser.read());
        assertEquals("No content", e.getMessage());
    }

    @Test
    public void testParseEmptyString() {
        JsonReader parser = new JsonReaderImpl(new StringReader("\"\""));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.STRING);
        assertEquals("", ((JsonString) value).getString());
    }

    @ParameterizedTest
    @CsvSource({
        "\"abc\",     abc",
        "\"hello world\",     hello world"
    })
    public void testParseString(String s, String expected) {
        JsonReader parser = new JsonReaderImpl(new StringReader(s));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.STRING);
        assertEquals(expected, ((JsonString) value).getString());
    }

    @ParameterizedTest
    @CsvSource({
        "0.00,     0.0",
        "-12345.6789,     -12345.6789",
        "37e+3,     37000",
        "-5E-4,     -0.0005"
    })
    public void testParseNumber(String s, double expected) {
        JsonReader parser = new JsonReaderImpl(new StringReader(s));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.NUMBER);
        assertEquals(expected, ((JsonNumber) value).doubleValue());
    }

    @Test
    public void testParseNull() {
        JsonReader parser = new JsonReaderImpl(new StringReader("null"));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.NULL);
        assertEquals(JsonValue.NULL, value);
    }

    @Test
    public void testParseTrue() {
        JsonReader parser = new JsonReaderImpl(new StringReader("true"));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.TRUE);
        assertEquals(JsonValue.TRUE, value);
    }

    @Test
    public void testParseFalse() {
        JsonReader parser = new JsonReaderImpl(new StringReader("false"));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.FALSE);
        assertEquals(JsonValue.FALSE, value);
    }

    @ParameterizedTest
    @ValueSource(strings = { "[", "[100", "[true,", "[true,{\"a\":2}" })
    public void testErrorUnexpectedEndOfArray(String s) {
        JsonReader parser = new JsonReaderImpl(new StringReader(s));
        JsonParsingException e = assertThrows(
            JsonParsingException.class, () -> parser.readValue());
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
        JsonReader parser = new JsonReaderImpl(new StringReader(s));
        JsonParsingException e = assertThrows(
            JsonParsingException.class, () -> parser.readValue());
        assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    }

    @Test
    public void testParseEmptyArray() {
        JsonReader parser = new JsonReaderImpl(new StringReader("[]"));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.ARRAY);
        assertEquals(0, value.asJsonArray().size());
    }

    @Test
    public void testParseArrayOneLevel() {
        JsonReader parser = new JsonReaderImpl(new StringReader("[1.23, true, \"true\", null]"));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.ARRAY);
        JsonArray array = assertInstanceOf(JsonArray.class, value);
        assertEquals(4, array.size());
        assertEquals(1.23, array.getJsonNumber(0).doubleValue());
        assertEquals(true, array.getBoolean(1));
        assertEquals("true", array.getJsonString(2).getString());
        assertEquals(true, array.isNull(3));
    }

    @Test
    public void testParseArrayNested() {
        JsonReader parser = new JsonReaderImpl(new StringReader("""
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
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.ARRAY);
        JsonArray array = value.asJsonArray();
        assertEquals(5, array.size());
        assertEquals(false, array.getBoolean(0));
        assertEquals(2, array.getJsonArray(1).size());
        assertEquals(1, array.getJsonArray(1).getJsonArray(0).size());
        assertEquals(500, array.getJsonArray(1).getJsonArray(0).getJsonNumber(0).doubleValue());
        assertEquals(true, array.getJsonArray(1).isNull(1));
        assertEquals(0, array.getJsonArray(2).size());
        assertEquals(true, array.getBoolean(3));
        assertEquals(2, array.getJsonArray(4).size());
        assertEquals("x", array.getJsonArray(4).getJsonString(0).getString());
        assertEquals(2, array.getJsonArray(4).getJsonArray(1).size());
        assertEquals("yz", array.getJsonArray(4).getJsonArray(1).getJsonString(0).getString());
        assertEquals(1, array.getJsonArray(4).getJsonArray(1).getJsonArray(1).size());
        assertEquals(1, array.getJsonArray(4).getJsonArray(1).getJsonArray(1).getJsonArray(0).size());
        assertEquals(1, array.getJsonArray(4).getJsonArray(1).getJsonArray(1).getJsonArray(0).getJsonArray(0).size());
        assertEquals(-2.34, array.getJsonArray(4).getJsonArray(1).getJsonArray(1).getJsonArray(0).getJsonArray(0).getJsonNumber(0).doubleValue());
    }

    @ParameterizedTest
    @ValueSource(strings = { "{", "{\"a\":", "{\"a\":1,", "{\"a\":1,\"b\":true" })
    public void testErrorUnexpectedEndOfObject(String s) {
        JsonReader parser = new JsonReaderImpl(new StringReader(s));
        JsonParsingException e = assertThrows(
            JsonParsingException.class, () -> parser.readValue());
        assertEquals("Unexpected end of an object", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "---", value = {
        "{\"ddd\": 300 : --- :",
        "{\"x\": {} [] --- ["
    })
    public void testErrorUnexpectedValueSeparator(String s, String expected) {
        JsonReader parser = new JsonReaderImpl(new StringReader(s));
        JsonParsingException e = assertThrows(
            JsonParsingException.class, () -> parser.readValue());
        assertEquals(String.format("Expected ',' but '%s' was encountered", expected), e.getMessage());
    }

    @Test
    public void testErrorExpectedName() {
        JsonReader parser = new JsonReaderImpl(new StringReader("{\"abc\":false, 521"));
        JsonParsingException e = assertThrows(
            JsonParsingException.class, () -> parser.readValue());
        assertEquals("Expected name, but '521' was encountered", e.getMessage());
    }

    @Test
    public void testErrorExpectedNameSeparator() {
        JsonReader parser = new JsonReaderImpl(new StringReader("{\"abc\" false}"));
        JsonParsingException e = assertThrows(
            JsonParsingException.class, () -> parser.readValue());
        assertEquals("Expected ':', but 'false' was encountered", e.getMessage());
    }

    @Test
    public void testParseEmptyObject() {
        JsonReader parser = new JsonReaderImpl(new StringReader("{}"));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.OBJECT);
        assertEquals(0, value.asJsonObject().size());
    }

    @Test
    public void testParseObjectWithEmptyName() {
        JsonReader parser = new JsonReaderImpl(new StringReader("{ \"\": 1 }"));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.OBJECT);
        JsonObject obj = assertInstanceOf(JsonObject.class, value);
        assertEquals(1, obj.size());
        assertEquals(1, obj.getJsonNumber("").intValue());
    }

    @Test
    public void testParseObjectOneLevel() {
        JsonReader parser = new JsonReaderImpl(new StringReader("""
            {
                "status": "ok",
                "num": -321,
                "items": ["a", "b"],
                "true": false,
                "???": null
            }
            """));
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.OBJECT);
        JsonObject obj = assertInstanceOf(JsonObject.class, value);
        assertEquals(5, obj.size());
        assertEquals("ok", obj.getJsonString("status").getString());
        assertEquals(-321, obj.getJsonNumber("num").doubleValue());
        assertEquals(2, obj.getJsonArray("items").size());
        assertEquals("a", obj.getJsonArray("items").getJsonString(0).getString());
        assertEquals("b", obj.getJsonArray("items").getJsonString(1).getString());
        assertEquals(false, obj.getBoolean("true"));
        assertEquals(true, obj.isNull("???"));
    }

    @Test
    public void testParseObjectNested() {
        JsonReader parser = new JsonReaderImpl(new StringReader("""
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
        JsonValue value = parser.readValue();
        assertEquals(value.getValueType(), ValueType.OBJECT);
        JsonObject obj = assertInstanceOf(JsonObject.class, value);
        assertEquals(4, obj.size());
        assertEquals(3, obj.getJsonObject("a").size());
        assertEquals(true, obj.getJsonObject("a").getBoolean("a1"));
        assertEquals(4, obj.getJsonObject("a").getJsonNumber("a2").doubleValue());
        assertEquals(1, obj.getJsonObject("a").getJsonObject("a3").size());
        assertEquals(-10, obj.getJsonObject("a").getJsonObject("a3").getJsonNumber("aaaaaa").doubleValue());
        assertEquals(true, obj.getBoolean("b"));
        assertEquals(3, obj.getJsonObject("c").size());
        assertEquals(2, obj.getJsonObject("c").getJsonObject("ac").size());
        assertEquals(100, obj.getJsonObject("c").getJsonObject("ac").getJsonNumber("result").doubleValue());
        assertEquals(2, obj.getJsonObject("c").getJsonObject("ac").getJsonObject("response").size());
        assertEquals(0, obj.getJsonObject("c").getJsonObject("ac").getJsonObject("response").getJsonArray("list").size());
        assertEquals(1, obj.getJsonObject("c").getJsonObject("ac").getJsonObject("response").getJsonObject("x").size());
        assertEquals(1, obj.getJsonObject("c").getJsonObject("ac").getJsonObject("response").getJsonObject("x").getJsonObject("y").size());
        assertEquals("???", obj.getJsonObject("c").getJsonObject("ac").getJsonObject("response").getJsonObject("x").getJsonObject("y").getJsonString("z").getString());
        assertEquals(300, obj.getJsonObject("c").getJsonNumber("bc").doubleValue());
        assertEquals(1, obj.getJsonObject("c").getJsonObject("cc").getJsonObject("acc").size());
        assertEquals(2, obj.getJsonObject("c").getJsonObject("cc").getJsonObject("acc").getJsonArray("1").size());
        assertEquals(2, obj.getJsonObject("c").getJsonObject("cc").getJsonObject("acc").getJsonArray("1").getJsonNumber(0).doubleValue());
        assertEquals(3, obj.getJsonObject("c").getJsonObject("cc").getJsonObject("acc").getJsonArray("1").getJsonNumber(1).doubleValue());
        assertEquals(0, obj.getJsonObject("c").getJsonObject("cc").getJsonObject("null").size());
        assertEquals(0, obj.getJsonObject("d").size());
    }

    @Test
    public void testParseRfcExample_1() {
        JsonReader parser = new JsonReaderImpl(new StringReader("""
            {
                \"Image\": {
                    \"Width\":  800,
                    \"Height\": 600,
                    \"Title\":  \"View from 15th Floor\",
                    \"Thumbnail\": {
                        \"Url\":    \"http://www.example.com/image/481989943\",
                        \"Height\": 125,
                        \"Width\":  100
                    },
                    \"Animated\" : false,
                    \"IDs\": [116, 943, 234, 38793]
                }
            }
        """));
        JsonValue value = parser.readValue();
        assertEquals(ValueType.OBJECT, value.getValueType());
        JsonObject obj = value.asJsonObject();
        assertEquals(1, obj.size());
        assertEquals(6, obj.getJsonObject("Image").size());
        assertEquals(800, obj.getJsonObject("Image").getJsonNumber("Width").doubleValue());
        assertEquals(600, obj.getJsonObject("Image").getJsonNumber("Height").doubleValue());
        assertEquals("View from 15th Floor", obj.getJsonObject("Image").getJsonString("Title").getString());
        assertEquals(false, obj.getJsonObject("Image").getBoolean("Animated"));
        assertEquals(4, obj.getJsonObject("Image").getJsonArray("IDs").size());
        assertEquals(116, obj.getJsonObject("Image").getJsonArray("IDs").getJsonNumber(0).doubleValue());
        assertEquals(943, obj.getJsonObject("Image").getJsonArray("IDs").getJsonNumber(1).doubleValue());
        assertEquals(234, obj.getJsonObject("Image").getJsonArray("IDs").getJsonNumber(2).doubleValue());
        assertEquals(38793, obj.getJsonObject("Image").getJsonArray("IDs").getJsonNumber(3).doubleValue());
        assertEquals(3, obj.getJsonObject("Image").getJsonObject("Thumbnail").size());
        assertEquals("http://www.example.com/image/481989943", obj.getJsonObject("Image").getJsonObject("Thumbnail").getJsonString("Url").getString());
        assertEquals(125, obj.getJsonObject("Image").getJsonObject("Thumbnail").getJsonNumber("Height").doubleValue());
        assertEquals(100, obj.getJsonObject("Image").getJsonObject("Thumbnail").getJsonNumber("Width").doubleValue());
    }

    @Test
    public void testParseRfcExample_2() {
        JsonReader parser = new JsonReaderImpl(new StringReader("""
            [
                {
                    \"precision\": \"zip\",
                    \"Latitude\":  37.7668,
                    \"Longitude\": -122.3959,
                    \"Address\":   \"\",
                    \"City\":      \"SAN FRANCISCO\",
                    \"State\":     \"CA\",
                    \"Zip\":       \"94107\",
                    \"Country\":   \"US\"
                },
                {
                    \"precision\": \"zip\",
                    \"Latitude\":  37.371991,
                    \"Longitude\": -122.026020,
                    \"Address\":   \"\",
                    \"City\":      \"SUNNYVALE\",
                    \"State\":     \"CA\",
                    \"Zip\":       \"94085\",
                    \"Country\":   \"US\"
                }
            ]
        """));
        JsonValue value = parser.readValue();

        assertEquals(ValueType.ARRAY, value.getValueType());
        JsonArray array = value.asJsonArray();
        assertEquals(2, array.size());

        assertEquals(8, array.getJsonObject(0).size());
        assertEquals("zip", array.getJsonObject(0).getJsonString("precision").getString());
        assertEquals(37.7668, array.getJsonObject(0).getJsonNumber("Latitude").doubleValue());
        assertEquals(-122.3959, array.getJsonObject(0).getJsonNumber("Longitude").doubleValue());
        assertEquals("", array.getJsonObject(0).getJsonString("Address").getString());
        assertEquals("SAN FRANCISCO", array.getJsonObject(0).getJsonString("City").getString());
        assertEquals("CA", array.getJsonObject(0).getJsonString("State").getString());
        assertEquals("94107", array.getJsonObject(0).getJsonString("Zip").getString());
        assertEquals("US", array.getJsonObject(0).getJsonString("Country").getString());

        assertEquals(8, array.getJsonObject(1).size());
        assertEquals("zip", array.getJsonObject(1).getJsonString("precision").getString());
        assertEquals(37.371991, array.getJsonObject(1).getJsonNumber("Latitude").doubleValue());
        assertEquals(-122.026020, array.getJsonObject(1).getJsonNumber("Longitude").doubleValue());
        assertEquals("", array.getJsonObject(1).getJsonString("Address").getString());
        assertEquals("SUNNYVALE", array.getJsonObject(1).getJsonString("City").getString());
        assertEquals("CA", array.getJsonObject(1).getJsonString("State").getString());
        assertEquals("94085", array.getJsonObject(1).getJsonString("Zip").getString());
        assertEquals("US", array.getJsonObject(1).getJsonString("Country").getString());
    }
}
