package ahodanenok.json.parser.tokenizer;

import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

public final class DefaultJsonTokenizer implements JsonTokenizer {

    private final PushbackReader reader;
    private JsonToken token;
    private CharBuffer buf;

    public DefaultJsonTokenizer(Reader reader) {
        // todo: check if already pushback
        this.reader = new PushbackReader(reader, 1);
        this.buf = CharBuffer.allocate(128); // todo: configuratble initial capacity?
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
        } else if (ch == 0x22) { // "
            token = new JsonStringToken(TokenType.STRING, readString());
        } else {
            token = new JsonDoubleToken(TokenType.NUMBER, readNumber(ch));
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

        // ch = reader.read();
        // if (ch != -1 && !isWhitespace(ch)) {
        //     // todo: custom exception
        //     throw new IllegalStateException("unexpected character");
        // }
    }

    // todo: implement escapes
    private String readString() throws Exception {
        buf.clear();

        int ch;
        while (true) {
            ch = reader.read();
            if (ch == -1 || ch == 0x22) {
                break;
            }

            if (buf.remaining() == 0) {
                // todo: configurable string size limit?
                expandBuf();
                if (buf.remaining() == 0) {
                    // todo: custom exception
                    throw new IllegalStateException("string too long");
                }
            }

            buf.put((char) ch);
        }

        if (ch == -1) {
            // todo: custom exception
            throw new IllegalStateException("unexpected end of string");
        }

        return buf.flip().toString();
    }

    // todo: config for reading as double/bigdecimal?
    private double readNumber(int initialCh) throws Exception {
        buf.clear();
        buf.put((char) initialCh);

        int ch;
        while (true) {
            ch = reader.read();
            if (ch == -1) {
                break;
            } else if (isWhitespace(ch)
                    // todo: temporary while number reading is not implemented
                    || !(ch >= '0' && ch <= '9' || ch == '+' || ch == '-' || ch == 'e' || ch == 'E' || ch == '.')) {
                reader.unread(ch);
                break;
            }

            if (buf.remaining() == 0) {
                // todo: configurable number size limit?
                expandBuf();
                if (buf.remaining() == 0) {
                    // todo: custom exception
                    throw new IllegalStateException("number too long");
                }
            }

            buf.put((char) ch);
        }

        // todo: implement https://datatracker.ietf.org/doc/html/rfc8259#section-6
        return Double.parseDouble(buf.flip().toString());
    }

    private void expandBuf() {
        CharBuffer prevBuf = buf;
        buf = CharBuffer.allocate(Math.min(buf.capacity() * 2, Integer.MAX_VALUE - 8));
        buf.put(prevBuf.flip());
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
