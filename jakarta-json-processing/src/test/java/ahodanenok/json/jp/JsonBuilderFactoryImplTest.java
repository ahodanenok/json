package ahodanenok.json.jp;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonBuilderFactoryImplTest {

    @Test
    public void testCreateObjectBuilderEmpty() {
        JsonBuilderFactory factory = new JsonBuilderFactoryImpl();
        assertEquals(0, factory.createObjectBuilder().build().size());
    }

    @Test
    public void testCreateObjectBuilderFromJsonObject() {
        JsonBuilderFactory factory = new JsonBuilderFactoryImpl();
        JsonObject source = new JsonObjectImpl(Map.of(
            "a", JsonValue.TRUE,
            "b", JsonValue.EMPTY_JSON_OBJECT,
            "c", new JsonStringImpl("abc")));
        JsonObject object = factory.createObjectBuilder(source).build();
        assertEquals(3, object.size());
        assertEquals(true, object.getBoolean("a"));
        assertEquals(0, object.getJsonObject("b").size());
        assertEquals("abc", object.getString("c"));
    }

    @Test
    public void testCreateObjectBuilderFromMap() {
        JsonBuilderFactory factory = new JsonBuilderFactoryImpl();
        Map<String, Object> map = new HashMap<>() {{
            put("1", (byte) 32);
            put("2", JsonValue.FALSE);
            put("3", BigInteger.valueOf(912512));
            put("4", "str");
            put("5", true);
            put("6", Long.MAX_VALUE);
            put("7", "test");
            put("8", (short) 255);
            put("9", (double) 613.2123);
            put("10", null);
            put("11", 2351256);
            put("12", BigDecimal.valueOf(-25.125));
        }};
        JsonObject object = factory.createObjectBuilder(map).build();
        assertEquals(12, object.size());
        assertEquals(32, object.getInt("1"));
        assertEquals(false, object.getBoolean("2"));
        assertEquals(BigInteger.valueOf(912512), object.getJsonNumber("3").bigIntegerValue());
        assertEquals("str", object.getString("4"));
        assertEquals(true, object.getBoolean("5"));
        assertEquals(Long.MAX_VALUE, object.getJsonNumber("6").longValue());
        assertEquals("test", object.getString("7"));
        assertEquals(255, object.getInt("8"));
        assertEquals(613.2123, object.getJsonNumber("9").doubleValue());
        assertEquals(true, object.isNull("10"));
        assertEquals(2351256, object.getInt("11"));
        assertEquals(BigDecimal.valueOf(-25.125), object.getJsonNumber("12").bigDecimalValue());
    }

    @Test
    public void testCreateArrayBuilderEmpty() {
        JsonBuilderFactory factory = new JsonBuilderFactoryImpl();
        assertEquals(0, factory.createArrayBuilder().build().size());
    }

    @Test
    public void testCreateArrayBuilderFromJsonArray() {
        JsonBuilderFactory factory = new JsonBuilderFactoryImpl();
        JsonArray source = new JsonArrayImpl(List.of(
            JsonValue.NULL, new JsonNumberIntegerImpl(500), JsonValue.FALSE, new JsonStringImpl("123")));
        JsonArray array = factory.createArrayBuilder(source).build();
        assertEquals(4, array.size());
        assertEquals(true, array.isNull(0));
        assertEquals(500, array.getInt(1));
        assertEquals(false, array.getBoolean(2));
        assertEquals("123", array.getString(3));
    }

    @Test
    public void testCreateArrayBuilderFromCollection() {
        JsonBuilderFactory factory = new JsonBuilderFactoryImpl();
        List<Object> list = new ArrayList<>() {{
            add((byte) 32);
            add(JsonValue.FALSE);
            add(BigInteger.valueOf(912512));
            add("str");
            add(true);
            add(Long.MAX_VALUE);
            add("test");
            add((short) 255);
            add((double) 613.2123);
            add(null);
            add(2351256);
            add(BigDecimal.valueOf(-25.125));
        }};
        JsonArray array = factory.createArrayBuilder(list).build();
        assertEquals(12, array.size());
        assertEquals(32, array.getInt(0));
        assertEquals(false, array.getBoolean(1));
        assertEquals(BigInteger.valueOf(912512), array.getJsonNumber(2).bigIntegerValue());
        assertEquals("str", array.getString(3));
        assertEquals(true, array.getBoolean(4));
        assertEquals(Long.MAX_VALUE, array.getJsonNumber(5).longValue());
        assertEquals("test", array.getString(6));
        assertEquals(255, array.getInt(7));
        assertEquals(613.2123, array.getJsonNumber(8).doubleValue());
        assertEquals(true, array.isNull(9));
        assertEquals(2351256, array.getInt(10));
        assertEquals(BigDecimal.valueOf(-25.125), array.getJsonNumber(11).bigDecimalValue());
    }
}
