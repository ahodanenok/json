package ahodanenok.json.parser.tokenizer;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import ahodanenok.json.parser.JsonParseException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultJsonTokenizerTest {

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "\t", "\r", "\n", "\r\t \n" })
    public void testReadEmpty(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { "[", "\t[", "\r[", " [", " \t \r\n \n[", " \n \r[\t " })
    public void testReadBeginArray(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.BEGIN_ARRAY, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { "]", "\t]", "\r]", " ]", " \r\t \n \n]", " \t\n]  " })
    public void testReadEndArray(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.END_ARRAY, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { "{", "\t{", "\r{", " {", " \r\r\t \n {", " \n \r{  \t" })
    public void testReadBeginObject(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.BEGIN_OBJECT, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { "}", "\t}", "\r}", " }", " \r \n\t \n }", " \t\n }  \r " })
    public void testReadEndObject(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.END_OBJECT, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { ":", "\t:", "\r:", " :", " \r \t \n :", " \t \n :  \r " })
    public void testReadNameSeparator(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NAME_SEPARATOR, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { ",", "\t,", "\r,", " ,", " \r \t \n ,", " \t \n ,  \r " })
    public void testReadValueSeparator(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.VALUE_SEPARATOR, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { "true", "\ttrue", "\rtrue", " true", " \r \t \n true", " \t \n true  \r " })
    public void testReadTrue(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.TRUE, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @CsvSource({ "truE, E", "tRue, R", "trUe, U", "trye, y" })
    public void testErrorWhenTrueNotCorrect(String s, char incorrect) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals(String.format("Unexpected character '%c' while 'true' was expected", incorrect), e.getMessage());
        assertNotNull(e.getState());
    }

    @ParameterizedTest
    @ValueSource(strings = { "t", "tr", "tru" })
    public void testErrorWhenTrueUnexpectedEnd(String s) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals("Unexpected end of text while 'true' was expected", e.getMessage());
        assertNotNull(e.getState());
    }

    @ParameterizedTest
    @ValueSource(strings = { "false", "\tfalse", "\rfalse", " false", " \r \t \n false", " \t\n \rfalse  \t " })
    public void testReadFalse(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.FALSE, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @CsvSource({ "falsE, E", "falSe, S", "faLse, L", "fAlse, A", "falsy, y" })
    public void testErrorWhenFalseNotCorrect(String s, char incorrect) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals(String.format("Unexpected character '%c' while 'false' was expected", incorrect), e.getMessage());
        assertNotNull(e.getState());
    }

    @ParameterizedTest
    @ValueSource(strings = { "f", "fa", "fal", "fals" })
    public void testErrorWhenFalseUnexpectedEnd(String s) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals("Unexpected end of text while 'false' was expected", e.getMessage());
        assertNotNull(e.getState());
    }

    @ParameterizedTest
    @ValueSource(strings = { "null", "\tnull", "\rnull", " null", " \r \t \n null", " \t\n \rnull  \t " })
    public void testReadNull(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NULL, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @CsvSource({ "nulL, L", "nuLl, L", "nUll, U", "nul1, 1" })
    public void testErrorWhenNullNotCorrect(String s, char incorrect) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals(String.format("Unexpected character '%c' while 'null' was expected", incorrect), e.getMessage());
        assertNotNull(e.getState());
    }

    @ParameterizedTest
    @ValueSource(strings = { "n", "nu", "nul" })
    public void testErrorWhenNullUnexpectedEnd(String s) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals("Unexpected end of text while 'null' was expected", e.getMessage());
        assertNotNull(e.getState());
    }

    @Test
    public void testReadEmptyString() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("\"\""));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        JsonStringToken token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("", token.stringValue());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testReadString() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("  \"Hello, World\" "));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        JsonStringToken token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("Hello, World", token.stringValue());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testReadLongString() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(" \t \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dictum congue arcu, eget laoreet nisl venenatis non. Nulla facilisi. Sed eget tincidunt neque, quis ullamcorper metus. Ut non nisi sit amet nisi malesuada laoreet a sit amet diam. Fusce dignissim nec sem at lobortis. Quisque dapibus quam in ex convallis condimentum. In euismod massa vitae vulputate gravida. Suspendisse eget dignissim est, quis dignissim massa. Maecenas lobortis dui a ornare sodales.\" \n "));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        JsonStringToken token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dictum congue arcu, eget laoreet nisl venenatis non. Nulla facilisi. Sed eget tincidunt neque, quis ullamcorper metus. Ut non nisi sit amet nisi malesuada laoreet a sit amet diam. Fusce dignissim nec sem at lobortis. Quisque dapibus quam in ex convallis condimentum. In euismod massa vitae vulputate gravida. Suspendisse eget dignissim est, quis dignissim massa. Maecenas lobortis dui a ornare sodales.", token.stringValue());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testReadMultipleStrings() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("  \t\t \"abc\" \r \"org.junit.platform.commons.util.ReflectionUtils.invokeMethod\"  \"  \" \n\n \"test string\" "));
        JsonStringToken token;

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("abc", token.stringValue());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("org.junit.platform.commons.util.ReflectionUtils.invokeMethod", token.stringValue());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("  ", token.stringValue());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("test string", token.stringValue());

        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        "\\"a\\"b\\"c\\"",     \"a\"b\"c\"
        "\\\\a\\\\b\\\\c\\\\",     \\a\\b\\c\\
        "\\/a\\/b\\/c\\/",     /a/b/c/
        "\\bX\\bY\\bZ\\b",     '\bX\bY\bZ\b'
        "\\fX\\fY\\fZ\\f",     '\fX\fY\fZ\f'
        "\\nX\\nY\\nZ\\n",     '\nX\nY\nZ\n'
        "\\rX\\rY\\rZ\\r",     '\rX\rY\rZ\r'
        "\\tX\\tY\\tZ\\t",     '\tX\tY\tZ\t'
        "\\"\\\\\\/\\b\\f\\n\\r\\t",     '\"\\/\b\f\n\r\t'
        "=\\"=\\\\=\\/=\\b=\\f=\\n=\\r=\\t=",     '=\"=\\=/=\b=\f=\n=\r=\t='
    """)
    public void testReadStringWithEscapes(String s, String expected) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        assertEquals(expected, tokenizer.currentToken().stringValue());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        "\\u2160\\uFE10\\uab35",     '\u2160\uFE10\uab35'
        "A\\u3a5CB\\uefA0C\\u0012D",     'A\u3A5CB\uEFA0C\u0012D'
    """)
    public void testReadStringWithUnicodeEscape(String s, String expected) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        assertEquals(expected, tokenizer.currentToken().stringValue());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        \"A=\\uD801\\uDC37+D\",     'A=\uD801\uDC37+D'
        \"\\uD852\\uDF62\",     '\uD852\uDF62'
    """)
    public void testReadStringWithSurrogates(String s, String expected) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        assertEquals(expected, tokenizer.currentToken().stringValue());
    }

    @Test
    public void testReadStringIgnoreEscapedUnicodeEscape() {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("\"\\\\uD2B1--\\\\uABCD--\\\\u1234\""));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        assertEquals("\\uD2B1--\\uABCD--\\u1234", tokenizer.currentToken().stringValue());
    }

    @ParameterizedTest
    @CsvSource({
        "\"AB\\c\",     \\c",
        "\"3\\21\",     \\2",
        "\"\\U1234\",     \\U"
    })
    public void testReadStringErrorWhenUnsupportedEscape(String s, String expected) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals(String.format("Unsupported escape sequence '%s'", expected), e.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "\"\u0000\",     0x0",
        "\"\u0001\",     0x1",
        "\"\u0002\",     0x2",
        "\"\u0003\",     0x3",
        "\"\u0004\",     0x4",
        "\"\u0005\",     0x5",
        "\"\u0006\",     0x6",
        "\"\u0007\",     0x7",
        "\"\u0008\",     0x8",
        "\"\u0009\",     0x9",
        "'\"\n\"',       0xA",
        "\"\u000B\",     0xB",
        "\"\u000C\",     0xC",
        "'\"\r\"',       0xD",
        "\"\u000E\",     0xE",
        "\"\u000F\",     0xF",
        "\"\u0010\",     0x10",
        "\"\u0011\",     0x11",
        "\"\u0012\",     0x12",
        "\"\u0013\",     0x13",
        "\"\u0014\",     0x14",
        "\"\u0015\",     0x15",
        "\"\u0016\",     0x16",
        "\"\u0017\",     0x17",
        "\"\u0018\",     0x18",
        "\"\u0019\",     0x19",
        "\"\u001A\",     0x1A",
        "\"\u001B\",     0x1B",
        "\"\u001C\",     0x1C",
        "\"\u001D\",     0x1D",
        "\"\u001E\",     0x1E",
        "\"\u001F\",     0x1F"
    })
    public void testReadStringErrorWhenUnescapedControlCharacter(String s, String expected) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals(String.format("Control charcter '%s' must be escaped", expected), e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "\"abc\\",
        "\"=\\uABC",
        "\"\\uD852\\uDF6",
        "\"\\uD852\\uDF",
        "\"\\uD852\\uD",
        "\"\\uD852\\u",
        "\"\\uD852\\",
        "\"\\uD852"
    })
    public void testReadStringErrorWhenEscapeUnfinished(String s) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals("Unexpected end of the string while reading an escape sequence", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "\"\\uABCH\",     H",
        "\"\\u01ZD\",     Z",
        "\"\\ub!23\",     !",
        "\"\\un234\",     n"
    })
    public void testReadStringErrorWhenNotHexDigitInUnicodeEscape(String s, String expected) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals(String.format("Incorrect unicode escape sequence: '%s' is not a hex digit", expected), e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "\"\\uD852\\\"",
        "\"\\uD852\""
    })
    public void testReadStringErrorWhenUnexpectedCharacterInLowSurrogate(String s) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals("Unexpected character '\"' while a unicode escape sequence was expected", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "\"", "\"abc", "\"hello world" })
    public void testErrorWhenStringNotTerminated(String s) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals("Unexpected end of the string, must be terminated with a double quote", e.getMessage());
        assertNotNull(e.getState());
    }

    @ParameterizedTest
    @CsvSource({
        "0,  0.0",
        "-0,  -0.0",
        "0.0,  0.0",
        "-0.000,  -0.0",
        "0e+100,  0.0",
        "1,  1.0",
        "-1,  -1.0",
        "-1e5,  -100000",
        "-1e-5,  -0.00001",
        "123456.7890,  123456.7890",
        "-123456.7890,  -123456.7890",
        "52.378E+2,  5237.8",
        "-731236.378E-4,  -73.1236378",
        "321,  321",
        "-321,  -321"
    })
    public void testReadNumber(String s, double n) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NUMBER, tokenizer.currentToken().getType());
        JsonDoubleToken token = assertInstanceOf(JsonDoubleToken.class, tokenizer.currentToken());
        assertEquals(n, token.doubleValue());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "true,falsenull:[}-7.3456e+2]\"abc def\"{",
        " true\t,\r\nfalse    null\t\t\t:\r[ }\n-7.3456e+2 \r ] \"abc def\"\t{\n"
    })
    public void testReadMultiple(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.TRUE, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.VALUE_SEPARATOR, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.FALSE, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NULL, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NAME_SEPARATOR, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.BEGIN_ARRAY, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.END_OBJECT, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NUMBER, tokenizer.currentToken().getType());
        assertEquals(-734.56, assertInstanceOf(JsonDoubleToken.class, tokenizer.currentToken()).doubleValue());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.END_ARRAY, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        assertEquals("abc def", assertInstanceOf(JsonStringToken.class, tokenizer.currentToken()).stringValue());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.BEGIN_OBJECT, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testUnusableWhenHalted() {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("[123]"));
        assertTrue(tokenizer.advance());
        tokenizer.halt();

        IllegalStateException e;
        e = assertThrows(IllegalStateException.class, () -> tokenizer.currentToken());
        assertEquals("Tokenizer was halted and can't be used anymore", e.getMessage());
        e = assertThrows(IllegalStateException.class, () -> tokenizer.advance());
        assertEquals("Tokenizer was halted and can't be used anymore", e.getMessage());
        e = assertThrows(IllegalStateException.class, () -> tokenizer.halt());
        assertEquals("Tokenizer was halted and can't be used anymore", e.getMessage());
        assertDoesNotThrow(() -> tokenizer.currentLocation());
    }

    @Test
    public void testErrorWhenNotAdvanced() {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("1"));

        IllegalStateException e;
        e = assertThrows(IllegalStateException.class, () -> tokenizer.currentToken());
        assertEquals("There is no token to return. This method could be called only when advance() returned true", e.getMessage());
        assertTrue(tokenizer.advance());
        assertDoesNotThrow(() -> tokenizer.currentToken());
        assertFalse(tokenizer.advance());
        e = assertThrows(IllegalStateException.class, () -> tokenizer.currentToken());
        assertEquals("There is no token to return. This method could be called only when advance() returned true", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "abc, abc",
        "hello-world, hello-w...",
        "True, True",
        "False, False",
        "Null, Null",
        "=12345678, =123456...",
        "check\": \"abc\", check\":..."
    })
    public void testErrorWhenUnexpectedToken(String s, String incorrect) {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        JsonParseException e = assertThrows(JsonParseException.class, () -> tokenizer.advance());
        assertEquals(String.format("Unexpected token '%s' at pos 0", incorrect), e.getMessage());
        assertNotNull(e.getState());
    }

    @Test
    public void testLocation() {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(
            "\t{\n"
            + "}  true \n"
            + "false ][\n"
            + "\n"
            + "\n"
            + "123\n"
            + "null :\n"));

        assertEquals(0, tokenizer.currentLocation().getRow());
        assertEquals(0, tokenizer.currentLocation().getColumn());
        assertEquals(0, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.BEGIN_OBJECT, tokenizer.currentToken().getType());
        assertEquals(0, tokenizer.currentLocation().getRow());
        assertEquals(2, tokenizer.currentLocation().getColumn());
        assertEquals(2, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.END_OBJECT, tokenizer.currentToken().getType());
        assertEquals(1, tokenizer.currentLocation().getRow());
        assertEquals(1, tokenizer.currentLocation().getColumn());
        assertEquals(4, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.TRUE, tokenizer.currentToken().getType());
        assertEquals(1, tokenizer.currentLocation().getRow());
        assertEquals(7, tokenizer.currentLocation().getColumn());
        assertEquals(10, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.FALSE, tokenizer.currentToken().getType());
        assertEquals(2, tokenizer.currentLocation().getRow());
        assertEquals(5, tokenizer.currentLocation().getColumn());
        assertEquals(17, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.END_ARRAY, tokenizer.currentToken().getType());
        assertEquals(2, tokenizer.currentLocation().getRow());
        assertEquals(7, tokenizer.currentLocation().getColumn());
        assertEquals(19, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.BEGIN_ARRAY, tokenizer.currentToken().getType());
        assertEquals(2, tokenizer.currentLocation().getRow());
        assertEquals(8, tokenizer.currentLocation().getColumn());
        assertEquals(20, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NUMBER, tokenizer.currentToken().getType());
        assertEquals(5, tokenizer.currentLocation().getRow());
        assertEquals(3, tokenizer.currentLocation().getColumn());
        assertEquals(26, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NULL, tokenizer.currentToken().getType());
        assertEquals(6, tokenizer.currentLocation().getRow());
        assertEquals(4, tokenizer.currentLocation().getColumn());
        assertEquals(31, tokenizer.currentLocation().getPosition());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NAME_SEPARATOR, tokenizer.currentToken().getType());
        assertEquals(6, tokenizer.currentLocation().getRow());
        assertEquals(6, tokenizer.currentLocation().getColumn());
        assertEquals(33, tokenizer.currentLocation().getPosition());

        assertFalse(tokenizer.advance());
        assertEquals(7, tokenizer.currentLocation().getRow());
        assertEquals(0, tokenizer.currentLocation().getColumn());
        assertEquals(34, tokenizer.currentLocation().getPosition());
    }
}
