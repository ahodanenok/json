package ahodanenok.json.writer;

import java.io.Closeable;
import java.io.IOException;

public interface JsonOutput extends Closeable {

    void writeBeginArray() throws IOException;

    void writeEndArray() throws IOException;

    void writeBeginObject() throws IOException;

    void writeEndObject() throws IOException;

    void writeValueSeparator() throws IOException;

    void writeNameSeparator() throws IOException;

    void writeString(String s) throws IOException;

    void writeNumber(double n) throws IOException;

    void writeBoolean(boolean b) throws IOException;

    void writeNull() throws IOException;

    void close() throws IOException;
}
