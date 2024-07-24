package ahodanenok.json.writer;

import java.io.IOException;

public class JsonWriteIOException extends JsonWriteException {

    public JsonWriteIOException(String message, IOException cause) {
        super(message, cause);
    }
}
