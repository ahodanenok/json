package ahodanenok.json.jp;

import java.math.BigInteger;
import java.math.BigDecimal;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectBuilderImplTest {

    @Test
    public void testBuildEmptyObject() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddJsonValue() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("a", new JsonStringImpl("b"));

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals("b", object.getString("a"));
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddString() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("1", "2");

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals("2", object.getString("1"));
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddBigInteger() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("n", BigInteger.valueOf(3000));

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(BigInteger.valueOf(3000), object.getJsonNumber("n").bigIntegerValue());
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddBigDecimal() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("n1", BigDecimal.valueOf(123.45));

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(BigDecimal.valueOf(123.45), object.getJsonNumber("n1").bigDecimalValue());
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddInt() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("i", -4567);

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(-4567, object.getInt("i"));
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddLong() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("L", Long.MAX_VALUE);

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(Long.MAX_VALUE, object.getJsonNumber("L").longValue());
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddDouble() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("d", 2592.582);

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(2592.582, object.getJsonNumber("d").doubleValue());
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddBoolean() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("bb", true);

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(true, object.getBoolean("bb"));
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddNull() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.addNull("nn");

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(true, object.isNull("nn"));
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddObjectBuilder() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("obj", new JsonObjectBuilderImpl());

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(0, object.getJsonObject("obj").size());
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddArrayBuilder() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("arr", new JsonArrayBuilderImpl());

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(0, object.getJsonArray("arr").size());
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddAllObjectBuilder() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.addAll(new JsonObjectBuilderImpl().add("a", JsonValue.TRUE).add("b", 10));

        JsonObject object = builder.build();
        assertEquals(2, object.size());
        assertEquals(true, object.getBoolean("a"));
        assertEquals(10, object.getInt("b"));
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectRemove() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.remove("none");
        builder.add("x", -1);
        builder.add("y", false);
        builder.remove("x");
        builder.add("z", "s");
        builder.remove("z");

        JsonObject object = builder.build();
        assertEquals(1, object.size());
        assertEquals(false, object.getBoolean("y"));
        assertEquals(0, builder.build().size());
    }

    @Test
    public void testBuildObjectAddMultiple() {
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        builder.add("A", "test");
        builder.add("B", -52.1);
        builder.add("C", true);
        builder.add("D", new JsonArrayBuilderImpl().add(1).build());
        builder.addNull("E");

        JsonObject object = builder.build();
        assertEquals(5, object.size());
        assertEquals("test", object.getString("A"));
        assertEquals(-52.1, object.getJsonNumber("B").doubleValue());
        assertEquals(true, object.getBoolean("C"));
        assertEquals(1, object.getJsonArray("D").size());
        assertEquals(true, object.isNull("E"));
        assertEquals(0, builder.build().size());
    }
}
