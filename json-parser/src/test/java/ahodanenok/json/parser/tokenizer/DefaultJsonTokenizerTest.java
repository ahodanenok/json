package ahodanenok.json.parser.tokenizer;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
    @ValueSource(strings = { "false", "\tfalse", "\rfalse", " false", " \r \t \n false", " \t\n \rfalse  \t " })
    public void testReadFalse(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.FALSE, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = { "null", "\tnull", "\rnull", " null", " \r \t \n null", " \t\n \rnull  \t " })
    public void testReadNull(String s) throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(s));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.NULL, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testReadEmptyString() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("\"\""));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        JsonStringToken token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("", token.getValue());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testReadString() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("  \"Hello, World\" "));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        JsonStringToken token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("Hello, World", token.getValue());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testReadLongString() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(" \t \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dictum congue arcu, eget laoreet nisl venenatis non. Nulla facilisi. Sed eget tincidunt neque, quis ullamcorper metus. Ut non nisi sit amet nisi malesuada laoreet a sit amet diam. Fusce dignissim nec sem at lobortis. Quisque dapibus quam in ex convallis condimentum. In euismod massa vitae vulputate gravida. Suspendisse eget dignissim est, quis dignissim massa. Maecenas lobortis dui a ornare sodales.\" \n "));
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        JsonStringToken token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dictum congue arcu, eget laoreet nisl venenatis non. Nulla facilisi. Sed eget tincidunt neque, quis ullamcorper metus. Ut non nisi sit amet nisi malesuada laoreet a sit amet diam. Fusce dignissim nec sem at lobortis. Quisque dapibus quam in ex convallis condimentum. In euismod massa vitae vulputate gravida. Suspendisse eget dignissim est, quis dignissim massa. Maecenas lobortis dui a ornare sodales.", token.getValue());
        assertFalse(tokenizer.advance());
    }

    @Test
    public void testReadMultipleStrings() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader("  \t\t \"abc\" \r \"org.junit.platform.commons.util.ReflectionUtils.invokeMethod\"  \"  \" \n\n \"test string\" "));
        JsonStringToken token;

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("abc", token.getValue());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("org.junit.platform.commons.util.ReflectionUtils.invokeMethod", token.getValue());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("  ", token.getValue());

        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        token = assertInstanceOf(JsonStringToken.class, tokenizer.currentToken());
        assertEquals("test string", token.getValue());

        assertFalse(tokenizer.advance());
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
        assertEquals(n, token.getValue());
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
        assertEquals(-734.56, assertInstanceOf(JsonDoubleToken.class, tokenizer.currentToken()).getValue());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.END_ARRAY, tokenizer.currentToken().getType());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.STRING, tokenizer.currentToken().getType());
        assertEquals("abc def", assertInstanceOf(JsonStringToken.class, tokenizer.currentToken()).getValue());
        assertTrue(tokenizer.advance());
        assertEquals(TokenType.BEGIN_OBJECT, tokenizer.currentToken().getType());
        assertFalse(tokenizer.advance());
    }
}
