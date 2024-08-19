package ahodanenok.json.jp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

final class DetectEncodingJsonReaderImpl implements JsonReader {

    static final int BUFFER_SIZE = 8092;
    static final List<Charset> CHARSETS_TO_TRY = List.of(
        StandardCharsets.UTF_8,
        StandardCharsets.UTF_16,
        StandardCharsets.UTF_16LE,
        StandardCharsets.UTF_16BE,
        Charset.forName("UTF_32"),
        Charset.forName("UTF_32LE"),
        Charset.forName("UTF_32BE"));

    private final BufferedInputStream in;
    private final BiFunction<InputStream, Charset, JsonReader> readerSupplier;
    private JsonReader reader;
    private JsonReader firstReader;
    private JsonException firstException;

    DetectEncodingJsonReaderImpl(InputStream in, BiFunction<InputStream, Charset, JsonReader> readerSupplier) {
        this.in = new BufferedInputStream(in, BUFFER_SIZE);
        this.readerSupplier = readerSupplier;
    }

    @Override
    public JsonStructure read() {
        return execute(r -> r.read());
    }

    @Override
    public JsonObject readObject() {
        return execute(r -> r.readObject());
    }

    @Override
    public JsonArray readArray() {
        return execute(r -> r.readArray());
    }

    @Override
    public JsonValue readValue() {
        return execute(r -> r.readValue());
    }

    @Override
    public void close() {
        execute(r -> {
            r.close();
            return null;
        });
    }

    private <T> T execute(Function<JsonReader, T> action) {
        if (firstReader != null) {
            return action.apply(firstReader);
        }

        for (Charset charset : CHARSETS_TO_TRY) {
            reader = readerSupplier.apply(in, charset);
            if (firstReader == null) {
                firstReader = reader;
            }

            try {
                in.mark(BUFFER_SIZE);
                return action.apply(reader);
            } catch (JsonException e) {
                if (firstException == null) {
                    firstException = e;
                }

                if (e.getMessage().startsWith("Unexpected token")
                        || e.getCause() instanceof CharacterCodingException) {
                    try {
                        in.reset();
                    } catch (IOException __) {
                        throw e;
                    }
                } else {
                    throw e;
                }
            }
        }

        reader = firstReader;
        throw firstException;
    }
}
