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
import java.util.Set;

import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import ahodanenok.json.writer.DefaultJsonOutput;
import ahodanenok.json.writer.JsonOutput;
import ahodanenok.json.writer.PrettyPrintingJsonOutput;

final class JsonGeneratorFactoryImpl implements JsonGeneratorFactory {

    private final boolean prettyPrinting;
    private final Map<String, Object> configInUse;

    JsonGeneratorFactoryImpl() {
        this(Map.of());
    }

    JsonGeneratorFactoryImpl(Map<String, ?> config) {
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
    public JsonGenerator createGenerator(Writer writer) {
        JsonOutput out = new DefaultJsonOutput(writer);
        if (prettyPrinting) {
            out = new PrettyPrintingJsonOutput(out);
        }

        return new JsonGeneratorImpl(out);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return createGenerator(out, StandardCharsets.UTF_8);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset) {
        return createGenerator(new OutputStreamWriter(
            out,
            charset.newEncoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)));
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return Collections.unmodifiableMap(configInUse);
    }
}
