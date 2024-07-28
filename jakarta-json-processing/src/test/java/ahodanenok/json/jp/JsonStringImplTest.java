package ahodanenok.json.jp;

import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class JsonStringImplTest {

    @Test
    public void testEmptyString() {
        JsonString string = new JsonStringImpl("");
        assertEquals(JsonValue.ValueType.STRING, string.getValueType());
        assertEquals("", string.getString());
        assertEquals("", string.getChars());
    }

    @Test
    public void testString() {
        JsonString string = new JsonStringImpl("json string");
        assertEquals(JsonValue.ValueType.STRING, string.getValueType());
        assertEquals("json string", string.getString());
        assertEquals("json string", string.getChars());
    }

    @ParameterizedTest
    @CsvSource({
        "'',    ''",
        "'abc',    'abc'",
        "'HELLO WORLD',     'HELLO WORLD'"
    })
    public void testEqual(String a, String b) {
        JsonString string_1 = new JsonStringImpl(a);
        JsonString string_2 = new JsonStringImpl(b);
        assertEquals(string_1, string_2);
        assertEquals(string_1.hashCode(), string_2.hashCode());
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(new JsonStringImpl(""), new JsonStringImpl(" "));
        assertNotEquals(new JsonStringImpl("A"), new JsonStringImpl("a"));
        assertNotEquals(new JsonStringImpl("what"), new JsonStringImpl("whaT"));
    }
}
