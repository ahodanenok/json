package ahodanenok.json.jp;

import java.util.List;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonPatchImplTest {

    @Test
    public void testAdd() {
        JsonArray operations = new JsonArrayImpl(List.of(new JsonObjectImpl(Map.of(
            "op", new JsonStringImpl("add"),
            "path", new JsonStringImpl("/foo/1"),
            "value", new JsonStringImpl("added!")
        ))));
        JsonPatch patch = new JsonPatchImpl(operations);
        JsonObject target = new JsonObjectImpl(Map.of(
            "foo", new JsonArrayImpl(List.of(JsonValue.TRUE)),
            "bar", new JsonNumberIntegerImpl(123)
        ));
        JsonObject result = patch.apply(target);
        assertEquals(2, target.size());
        assertEquals(1, target.get("foo").asJsonArray().size());
        assertEquals(JsonValue.TRUE, target.get("foo").asJsonArray().get(0));
        assertEquals(123, target.getInt("bar"));
        assertEquals(2, result.size());
        assertEquals(2, result.get("foo").asJsonArray().size());
        assertEquals(JsonValue.TRUE, result.get("foo").asJsonArray().get(0));
        assertEquals("added!", result.get("foo").asJsonArray().getString(1));
        assertEquals(123, result.getInt("bar"));
    }

    @Test
    public void testRemove() {
        JsonArray operations = new JsonArrayImpl(List.of(new JsonObjectImpl(Map.of(
           "op", new JsonStringImpl("remove"),
           "path", new JsonStringImpl("/y/a1")
        ))));
        JsonObject target = new JsonObjectImpl(Map.of(
            "x", new JsonNumberIntegerImpl(1),
            "y", new JsonObjectImpl(Map.of(
                "a1", new JsonStringImpl("test"),
                "a2", JsonValue.TRUE
            ))
        ));
        JsonObject result = new JsonPatchImpl(operations).apply(target);
        assertEquals(2, target.size());
        assertEquals(1, target.getInt("x"));
        assertEquals(2, target.get("y").asJsonObject().size());
        assertEquals("test", target.get("y").asJsonObject().getString("a1"));
        assertEquals(JsonValue.TRUE, target.get("y").asJsonObject().get("a2"));
        assertEquals(2, result.size());
        assertEquals(1, result.getInt("x"));
        assertEquals(1, result.get("y").asJsonObject().size());
        assertEquals(JsonValue.TRUE, target.get("y").asJsonObject().get("a2"));
    }

    @Test
    public void testReplace() {
        JsonArray operations = new JsonArrayImpl(List.of(new JsonObjectImpl(Map.of(
            "op", new JsonStringImpl("replace"),
            "path", new JsonStringImpl("/ef"),
            "value", new JsonArrayImpl(List.of(JsonValue.TRUE))
        ))));
        JsonObject target = new JsonObjectImpl(Map.of(
            "abcd", new JsonNumberIntegerImpl(3210),
            "ef", JsonValue.FALSE
        ));
        JsonObject result = new JsonPatchImpl(operations).apply(target);
        assertEquals(2, target.size());
        assertEquals(3210, target.getInt("abcd"));
        assertEquals(JsonValue.FALSE, target.get("ef"));
        assertEquals(2, result.size());
        assertEquals(3210, result.getInt("abcd"));
        assertEquals(1, result.get("ef").asJsonArray().size());
        assertEquals(JsonValue.TRUE, result.get("ef").asJsonArray().get(0));
    }

    @Test
    public void testCopy() {
        JsonArray operations = new JsonArrayImpl(List.of(new JsonObjectImpl(Map.of(
            "op", new JsonStringImpl("copy"),
            "path", new JsonStringImpl("/0"),
            "from", new JsonStringImpl("/1/a")
        ))));
        JsonArray target = new JsonArrayImpl(List.of(
            new JsonNumberIntegerImpl(10),
            new JsonObjectImpl(Map.of(
                "a", new JsonStringImpl("test")
            ))
        ));
        JsonArray result = new JsonPatchImpl(operations).apply(target);
        assertEquals(2, target.size());
        assertEquals(10, target.getInt(0));
        assertEquals(1, target.get(1).asJsonObject().size());
        assertEquals("test", target.get(1).asJsonObject().getString("a"));
        assertEquals(3, result.size());
        assertEquals("test", result.getString(0));
        assertEquals(10, result.getInt(1));
        assertEquals(1, result.get(2).asJsonObject().size());
        assertEquals("test", result.get(2).asJsonObject().getString("a"));
    }

    @Test
    public void testMove() {
        JsonArray operations = new JsonArrayImpl(List.of(new JsonObjectImpl(Map.of(
            "op", new JsonStringImpl("move"),
            "from", new JsonStringImpl("/1/a"),
            "path", new JsonStringImpl("/1")
        ))));
        JsonArray target = new JsonArrayImpl(List.of(
            new JsonNumberIntegerImpl(10),
            new JsonObjectImpl(Map.of(
                "a", new JsonStringImpl("test"),
                "b", JsonValue.TRUE
            ))
        ));
        JsonArray result = new JsonPatchImpl(operations).apply(target);
        assertEquals(2, target.size());
        assertEquals(10, target.getInt(0));
        assertEquals(2, target.get(1).asJsonObject().size());
        assertEquals("test", target.get(1).asJsonObject().getString("a"));
        assertEquals(JsonValue.TRUE, target.get(1).asJsonObject().get("b"));
        assertEquals(3, result.size());
        assertEquals(10, result.getInt(0));
        assertEquals("test", result.getString(1));
        assertEquals(1, result.get(2).asJsonObject().size());
        assertEquals(JsonValue.TRUE, result.get(2).asJsonObject().get("b"));
    }

    @Test
    public void testTest() {
        JsonArray operations = new JsonArrayImpl(List.of(new JsonObjectImpl(Map.of(
            "op", new JsonStringImpl("test"),
            "path", new JsonStringImpl("/y/0"),
            "value", new JsonNumberIntegerImpl(222)
        ))));
        JsonObject target = new JsonObjectImpl(Map.of(
            "x", JsonValue.TRUE,
            "y", new JsonArrayImpl(List.of(new JsonNumberIntegerImpl(222)))
        ));
        JsonObject result = new JsonPatchImpl(operations).apply(target);
        assertEquals(target, result);
    }
}
