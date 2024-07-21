package ahodanenok.json.writer;

public interface JsonStreamingWriter {

    void writeBeginArray();

    void writeBeginObject();

    void writeEnd();

    void writeName(String name);

    void writeString(String str);

    void writeNumber(double num);

    void writeBoolean(boolean bool);

    void writeNull();
}
