package ahodanenok.json.writer;

import java.math.BigInteger;
import java.math.BigDecimal;

public interface JsonStreamingWriter {

    void writeBeginArray();

    void writeBeginObject();

    void writeEnd();

    void writeName(String name);

    void writeString(String str);

    void writeNumber(int num);

    void writeNumber(long num);

    void writeNumber(double num);

    void writeNumber(BigInteger num);

    void writeNumber(BigDecimal num);

    void writeBoolean(boolean bool);

    void writeNull();

    void close();
}
