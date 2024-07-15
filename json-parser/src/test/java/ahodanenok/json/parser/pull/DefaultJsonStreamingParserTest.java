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
}
