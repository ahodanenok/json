package ahodanenok.json.jp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;

import ahodanenok.json.parser.JsonParseException;
import ahodanenok.json.parser.tokenizer.DefaultJsonTokenizer;

final class JsonParserFactoryImpl implements JsonParserFactory {

    static final int BUFFER_SIZE = 8092;
    static final List<Charset> CHARSETS_TO_TRY = List.of(
        StandardCharsets.UTF_8,
        StandardCharsets.UTF_16,
        StandardCharsets.UTF_16LE,
        StandardCharsets.UTF_16BE,
        Charset.forName("UTF_32"),
        Charset.forName("UTF_32LE"),
        Charset.forName("UTF_32BE"));

    @Override
    public JsonParser createParser(Reader reader) {
        Objects.requireNonNull(reader);
        return new JsonParserImpl(reader);
    }

    @Override
    public JsonParser createParser(InputStream in) {
        Objects.requireNonNull(in);

        Charset charset = null;
        JsonException firstException = null;
        CustomBufferedInputStream bufIn  = new CustomBufferedInputStream(in);
        try {
            for (Charset possibleCharset : CHARSETS_TO_TRY) {
                bufIn.mark(BUFFER_SIZE);

                DefaultJsonTokenizer tokenizer = new DefaultJsonTokenizer(new InputStreamReader(
                    bufIn,
                    possibleCharset.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT)
                ));

                try {
                    while (bufIn.hasMark() && tokenizer.advance());
                } catch (JsonParseException e) {
                    if (firstException == null) {
                        firstException = new JsonException(e.getMessage(), e.getCause());
                    }

                    bufIn.reset();
                    continue;
                }

                charset = possibleCharset;
                bufIn.reset();
                break;
            }
        } catch (IOException e) {
            throw new JsonException("Couldn't determine encoding", e);
        }

        if (charset == null) {
            throw firstException;
        }

        return createParser(new InputStreamReader(
            bufIn,
            charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)));
    }

    @Override
    public JsonParser createParser(InputStream in, Charset charset) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(charset);
        return createParser(new InputStreamReader(
            in,
            charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)));
    }

    @Override
    public JsonParser createParser(JsonObject obj) {
        Objects.requireNonNull(obj);
        return createParser(new StringReader(obj.toString()));
    }

    @Override
    public JsonParser createParser(JsonArray array) {
        Objects.requireNonNull(array);
        return createParser(new StringReader(array.toString()));
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Map.of();
    }

    private static class CustomBufferedInputStream extends BufferedInputStream {

        CustomBufferedInputStream(InputStream in) {
            super(in);
        }

        boolean hasMark() {
            return markpos != -1;
        }
    }
}