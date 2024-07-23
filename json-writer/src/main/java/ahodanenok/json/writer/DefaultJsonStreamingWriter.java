package ahodanenok.json.writer;

import java.io.IOException;
import java.util.LinkedList;

public final class DefaultJsonStreamingWriter implements JsonStreamingWriter {

    private enum ContextType {
        ROOT, ARRAY, OBJECT;
    }

    private class WriteContext {
        ContextType type;
        int valuePos;
        boolean nameWritten;
    }

    private final JsonOutput output;
    private LinkedList<WriteContext> contexts;

    public DefaultJsonStreamingWriter(JsonOutput output) {
        this.output = output;
        this.contexts = new LinkedList<>();

        WriteContext context = new WriteContext();
        context.type = ContextType.ROOT;
        this.contexts.push(context);
    }

    @Override
    public void writeBeginArray() {
        prepareWriteOnValue();

        WriteContext arrayContext = new WriteContext();
        arrayContext.type = ContextType.ARRAY;
        contexts.push(arrayContext);

        try {
            output.writeBeginArray();
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBeginObject() {
        prepareWriteOnValue();

        WriteContext arrayContext = new WriteContext();
        arrayContext.type = ContextType.OBJECT;
        contexts.push(arrayContext);

        try {
            output.writeBeginObject();
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeEnd() {
        WriteContext context = contexts.pop();
        if (context.type == ContextType.ARRAY) {
            try {
                output.writeEndArray();
            } catch (IOException e) {
                // todo: custom exception
                throw new RuntimeException(e);
            }
        } else if (context.type == ContextType.OBJECT) {
            try {
                output.writeEndObject();
            } catch (IOException e) {
                // todo: custom exception
                throw new RuntimeException(e);
            }
        } else if (context.type == ContextType.ROOT) {
            // todo: custom exception
            throw new RuntimeException("not an object or array");
        } else {
            throw new IllegalStateException("Unknown context: " + context.type);
        }
    }

    @Override
    public void writeName(String name) {
        WriteContext context = contexts.peek();
        if (context.type != ContextType.OBJECT || context.nameWritten) {
            // todo: custom exception
            throw new RuntimeException();
        }
        prepareWriteOnValue();

        try {
            output.writeString(name);
            output.writeNameSeparator();
            context.nameWritten = true;
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeString(String str) {
        prepareWriteOnValue();
        try {
            output.writeString(str);
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeNumber(double num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBoolean(boolean bool) {
        prepareWriteOnValue();
        try {
            output.writeBoolean(bool);
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeNull() {
        prepareWriteOnValue();
        try {
            output.writeNull();
        } catch (IOException e) {
            // todo: custom exception
            throw new RuntimeException(e);
        }
    }

    private void prepareWriteOnValue() {
        WriteContext context = contexts.peek();
        if (context == null) {
            throw new IllegalStateException("No context!");
        }

        if (context.type == ContextType.ROOT && context.valuePos > 0) {
            // todo: custom exception
            throw new RuntimeException("multiple top-level values");
        } else if (context.type == ContextType.ARRAY && context.valuePos > 0) {
            try {
                output.writeValueSeparator();
            } catch (IOException e) {
                // todo: custom exception
                throw new RuntimeException(e);
            }
        } else if (context.type == ContextType.OBJECT && context.valuePos > 0 && !context.nameWritten) {
            try {
                output.writeValueSeparator();
            } catch (IOException e) {
                // todo: custom exception
                throw new RuntimeException(e);
            }
        }
        context.valuePos++;
        context.nameWritten = false;
    }
}
