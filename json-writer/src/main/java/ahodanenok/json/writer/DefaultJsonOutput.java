package ahodanenok.json.writer;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.math.BigDecimal;

public final class DefaultJsonOutput implements JsonOutput {

    private final Writer writer;

    public DefaultJsonOutput(Writer writer) {
        this.writer = writer;
    }

    public void writeBeginArray() throws IOException {
        writer.write('[');
    }

    public void writeEndArray() throws IOException {
        writer.write(']');
    }

    public void writeBeginObject() throws IOException {
        writer.write('{');
    }

    public void writeEndObject() throws IOException {
        writer.write('}');
    }

    public void writeValueSeparator() throws IOException {
        writer.write(',');
    }

    public void writeNameSeparator() throws IOException {
        writer.write(':');
    }

    public void writeString(String s) throws IOException {
        writer.write('"');
        char ch;
        for (int i = 0, n = s.length(); i < n; i++) {
            ch = s.charAt(i);
            if (ch == '"') {
                writer.write('\\');
                writer.write('"');
            } else if (ch == '\\') {
                writer.write('\\');
                writer.write('\\');
            } else if (ch < 0x20) {
                if (ch == '\b') {
                    writer.write('\\');
                    writer.write('b');
                } else if (ch == '\f') {
                    writer.write('\\');
                    writer.write('f');
                } else if (ch == '\n') {
                    writer.write('\\');
                    writer.write('n');
                } else if (ch == '\r') {
                    writer.write('\\');
                    writer.write('r');
                } else if (ch == '\t') {
                    writer.write('\\');
                    writer.write('t');
                } else {
                    writer.write(String.format("\\u%04x", (int) ch));
                }
            } else {
                writer.write(ch);
            }
        }
        writer.write('"');
    }

    public void writeNumber(int n) throws IOException {
        writer.write(Integer.toString(n));
    }

    public void writeNumber(long n) throws IOException {
        writer.write(Long.toString(n));
    }

    public void writeNumber(double n) throws IOException {
        writer.write(Double.toString(n));
    }

    public void writeNumber(BigInteger n) throws IOException {
        writer.write(n.toString());
    }

    public void writeNumber(BigDecimal n) throws IOException {
        writer.write(n.toString());
    }

    public void writeBoolean(boolean b) throws IOException {
        writer.write(b ? "true" : "false");
    }

    public void writeNull() throws IOException {
        writer.write("null");
    }

    @Override
    public void writeRaw(String s) throws IOException {
        writer.write(s);
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }
}
