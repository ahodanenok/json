package ahodanenok.json.jp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonConfig;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

final class JsonReaderFactoryImpl implements JsonReaderFactory {

    private JsonConfig.KeyStrategy keyStrategy;

    JsonReaderFactoryImpl() {
        init(Map.of());
    }

    JsonReaderFactoryImpl(Map<String, ?> config) {
        init(config);
    }

    private void init(Map<String, ?> config) {
        JsonConfig.KeyStrategy keyStrategy = (JsonConfig.KeyStrategy) config.get(JsonConfig.KEY_STRATEGY);
        if (keyStrategy != null) {
            this.keyStrategy = keyStrategy;
        } else {
            this.keyStrategy = JsonConfig.KeyStrategy.LAST;
        }
    }

    @Override
    public JsonReader createReader(Reader reader) {
        Objects.requireNonNull(reader);

        JsonReaderImpl jsonReader = new JsonReaderImpl(reader);
        jsonReader.setKeyStrategy(keyStrategy);

        return jsonReader;
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
        return createReader(new InputStreamReader(
            in,
            charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)));
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Map.of(JsonConfig.KEY_STRATEGY, keyStrategy);
    }
}
