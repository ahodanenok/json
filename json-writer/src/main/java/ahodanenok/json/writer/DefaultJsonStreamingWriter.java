package ahodanenok.json.writer;

import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.LinkedList;

public final class DefaultJsonStreamingWriter implements JsonStreamingWriter {

    private class WriteContext {
        WriteContextType type;
        int valuePos;
        boolean nameWritten;
    }

    private final JsonOutput output;
    private final LinkedList<WriteContext> contexts;

    public DefaultJsonStreamingWriter(JsonOutput output) {
        this.output = output;
        this.contexts = new LinkedList<>();

        WriteContext context = new WriteContext();
        context.type = WriteContextType.ROOT;
        this.contexts.push(context);
    }

    @Override
    public void writeBeginArray() {
        prepareWriteOnValue();

        WriteContext arrayContext = new WriteContext();
        arrayContext.type = WriteContextType.ARRAY;
        contexts.push(arrayContext);

        try {
            output.writeBeginArray();
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeBeginArray", e);
        }
    }

    @Override
    public void writeBeginObject() {
        prepareWriteOnValue();

        WriteContext arrayContext = new WriteContext();
        arrayContext.type = WriteContextType.OBJECT;
        contexts.push(arrayContext);

        try {
            output.writeBeginObject();
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeBeginObject", e);
        }
    }

    @Override
    public void writeEnd() {
        WriteContext context = contexts.pop();
        if (context.type == WriteContextType.ARRAY) {
            try {
                output.writeEndArray();
            } catch (IOException e) {
                throw new JsonWriteException("Failed to writeEnd", e);
            }
        } else if (context.type == WriteContextType.OBJECT) {
            try {
                output.writeEndObject();
            } catch (IOException e) {
                throw new JsonWriteException("Failed to writeEnd", e);
            }
        } else if (context.type == WriteContextType.ROOT) {
            throw new JsonWriteException("There is no open object or array to end");
        } else {
            throw new IllegalStateException("Unknown context: " + context.type);
        }
    }

    @Override
    public void writeName(String name) {
        WriteContext context = contexts.peek();
        if (context.type != WriteContextType.OBJECT) {
            throw new JsonWriteException(String.format(
                "Must be in '%s' context, but current context is '%s'", WriteContextType.OBJECT, context.type));
        } else if (context.nameWritten) {
            throw new JsonWriteException("Can't write two names in a row without a value between them");
        }

        if (context.valuePos > 0) {
            try {
                output.writeValueSeparator();
            } catch (IOException e) {
                throw new JsonWriteException("Failed to writeValueSeparator", e);
            }
        }

        try {
            output.writeString(name);
            output.writeNameSeparator();
            context.nameWritten = true;
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeName", e);
        }
    }

    @Override
    public void writeString(String str) {
        prepareWriteOnValue();
        try {
            output.writeString(str);
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeString", e);
        }
    }

    @Override
    public void writeNumber(int num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(long num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(double num) {
        if (Double.isNaN(num)) {
            throw new NumberFormatException("Illegal number: NaN");
        } else if (num == Double.POSITIVE_INFINITY || num == Double.NEGATIVE_INFINITY) {
            throw new NumberFormatException("Illegal number: Infinity");
        }

        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(BigInteger num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeNumber(BigDecimal num) {
        prepareWriteOnValue();
        try {
            output.writeNumber(num);
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeNumber", e);
        }
    }

    @Override
    public void writeBoolean(boolean bool) {
        prepareWriteOnValue();
        try {
            output.writeBoolean(bool);
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeBoolean", e);
        }
    }

    @Override
    public void writeNull() {
        prepareWriteOnValue();
        try {
            output.writeNull();
        } catch (IOException e) {
            throw new JsonWriteException("Failed to writeNull", e);
        }
    }

    @Override
    public void close() {
        if (contexts.peek().type != WriteContextType.ROOT) {
            throw new JsonWriteException("Incomplete json");
        }

        try {
            output.close();
        } catch (IOException e) {
            throw new JsonWriteException("Failed to close output", e);
        }
    }

    private void prepareWriteOnValue() {
        WriteContext context = contexts.peek();
        if (context.type == WriteContextType.ROOT && context.valuePos > 0) {
            throw new JsonWriteException("There can be only one value at the root");
        } else if (context.type == WriteContextType.ARRAY && context.valuePos > 0) {
            try {
                output.writeValueSeparator();
            } catch (IOException e) {
                throw new JsonWriteException("Failed to writeValueSeparator", e);
            }
        } else if (context.type == WriteContextType.OBJECT && !context.nameWritten) {
            throw new JsonWriteException("Each object member must begin with a name");
        }

        context.valuePos++;
        context.nameWritten = false;
    }
}
