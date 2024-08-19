package ahodanenok.json.jp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

final class JsonReaderFactoryImpl implements JsonReaderFactory {

    @Override
    public JsonReader createReader(Reader reader) {
        Objects.requireNonNull(reader);
        return new JsonReaderImpl(reader);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        Objects.requireNonNull(in);
        return new DetectEncodingJsonReaderImpl(in, this::createReader);
    }

    @Override
    public JsonReader createReader(InputStream in, Charset charset) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(charset);
        return new JsonReaderImpl(new InputStreamReader(
            in,
            charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)));
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Map.of();
    }
}
