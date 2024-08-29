package ahodanenok.json.jp;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import ahodanenok.json.writer.DefaultJsonOutput;

final class JsonGeneratorFactoryImpl implements JsonGeneratorFactory {

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return new JsonGeneratorImpl(new DefaultJsonOutput(writer));
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
        return Map.of();
    }
}