package ahodanenok.json.writer;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;

public interface JsonOutput extends Closeable {

    void writeBeginArray() throws IOException;

    void writeEndArray() throws IOException;

    void writeBeginObject() throws IOException;

    void writeEndObject() throws IOException;

    void writeValueSeparator() throws IOException;

    void writeNameSeparator() throws IOException;

    void writeString(String s) throws IOException;

    void writeNumber(int n) throws IOException;

    void writeNumber(long n) throws IOException;

    void writeNumber(double n) throws IOException;

    void writeNumber(BigInteger n) throws IOException;

    void writeNumber(BigDecimal n) throws IOException;

    void writeBoolean(boolean b) throws IOException;

    void writeNull() throws IOException;

    void writeRaw(String s) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;
}
