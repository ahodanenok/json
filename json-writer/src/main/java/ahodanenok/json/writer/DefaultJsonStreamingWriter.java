package ahodanenok.json.writer;

import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;
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
            throw new JsonWriteIOException("Failed to writeBeginArray", e);
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
            throw new JsonWriteIOException("Failed to writeBeginObject", e);
        }
    }

    @Override
    public void writeEnd() {
        WriteContext context = contexts.pop();
        if (context.type == ContextType.ARRAY) {
            try {
                output.writeEndArray();
            } catch (IOException e) {
                throw new JsonWriteIOException("Failed to writeEnd", e);
            }
        } else if (context.type == ContextType.OBJECT) {
            try {
                output.writeEndObject();
            } catch (IOException e) {
                throw new JsonWriteIOException("Failed to writeEnd", e);
            }
        } else if (context.type == ContextType.ROOT) {
            throw new JsonWriteException("There is no open object or array to end");
        } else {
            throw new IllegalStateException("Unknown context: " + context.type);
        }
    }

    @Override
    public void writeName(String name) {
        WriteContext context = contexts.peek();
        if (context.type != ContextType.OBJECT) {
            throw new JsonWriteException(String.format(
                "Must be in an object context, but current is %s", context.type.name().toLowerCase()));
        } else if (context.nameWritten) {
            throw new JsonWriteException("Can't write two names in a row without a value between them");
        }

        if (context.valuePos > 0) {
            try {
                output.writeValueSeparator();
            } catch (IOException e) {
                throw new JsonWriteIOException("Failed to writeValueSeparator", e);
            }
        }

        try {
            output.writeString(name);
            output.writeNameSeparator();
            context.nameWritten = true;
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeName", e);
        }
    }

    @Override
    public void writeString(String str) {
        prepareWriteOnValue();
        try {
            output.writeString(str);
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeString", e);
        }
    }

    @Override
    public void writeNumber(int num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(long num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(double num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(BigInteger num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(BigDecimal num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeBoolean(boolean bool) {
        prepareWriteOnValue();
        try {
            output.writeBoolean(bool);
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeBoolean", e);
        }
    }

    @Override
    public void writeNull() {
        prepareWriteOnValue();
        try {
            output.writeNull();
        } catch (IOException e) {
            throw new JsonWriteIOException("Failed to writeNull", e);
        }
    }

    private void prepareWriteOnValue() {
        WriteContext context = contexts.peek();
        if (context == null) {
            throw new IllegalStateException("No context!");
        }

        if (context.type == ContextType.ROOT && context.valuePos > 0) {
            throw new JsonWriteException("There can be only one value at the root");
        } else if (context.type == ContextType.ARRAY && context.valuePos > 0) {
            try {
                output.writeValueSeparator();
            } catch (IOException e) {
                throw new JsonWriteIOException("Failed to writeValueSeparator", e);
            }
        } else if (context.type == ContextType.OBJECT && !context.nameWritten) {
            throw new JsonWriteException("Each object member must begin with a name");
        }

        context.valuePos++;
        context.nameWritten = false;
    }
}
