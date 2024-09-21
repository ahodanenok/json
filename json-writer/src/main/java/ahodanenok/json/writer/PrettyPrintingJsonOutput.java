package ahodanenok.json.writer;

import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.LinkedList;

public final class PrettyPrintingJsonOutput implements JsonOutput {

    private static class WriteContext {

        WriteContextType type;
        int pos;
        boolean nameSeparatorSeen;
    }

    private final JsonOutput out;
    private final LinkedList<WriteContext> contexts;

    private String whitespace;
    private String indent;
    private String lineSeparator;

    public PrettyPrintingJsonOutput(JsonOutput out) {
        this.out = out;
        this.contexts = new LinkedList<>();
        this.whitespace = " ";
        this.indent = "  ";
        this.lineSeparator = System.lineSeparator();

        WriteContext context = new WriteContext();
        context.type = WriteContextType.ROOT;
        this.contexts.push(context);
    }

    public void setWhitespace(String whitespace) {
        this.whitespace = whitespace;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void writeBeginArray() throws IOException {
        writeBeforeValue();

        WriteContext context = new WriteContext();
        context.type = WriteContextType.ARRAY;
        contexts.push(context);

        out.writeBeginArray();
    }

    @Override
    public void writeEndArray() throws IOException {
        WriteContext context = contexts.pop();
        if (context.pos > 0) {
            writeOnNewLine();
        }

        out.writeEndArray();
    }

    @Override
    public void writeBeginObject() throws IOException {
        writeBeforeValue();

        WriteContext context = new WriteContext();
        context.type = WriteContextType.OBJECT;
        contexts.push(context);

        out.writeBeginObject();
    }

    @Override
    public void writeEndObject() throws IOException {
        WriteContext context = contexts.pop();
        if (context.pos > 0) {
            writeOnNewLine();
        }

        out.writeEndObject();
    }

    @Override
    public void writeValueSeparator() throws IOException {
        out.writeValueSeparator();
    }

    @Override
    public void writeNameSeparator() throws IOException {
        WriteContext context = contexts.peek();
        context.nameSeparatorSeen = true;

        out.writeNameSeparator();
    }

    @Override
    public void writeString(String s) throws IOException {
        writeBeforeValue();
        out.writeString(s);
    }

    @Override
    public void writeNumber(int n) throws IOException {
        writeBeforeValue();
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(long n) throws IOException {
        writeBeforeValue();
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(double n) throws IOException {
        writeBeforeValue();
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(BigInteger n) throws IOException {
        writeBeforeValue();
        out.writeNumber(n);
    }

    @Override
    public void writeNumber(BigDecimal n) throws IOException {
        writeBeforeValue();
        out.writeNumber(n);
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        writeBeforeValue();
        out.writeBoolean(b);
    }

    @Override
    public void writeNull() throws IOException {
        writeBeforeValue();
        out.writeNull();
    }

    @Override
    public void writeRaw(String s) throws IOException {
        out.writeRaw(s);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    private void writeBeforeValue() throws IOException {
        WriteContext context = contexts.peek();
        if (context.type == WriteContextType.ARRAY) {
            writeOnNewLine();
        } else if (context.type == WriteContextType.OBJECT && !context.nameSeparatorSeen) {
            writeOnNewLine();
        } else if (context.type == WriteContextType.OBJECT && context.nameSeparatorSeen) {
            out.writeRaw(whitespace);
        }

        context.nameSeparatorSeen = false;
        context.pos++;
    }

    private void writeOnNewLine() throws IOException {
        out.writeRaw(lineSeparator);
        for (int i = 0; i < contexts.size() - 1; i++) {
            out.writeRaw(indent);
        }
    }
}
