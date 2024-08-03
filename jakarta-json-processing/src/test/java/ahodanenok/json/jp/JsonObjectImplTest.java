package ahodanenok.json.jp;

import java.util.List;
import java.util.Map;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonObjectImplTest {

    @Test
    public void testEmptyObject() {
        JsonObject object = new JsonObjectImpl(Map.of());
        assertEquals(JsonValue.ValueType.OBJECT, object.getValueType());
        assertEquals(0, object.size());
        assertEquals(null, object.getJsonArray("a"));
        assertEquals(null, object.getJsonNumber("b"));
        assertEquals(null, object.getJsonObject("c"));
        assertEquals(null, object.getJsonString("d"));
        assertThrows(NullPointerException.class, () -> object.getBoolean("e"));
        assertEquals(true, object.getBoolean("e", true));
        assertThrows(NullPointerException.class, () -> object.getString("f"));
        assertEquals("test", object.getString("f", "test"));
        assertThrows(NullPointerException.class, () -> object.getInt("g"));
        assertEquals(1234, object.getInt("g", 1234));
        assertThrows(NullPointerException.class, () -> object.isNull("h"));
        assertEquals(false, object.entrySet().iterator().hasNext());
    }

    @Test
    public void testObject() {
        JsonObject object = new JsonObjectImpl(Map.of(
            "a", JsonValue.TRUE,
            "b", JsonValue.NULL,
            "c", new JsonNumberIntegerImpl(2000),
            "d", new JsonObjectImpl(Map.of("result", new JsonStringImpl("ok"))),
            "e", new JsonStringImpl("str"),
            "f", new JsonArrayImpl(List.of(new JsonNumberDoubleImpl(123)))
        ));
        assertEquals(JsonValue.ValueType.OBJECT, object.getValueType());
        assertEquals(6, object.size());
        assertEquals(1, object.getJsonArray("f").size());
        assertEquals(123, object.getJsonArray("f").getInt(0));
        assertEquals(2000, object.getJsonNumber("c").intValue());
        assertEquals(1, object.getJsonObject("d").size());
        assertEquals("ok", object.getJsonObject("d").getString("result"));
        assertEquals("str", object.getJsonString("e").getString());
        assertEquals(true, object.getBoolean("a"));
        assertEquals(false, object.getBoolean("bbb", false));
        assertEquals("str", object.getString("e"));
        assertEquals("test", object.getString("eee", "test"));
        assertEquals(2000, object.getInt("c"));
        assertEquals(12345, object.getInt("ccc", 12345));
        assertEquals(true, object.isNull("b"));
        assertEquals(6, object.entrySet().size());
    }
}