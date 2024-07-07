package ahodanenok.json.parser.tokenizer;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.IOException;
import java.nio.CharBuffer;

import ahodanenok.json.parser.JsonParseException;

public final class DefaultJsonTokenizer implements JsonTokenizer {

    private final PushbackReader reader;
    private JsonToken token;
    private CharBuffer buf;

    private int row;
    private int col;
    private int pos;

    private boolean halted;

    public DefaultJsonTokenizer(Reader reader) {
        // todo: check if already pushback
        this.reader = new PushbackReader(reader, 1);
        this.buf = CharBuffer.allocate(128); // todo: configuratble initial capacity?
    }

    @Override
    public boolean advance() {
        if (halted) {
            throw new IllegalStateException("Tokenizer was halted and can't be used anymore");
        }

        try {
            return doAdvance();
        } catch (IOException e) {
            JsonParseState state = halt();
            throw new JsonParseException("Failed to read a character", state, e);
        }
    }

    private boolean doAdvance() throws IOException {
        int ch;
        while (true) {
            ch = reader.read();
            if (ch == -1) {
                token = null;
                return false;
            }

            if (!isWhitespace(ch)) {
                break;
            }

            updateLocation(ch);
        }

        if (ch == 0x5B) { // [
            updateLocation(ch);
            token = JsonToken.BEGIN_ARRAY;
        } else if (ch == 0x5D) { // ]
            updateLocation(ch);
            token = JsonToken.END_ARRAY;
        } else if (ch == 0x7B) { // {
            updateLocation(ch);
            token = JsonToken.BEGIN_OBJECT;
        } else if (ch == 0x7D) { // }
            updateLocation(ch);
            token = JsonToken.END_OBJECT;
        } else if (ch == 0x3A) { // :
            updateLocation(ch);
            token = JsonToken.NAME_SEPARATOR;
        } else if (ch == 0x2C) { // ,
            updateLocation(ch);
            token = JsonToken.VALUE_SEPARATOR;
        } else if (ch == 0x74) { // t
            updateLocation(ch);
            expectNext("rue", ch);
            token = JsonToken.TRUE;
        } else if (ch == 0x66) { // f
            updateLocation(ch);
            expectNext("alse", ch);
            token = JsonToken.FALSE;
        } else if (ch == 0x6E) { // n
            updateLocation(ch);
            expectNext("ull", ch);
            token = JsonToken.NULL;
        } else if (ch == 0x22) { // "
            updateLocation(ch);
            token = new JsonStringToken(TokenType.STRING, readString());
        } else {
            reader.unread(ch);
            token = new JsonDoubleToken(TokenType.NUMBER, readNumber());
        }

        return true;
    }

    private boolean isWhitespace(int ch) {
        return ch == 0x20 // Space
            || ch == 0x9  // Horizontal tab
            || ch == 0xA  // Line feed or New line
            || ch == 0xD; // Carriage return
    }

    private void updateLocation(int ch) {
        if (ch == -1) {
            return;
        }

        pos++;
        col++;
        // todo: make line separator configurable
        // todo: allow for line separators of multiple characters (\r\n)
        if (ch == '\n') {
            row++;
            col = 0;
        }
    }

    private void expectNext(String tail, int head) throws IOException {
        int ch;
        for (int i = 0, n = tail.length(); i < n; i++) {
            ch = reader.read();
            updateLocation(ch);
            if (ch == -1 ) {
                JsonParseState state = halt();
                throw new JsonParseException(
                    String.format("Unexpected end of text while '%c%s' was expected", head, tail),
                    state);
            } else if (ch != tail.charAt(i)) {
                JsonParseState state = halt();
                throw new JsonParseException(
                    String.format("Unexpected character '%c' while '%c%s' was expected", ch, head, tail),
                    state);
            }
        }
    }

    // todo: implement escapes
    private String readString() throws IOException {
        buf.clear();

        int ch;
        while (true) {
            ch = reader.read();
            // todo: throw an error if a control character is encountered (x00-x1F)
            updateLocation(ch);
            if (ch == -1 || ch == 0x22) {
                break;
            }

            if (buf.remaining() == 0) {
                // todo: configurable string size limit?
                expandBuf();
                if (buf.remaining() == 0) {
                    JsonParseState state = halt();
                    throw new JsonParseException("String is too long", state);
                }
            }

            buf.put((char) ch);
        }

        if (ch == -1) {
            JsonParseState state = halt();
            throw new JsonParseException(
                "Unexpected end of the string, must be terminated with a double quote", state);
        }

        return buf.flip().toString();
    }

    // todo: config for reading as double/bigdecimal?
    private double readNumber() throws IOException {
        buf.clear();

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

            updateLocation(ch);
            if (buf.remaining() == 0) {
                // todo: configurable number size limit?
                expandBuf();
                if (buf.remaining() == 0) {
                    JsonParseState state = halt();
                    throw new JsonParseException("Number is too long", state);
                }
            }

            buf.put((char) ch);
        }

        if (buf.position() == 0) {
            int n = 7;
            while ((ch = reader.read()) != -1 && n > 0) {
                buf.put((char) ch);
                n--;
            }
            if (ch != -1) {
                buf.put("...");
            }

            JsonParseState state = halt();
            throw new JsonParseException(
                String.format("Unexpected token '%s' at pos %d", buf.flip(), pos),
                state);
        }

        try {
            // todo: implement https://datatracker.ietf.org/doc/html/rfc8259#section-6
            return Double.parseDouble(buf.flip().toString());
        } catch (NumberFormatException e) {
            JsonParseState state = halt();
            throw new JsonParseException(
                String.format("Incorrect number '%s'", buf.toString()), state);
        }
    }

    private void expandBuf() {
        CharBuffer prevBuf = buf;
        buf = CharBuffer.allocate(Math.min(buf.capacity() * 2, Integer.MAX_VALUE - 8));
        buf.put(prevBuf.flip());
    }

    @Override
    public JsonToken currentToken() {
        if (halted) {
            throw new IllegalStateException("Tokenizer was halted and can't be used anymore");
        } else if (token == null) {
            throw new IllegalStateException(
                "There is no token to return. This method could be called only when advance() returned true");
        }

        return token;
    }

    @Override
    public JsonParseLocation currentLocation() {
        return new JsonParseLocation(row, col, pos);
    }

    @Override
    public JsonParseState halt() {
        if (halted) {
            throw new IllegalStateException("Tokenizer was halted and can't be used anymore");
        }

        this.halted = true;
        // todo: collect additional info to make errors more informative
        return new JsonParseState(currentLocation());
    }
}
