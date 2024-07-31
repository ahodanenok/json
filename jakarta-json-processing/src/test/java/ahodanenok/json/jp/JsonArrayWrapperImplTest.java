package ahodanenok.json.jp;

import org.junit.jupiter.api.Test;

import jakarta.json.JsonArray;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonArrayWrapperImplTest {

    @Test
    public void testEmpty() {
        JsonArray array = new JsonArrayWrapperImpl(ahodanenok.json.value.JsonArray.builder()
            .build());
        assertEquals(JsonValue.ValueType.ARRAY, array.getValueType());
        assertEquals(0, array.size());
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.getJsonObject(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.getJsonArray(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.getJsonNumber(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.getJsonString(0));
        assertEquals(0, array.getValuesAs(JsonString.class).size());
        assertThrows(IndexOutOfBoundsException.class, () -> array.getString(0));
        assertEquals("abc", array.getString(0, "abc"));
        assertThrows(IndexOutOfBoundsException.class, () -> array.getInt(0));
        assertEquals(123, array.getInt(0, 123));
        assertThrows(IndexOutOfBoundsException.class, () -> array.getBoolean(0));
        assertEquals(true, array.getBoolean(0, true));
        assertThrows(IndexOutOfBoundsException.class, () -> array.isNull(0));
    }

    @Test
    public void testValues() {
        JsonArray array = new JsonArrayWrapperImpl(ahodanenok.json.value.JsonArray.builder()
            .add(ahodanenok.json.value.JsonString.of("test"))
            .add(ahodanenok.json.value.JsonNumber.of(-56789))
            .add(ahodanenok.json.value.JsonBoolean.TRUE)
            .add(ahodanenok.json.value.JsonValue.NULL)
            .add(ahodanenok.json.value.JsonArray.builder()
                .add(ahodanenok.json.value.JsonBoolean.FALSE)
                .add(ahodanenok.json.value.JsonNumber.of(605403))
                .build()
            )
            .build());
        assertEquals(JsonValue.ValueType.ARRAY, array.getValueType());
        assertEquals(5, array.size());
        assertEquals(JsonValue.ValueType.STRING, array.get(0).getValueType());
        assertEquals(JsonValue.ValueType.NUMBER, array.get(1).getValueType());
        assertEquals(JsonValue.ValueType.TRUE, array.get(2).getValueType());
        assertEquals(JsonValue.ValueType.NULL, array.get(3).getValueType());
        assertEquals(JsonValue.ValueType.ARRAY, array.get(4).getValueType());
        assertEquals("test", array.getJsonString(0).getString());
        assertEquals(-56789, array.getJsonNumber(1).intValue());
        assertEquals(2, array.getJsonArray(4).size());
        assertEquals(false, array.getJsonArray(4).getBoolean(0));
        assertEquals(605403, array.getJsonArray(4).getInt(1));
        assertEquals(5, array.getValuesAs(JsonString.class).size());
        assertEquals("test", array.getValuesAs(JsonString.class).get(0).getString());
        assertThrows(ClassCastException.class, () -> array.getValuesAs(JsonString.class).get(1).getString());
        assertEquals("test", array.getString(0));
        assertEquals("test", array.getString(0, "data"));
        assertEquals(-56789, array.getInt(1));
        assertEquals(-56789, array.getInt(1, 1234));
        assertEquals(true, array.getBoolean(2));
        assertEquals(true, array.getBoolean(2, false));
        assertEquals(true, array.isNull(3));
    }
}
