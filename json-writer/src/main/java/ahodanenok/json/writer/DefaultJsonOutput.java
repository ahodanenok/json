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
        writer.write(s); // todo: escape
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

    public void close() throws IOException {
        writer.close();
    }
}
