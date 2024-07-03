package ahodanenok.json.tokenizer;

import java.io.Reader;

public final class DefaultJsonTokenizer implements JsonTokenizer {

    private final Reader reader;
    private JsonToken token;

    public DefaultJsonTokenizer(Reader reader) {
        this.reader = reader;
    }

    @Override
    public boolean advance() {
        try {
            return doAdvance();
        } catch (Exception e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    private boolean doAdvance() throws Exception {
        int ch;
        do {
            ch = reader.read();
        } while (ch != -1 && isWhitespace(ch));

        if (ch == -1) {
            token = null;
            return false;
        }

        if (ch == 0x5B) { // [
            token = JsonToken.BEGIN_ARRAY;
        } else if (ch == 0x5D) { // ]
            token = JsonToken.END_ARRAY;
        } else if (ch == 0x7B) { // {
            token = JsonToken.BEGIN_OBJECT;
        } else if (ch == 0x7D) { // }
            token = JsonToken.END_OBJECT;
        } else if (ch == 0x3A) { // :
            token = JsonToken.NAME_SEPARATOR;
        } else if (ch == 0x2C) { // ,
            token = JsonToken.VALUE_SEPARATOR;
        } else if (ch == 0x74) { // t
            expectNext("rue");
            token = JsonToken.TRUE;
        } else if (ch == 0x66) { // f
            expectNext("alse");
            token = JsonToken.FALSE;
        } else if (ch == 0x6E) { // n
            expectNext("ull");
            token = JsonToken.NULL;
        } else {
            // todo: custom exception?
            throw new IllegalStateException("unknown char: " + ch);
        }

        return true;
    }

    private boolean isWhitespace(int ch) {
        return ch == 0x20 // Space
            || ch == 0x9  // Horizontal tab
            || ch == 0xA  // Line feed or New line
            || ch == 0xD; // Carriage return
    }

    private void expectNext(String s) throws Exception {
        int ch;
        for (int i = 0, n = s.length(); i < n; i++) {
            ch = reader.read();
            if (ch == -1 || ch != s.charAt(i)) {
                // todo: custom exception
                throw new IllegalStateException("unexpected character");
            }
        }

        ch = reader.read();
        if (ch != -1 && !isWhitespace(ch)) {
            // todo: custom exception
            throw new IllegalStateException("unexpected character");
        }
    }

    @Override
    public JsonToken currentToken() {
        if (token == null) {
            // todo: custom exception
            throw new IllegalStateException("no token");
        }

        return token;
    }
}
