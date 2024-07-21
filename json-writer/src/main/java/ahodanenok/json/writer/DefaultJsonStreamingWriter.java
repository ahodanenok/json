package ahodanenok.json.writer;

import java.io.IOException;

public final class DefaultJsonStreamingWriter implements JsonStreamingWriter {

    private final JsonOutput output;

    public DefaultJsonStreamingWriter(JsonOutput output) {
        this.output = output;
    }

    @Override
    public void writeBeginArray() {
        // todo: check valid
        try {
            output.writeBeginArray();
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBeginObject() {
        // todo: check valid
        try {
            output.writeBeginObject();
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeEnd() {
        // todo: check valid
        // todo: impl
    }

    @Override
    public void writeName(String name) {
        // todo: check valid
        try {
            output.writeString(name);
            output.writeNameSeparator();
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeString(String str) {
        // todo: check valid
        try {
            output.writeString(str);
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeNumber(double num) {
        // todo: check valid
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBoolean(boolean bool) {
        // todo: check valid
        try {
            output.writeBoolean(bool);
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeNull() {
        // todo: check valid
        try {
            output.writeNull();
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }
}