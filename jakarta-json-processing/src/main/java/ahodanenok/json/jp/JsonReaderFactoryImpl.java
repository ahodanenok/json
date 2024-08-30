package ahodanenok.json.jp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.json.JsonConfig;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

final class JsonReaderFactoryImpl implements JsonReaderFactory {

    private final JsonConfig.KeyStrategy keyStrategy;
    private final Map<String, Object> configInUse;

    JsonReaderFactoryImpl() {
        this(Map.of());
    }

    JsonReaderFactoryImpl(Map<String, ?> config) {
        JsonConfig.KeyStrategy keyStrategy = (JsonConfig.KeyStrategy) config.get(JsonConfig.KEY_STRATEGY);
        if (keyStrategy != null) {
            this.keyStrategy = keyStrategy;
        } else {
            this.keyStrategy = JsonConfig.KeyStrategy.LAST;
        }

        this.configInUse = createConfigInUse(config.keySet());
    }

    private Map<String, Object> createConfigInUse(Set<String> configKeys) {
        Map<String, Object> configInUse = new HashMap<>();
        if (configKeys.contains(JsonConfig.KEY_STRATEGY)) {
            configInUse.put(JsonConfig.KEY_STRATEGY, this.keyStrategy);
        }

        return configInUse;
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
        return Collections.unmodifiableMap(configInUse);
    }
}
