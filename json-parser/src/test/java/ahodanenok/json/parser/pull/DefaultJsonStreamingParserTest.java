package ahodanenok.json.parser.pull;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultJsonStreamingParserTest {

    @Test
    public void testParseNoContent() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(""));
        assertFalse(parser.next());
    }

    @Test
    public void testParseEmptyString() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("\"\""));
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("", parser.getString());
        assertFalse(parser.next());
    }

    @ParameterizedTest
    @CsvSource({
        "\"abc\",     abc",
        "\"hello world\",     hello world"
    })
    public void testParseString(String s, String expected) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals(expected, parser.getString());
        assertFalse(parser.next());
    }

    @ParameterizedTest
    @CsvSource({
        "0.00,     0.0",
        "-12345.6789,     -12345.6789",
        "37e+3,     37000",
        "-5E-4,     -0.0005"
    })
    public void testParseNumber(String s, double expected) {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader(s));
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(expected, parser.getDouble());
        assertFalse(parser.next());
    }

    @Test
    public void testParseNull() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("null"));
        assertTrue(parser.next());
        assertEquals(EventType.NULL, parser.currentEvent());
        assertTrue(parser.isNull());
        assertFalse(parser.next());
    }

    @Test
    public void testParseTrue() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("true"));
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertFalse(parser.next());
    }

    @Test
    public void testParseFalse() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("false"));
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(false, parser.getBoolean());
        assertFalse(parser.next());
    }

    @Test
    public void testParseEmptyArray() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("[]"));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertFalse(parser.next());
    }

    @Test
    public void testParseArrayOneLevel() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("[1.23, true, \"true\", null]"));
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(1.23, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("true", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.NULL, parser.currentEvent());
        assertEquals(true, parser.isNull());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertFalse(parser.next());
    }

    @Test
    public void testParseArrayNested() {
        JsonStreamingParser parser = new DefaultJsonStreamingParser(new StringReader("""
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
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(false, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(500, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NULL, parser.currentEvent());
        assertEquals(true, parser.isNull());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BOOLEAN, parser.currentEvent());
        assertEquals(true, parser.getBoolean());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("x", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.STRING, parser.currentEvent());
        assertEquals("yz", parser.getString());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.BEGIN_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.NUMBER, parser.currentEvent());
        assertEquals(-2.34, parser.getDouble());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertTrue(parser.next());
        assertEquals(EventType.END_ARRAY, parser.currentEvent());
        assertFalse(parser.next());
    }
}
