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

    static void checkDouble(double value) {
        if (Double.isNaN(value)) {
            throw new NumberFormatException("Number can't be NaN");
        }
        if (value == Double.NEGATIVE_INFINITY) {
            throw new NumberFormatException("Number can't be NEGATIVE_INFINITY");
        }
        if (value == Double.POSITIVE_INFINITY) {
            throw new NumberFormatException("Number can't be POSITIVE_INFINITY");
        }
    }
}
