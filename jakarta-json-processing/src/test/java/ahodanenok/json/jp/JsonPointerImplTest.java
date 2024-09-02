package ahodanenok.json.jp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonPointerImplTest {

    @Test
    public void testCreateJsonPointer() {
        assertEquals(List.of(), new JsonPointerImpl("").tokens);
        assertEquals(List.of(" "), new JsonPointerImpl("/ ").tokens);
        assertEquals(List.of(" ", "  "), new JsonPointerImpl("/ /  ").tokens);
        assertEquals(List.of("123"), new JsonPointerImpl("/123").tokens);
        assertEquals(List.of("test"), new JsonPointerImpl("/test").tokens);
        assertEquals(List.of("c%d"), new JsonPointerImpl("/c%d").tokens);
        assertEquals(List.of("a/b"), new JsonPointerImpl("/a~1b").tokens);
        assertEquals(List.of("m~n"), new JsonPointerImpl("/m~0n").tokens);
        assertEquals(List.of("k\\\"l"), new JsonPointerImpl("/k\\\"l").tokens);
        assertEquals(List.of("foo", "bar"), new JsonPointerImpl("/foo/bar").tokens);
        assertEquals(List.of("foo", "0"), new JsonPointerImpl("/foo/0").tokens);
        assertEquals(List.of("response", "0", "items", "25", "name"), new JsonPointerImpl("/response/0/items/25/name").tokens);
    }

    @Test
    public void testAddObjectEmptyPointer() {
        JsonObject a = new JsonObjectImpl(Map.of());
        JsonObject b = new JsonObjectImpl(Map.of());
        assertSame(b, new JsonPointerImpl("").add(a, b));
    }

    @Test
    public void testAddArrayEmptyPointer() {
        JsonArray a = new JsonArrayImpl(List.of());
        JsonArray b = new JsonArrayImpl(List.of());
        assertSame(b, new JsonPointerImpl("").add(a, b));
    }

    @Test
    public void testErrorWhenAddDifferentTypesAtEmptyPointer() {
        JsonArray a = new JsonArrayImpl(List.of());
        JsonObject b = new JsonObjectImpl(Map.of());
        JsonException e1 = assertThrows(JsonException.class, () -> new JsonPointerImpl("").add(a, b));
        assertEquals(
            "Value of type 'ahodanenok.json.jp.JsonObjectImpl' is not assignable to the target of type 'ahodanenok.json.jp.JsonArrayImpl'",
            e1.getMessage());
        JsonException e2 = assertThrows(JsonException.class, () -> new JsonPointerImpl("").add(b, a));
        assertEquals(
            "Value of type 'ahodanenok.json.jp.JsonArrayImpl' is not assignable to the target of type 'ahodanenok.json.jp.JsonObjectImpl'",
            e2.getMessage());
    }

    @Test
    public void testAddValueToArray() {
        JsonArray array = new JsonArrayImpl(List.of(
            new JsonNumberIntegerImpl(10),
            new JsonNumberIntegerImpl(20),
            new JsonNumberIntegerImpl(30)));
        JsonPointer p0 = new JsonPointerImpl("/0");
        JsonPointer p1 = new JsonPointerImpl("/2");
        JsonPointer p2 = new JsonPointerImpl("/5");
        JsonPointer p3 = new JsonPointerImpl("/-");

        JsonArray result0 = p0.add(array, new JsonNumberIntegerImpl(1));
        assertEquals(10, array.getInt(0));
        assertEquals(20, array.getInt(1));
        assertEquals(30, array.getInt(2));
        assertEquals(1, result0.getInt(0));
        assertEquals(10, result0.getInt(1));
        assertEquals(20, result0.getInt(2));
        assertEquals(30, result0.getInt(3));

        JsonArray result1 = p1.add(result0, new JsonNumberIntegerImpl(2));
        assertEquals(1, result0.getInt(0));
        assertEquals(10, result0.getInt(1));
        assertEquals(20, result0.getInt(2));
        assertEquals(30, result0.getInt(3));
        assertEquals(1, result1.getInt(0));
        assertEquals(10, result1.getInt(1));
        assertEquals(2, result1.getInt(2));
        assertEquals(20, result1.getInt(3));
        assertEquals(30, result1.getInt(4));

        JsonArray result2 = p2.add(result1, new JsonNumberIntegerImpl(3));
        assertEquals(1, result1.getInt(0));
        assertEquals(10, result1.getInt(1));
        assertEquals(2, result1.getInt(2));
        assertEquals(20, result1.getInt(3));
        assertEquals(30, result1.getInt(4));
        assertEquals(1, result2.getInt(0));
        assertEquals(10, result2.getInt(1));
        assertEquals(2, result2.getInt(2));
        assertEquals(20, result2.getInt(3));
        assertEquals(30, result2.getInt(4));
        assertEquals(3, result2.getInt(5));

        JsonArray result3 = p3.add(result2, new JsonNumberIntegerImpl(4));
        assertEquals(1, result2.getInt(0));
        assertEquals(10, result2.getInt(1));
        assertEquals(2, result2.getInt(2));
        assertEquals(20, result2.getInt(3));
        assertEquals(30, result2.getInt(4));
        assertEquals(3, result2.getInt(5));
        assertEquals(1, result3.getInt(0));
        assertEquals(10, result3.getInt(1));
        assertEquals(2, result3.getInt(2));
        assertEquals(20, result3.getInt(3));
        assertEquals(30, result3.getInt(4));
        assertEquals(3, result3.getInt(5));
        assertEquals(4, result3.getInt(6));
    }

    @Test
    public void testAddValueToArrayInsideArray() {
        JsonArray array = new JsonArrayImpl(List.of(
            new JsonNumberIntegerImpl(10),
            new JsonArrayImpl(List.of(
                new JsonNumberIntegerImpl(20),
                new JsonNumberIntegerImpl(30)
            ))
        ));
        JsonPointer p = new JsonPointerImpl("/1/0");

        JsonArray result = p.add(array, new JsonNumberIntegerImpl(1));
        assertEquals(10, array.getInt(0));
        assertEquals(20, array.get(1).asJsonArray().getInt(0));
        assertEquals(30, array.get(1).asJsonArray().getInt(1));
        assertEquals(10, result.getInt(0));
        assertEquals(1, result.get(1).asJsonArray().getInt(0));
        assertEquals(20, result.get(1).asJsonArray().getInt(1));
        assertEquals(30, result.get(1).asJsonArray().getInt(2));
    }

    @Test
    public void testAddValueToArrayInsideObject() {
        JsonObject object = new JsonObjectImpl(new LinkedHashMap<>() {{
            put("a", new JsonNumberIntegerImpl(100));
            put("b", new JsonArrayImpl(List.of(
                new JsonNumberIntegerImpl(10),
                new JsonNumberIntegerImpl(20)
            )));
            put("c", new JsonNumberIntegerImpl(200));
        }});
        JsonPointer p = new JsonPointerImpl("/b/1");

        JsonObject result = p.add(object, new JsonNumberIntegerImpl(1));
        assertEquals(100, object.getInt("a"));
        assertEquals(10, object.get("b").asJsonArray().getInt(0));
        assertEquals(20, object.get("b").asJsonArray().getInt(1));
        assertEquals(200, object.getInt("c"));
        assertEquals(100, result.getInt("a"));
        assertEquals(10, result.get("b").asJsonArray().getInt(0));
        assertEquals(1, result.get("b").asJsonArray().getInt(1));
        assertEquals(20, result.get("b").asJsonArray().getInt(2));
        assertEquals(200, result.getInt("c"));
    }

    @Test
    public void testAddValueToObject() {
        JsonObject object = new JsonObjectImpl(new LinkedHashMap<>() {{
            put("a", JsonValue.TRUE);
            put("b", new JsonNumberIntegerImpl(100));
            put("c", new JsonStringImpl("test"));
        }});

        JsonObject result0 = new JsonPointerImpl("/a").add(object, new JsonNumberIntegerImpl(500));
        assertEquals(JsonValue.TRUE, object.get("a"));
        assertEquals(100, object.getInt("b"));
        assertEquals("test", object.getString("c"));
        assertEquals(500, result0.getInt("a"));
        assertEquals(100, result0.getInt("b"));
        assertEquals("test", result0.getString("c"));

        JsonObject result1 = new JsonPointerImpl("/bb").add(result0, JsonValue.FALSE);
        assertEquals(500, result0.getInt("a"));
        assertEquals(100, result0.getInt("b"));
        assertEquals("test", result0.getString("c"));
        assertEquals(500, result1.getInt("a"));
        assertEquals(100, result1.getInt("b"));
        assertEquals("test", result1.getString("c"));
        assertEquals(JsonValue.FALSE, result1.get("bb"));
    }

    @Test
    public void testAddValueToObjectInsideArray() {
        JsonArray array = new JsonArrayImpl(List.of(
            JsonValue.NULL,
            new JsonNumberIntegerImpl(123),
            new JsonObjectImpl(new LinkedHashMap<>() {{
                put("foo", new JsonStringImpl("bar"));
                put("a1", JsonValue.TRUE);
            }})
        ));

        JsonArray result = new JsonPointerImpl("/2/abcd").add(array, new JsonNumberIntegerImpl(-3));
        assertEquals(JsonValue.NULL, array.get(0));
        assertEquals(123, array.getInt(1));
        assertEquals("bar", array.get(2).asJsonObject().getString("foo"));
        assertEquals(JsonValue.TRUE, array.get(2).asJsonObject().get("a1"));
        assertEquals(JsonValue.NULL, result.get(0));
        assertEquals(123, result.getInt(1));
        assertEquals("bar", result.get(2).asJsonObject().getString("foo"));
        assertEquals(JsonValue.TRUE, result.get(2).asJsonObject().get("a1"));
        assertEquals(-3, result.get(2).asJsonObject().getInt("abcd"));
    }

    @Test
    public void testAddValueToObjectInsideObject() {
        JsonObject object = new JsonObjectImpl(new LinkedHashMap<>() {{
            put("x", new JsonObjectImpl(new LinkedHashMap<>() {{
                put("y", new JsonObjectImpl(new LinkedHashMap<>() {{
                    put("abc", new JsonNumberIntegerImpl(100));
                }}));
            }}));
            put("z", JsonValue.TRUE);
        }});

        JsonObject result = new JsonPointerImpl("/x/y/z").add(object, new JsonStringImpl("test"));
        assertEquals(1, object.get("x").asJsonObject().get("y").asJsonObject().size());
        assertEquals(100, object.get("x").asJsonObject().get("y").asJsonObject().getInt("abc"));
        assertEquals(JsonValue.TRUE, object.get("z"));
        assertEquals(2, result.get("x").asJsonObject().get("y").asJsonObject().size());
        assertEquals(100, result.get("x").asJsonObject().get("y").asJsonObject().getInt("abc"));
        assertEquals("test", result.get("x").asJsonObject().get("y").asJsonObject().getString("z"));
        assertEquals(JsonValue.TRUE, result.get("z"));
    }

    @Test
    public void testContainsValueArray() {
        JsonArray array = new JsonArrayImpl(List.of(JsonValue.TRUE, new JsonNumberIntegerImpl(123)));
        assertEquals(true, new JsonPointerImpl("").containsValue(array));
        assertEquals(true, new JsonPointerImpl("/0").containsValue(array));
        assertEquals(true, new JsonPointerImpl("/1").containsValue(array));
        assertEquals(false, new JsonPointerImpl("/-1").containsValue(array));
        assertEquals(false, new JsonPointerImpl("/2").containsValue(array));
        assertEquals(false, new JsonPointerImpl("/-").containsValue(array));
        assertEquals(false, new JsonPointerImpl("/x").containsValue(array));
    }

    @Test
    public void testContainsValueObject() {
        JsonObject object = new JsonObjectImpl(Map.of(
            "a", new JsonStringImpl("test"),
            "b", JsonValue.NULL
        ));
        assertEquals(true, new JsonPointerImpl("").containsValue(object));
        assertEquals(true, new JsonPointerImpl("/a").containsValue(object));
        assertEquals(true, new JsonPointerImpl("/b").containsValue(object));
        assertEquals(false, new JsonPointerImpl("/A").containsValue(object));
        assertEquals(false, new JsonPointerImpl("/-").containsValue(object));
        assertEquals(false, new JsonPointerImpl("/0").containsValue(object));
    }

    @Test
    public void testGetValueArray() {
        JsonArray array = new JsonArrayImpl(List.of(
            new JsonNumberIntegerImpl(123),
            JsonValue.TRUE,
            new JsonStringImpl("abc")));
        assertEquals(array, new JsonPointerImpl("").getValue(array));
        assertEquals(new JsonNumberIntegerImpl(123), new JsonPointerImpl("/0").getValue(array));
        assertEquals(JsonValue.TRUE, new JsonPointerImpl("/1").getValue(array));
        assertEquals(new JsonStringImpl("abc"), new JsonPointerImpl("/2").getValue(array));
        assertThrows(JsonException.class, () -> new JsonPointerImpl("/3").getValue(array));
        assertThrows(JsonException.class, () -> new JsonPointerImpl("/-").getValue(array));
        assertThrows(JsonException.class, () -> new JsonPointerImpl("/abc").getValue(array));
    }

    @Test
    public void testGetValueObject() {
        JsonObject object = new JsonObjectImpl(Map.of(
            "x", JsonValue.TRUE,
            "y", new JsonNumberIntegerImpl(500)
        ));
        assertEquals(object, new JsonPointerImpl("").getValue(object));
        assertEquals(JsonValue.TRUE, new JsonPointerImpl("/x").getValue(object));
        assertEquals(new JsonNumberIntegerImpl(500), new JsonPointerImpl("/y").getValue(object));
        assertThrows(JsonException.class, () -> new JsonPointerImpl("/z").getValue(object));
    }
}
