package ahodanenok.json.tokenizer;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DefaultJsonTokenizerTest {

    @Test
    public void testReadEmpty() throws Exception {
        DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new StringReader(""));
        assertFalse(tokenizer.advance());
    }
}
