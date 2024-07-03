package ahodanenok.json.tokenizer;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
