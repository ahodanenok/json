package ahodanenok.json.jp;

import java.io.StringWriter;

import jakarta.json.JsonValue;

import ahodanenok.json.writer.DefaultJsonOutput;

final class Utils {

    static String writeValueToString(JsonValue value) {
        StringWriter writer = new StringWriter();
        DefaultJsonOutput output = new DefaultJsonOutput(writer);
        JsonWriterImpl jsonWriter = new JsonWriterImpl(output);
        jsonWriter.write(value);

        return writer.toString();
    }
}
