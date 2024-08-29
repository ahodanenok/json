package ahodanenok.json.writer;

import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;

// todo: implement
public final class PrettyPrintingJsonOutput implements JsonOutput {

    private final JsonOutput out;

    public PrettyPrintingJsonOutput(JsonOutput out) {
        this.out = out;
    }

    @Override
    public void writeBeginArray() throws IOException {
        out.writeBeginArray();
    }

    @Override
    public void writeEndArray() throws IOException {
        out.writeEndArray();
    }

    @Override
    public void writeBeginObject() throws IOException {
        out.writeBeginObject();
    }

    @Override
    public void writeEndObject() throws IOException {
        out.writeEndObject();
    }

    @Override
    public void writeValueSeparator() throws IOException {
        out.writeValueSeparator();
    }

    @Override
    public void writeNameSeparator() throws IOException {
        out.writeNameSeparator();
    }

    @Override
    public void writeString(String s) throws IOException {
        out.writeString(s);
    }

    @Override
    public void writeNumber(int n) throws IOException {
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(long n) throws IOException {
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(double n) throws IOException {
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(BigInteger n) throws IOException {
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(BigDecimal n) throws IOException {
        out.writeNumber(n);
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        out.writeBoolean(b);
    }

    @Override
    public void writeNull() throws IOException {
        out.writeNull();
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
