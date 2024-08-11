package ahodanenok.json.jp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

final class JsonReaderFactoryImpl implements JsonReaderFactory {

    @Override
    public JsonReader createReader(Reader reader) {
        return new JsonReaderImpl(reader);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return new JsonReaderImpl(new InputStreamReader(in, StandardCharsets.UTF_8.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)));
    }

    @Override
    public JsonReader createReader(InputStream in, Charset charset) {
        return new JsonReaderImpl(new InputStreamReader(in, charset));
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Map.of();
    }
}
