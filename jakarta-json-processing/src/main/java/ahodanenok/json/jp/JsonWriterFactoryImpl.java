package ahodanenok.json.jp;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;

import ahodanenok.json.writer.DefaultJsonOutput;

final class JsonWriterFactoryImpl implements JsonWriterFactory {

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
        return Map.of();
    }
}