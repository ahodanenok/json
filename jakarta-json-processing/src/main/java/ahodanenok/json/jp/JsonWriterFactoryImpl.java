package ahodanenok.json.jp;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

import ahodanenok.json.writer.DefaultJsonOutput;

final class JsonWriterFactoryImpl implements JsonWriterFactory {

    private final boolean prettyPrinting;
    private final Map<String, Object> configInUse;

    JsonWriterFactoryImpl() {
        this(Map.of());
    }

    JsonWriterFactoryImpl(Map<String, ?> config) {
        Object prettyPrinting = config.get(JsonGenerator.PRETTY_PRINTING);
        if (prettyPrinting instanceof Boolean) {
            this.prettyPrinting = (boolean) prettyPrinting;
        } else {
            this.prettyPrinting = false;
        }

        this.configInUse = createConfigInUse(config.keySet());
    }

    private Map<String, Object> createConfigInUse(Set<String> configKeys) {
        Map<String, Object> configInUse = new HashMap<>();
        if (configKeys.contains(JsonGenerator.PRETTY_PRINTING)) {
            configInUse.put(JsonGenerator.PRETTY_PRINTING, this.prettyPrinting);
        }

        return configInUse;
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        Objects.requireNonNull(writer);
        return new JsonWriterImpl(new DefaultJsonOutput(writer));
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        Objects.requireNonNull(out);
        return new JsonWriterImpl(new DefaultJsonOutput(new OutputStreamWriter(
            out,
            StandardCharsets.UTF_8.newEncoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT))));
    }

    @Override
    public JsonWriter createWriter(OutputStream out, Charset charset) {
        Objects.requireNonNull(out);
        Objects.requireNonNull(charset);
        return new JsonWriterImpl(new DefaultJsonOutput(new OutputStreamWriter(
            out,
            charset.newEncoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT))));
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Collections.unmodifiableMap(configInUse);
    }
}