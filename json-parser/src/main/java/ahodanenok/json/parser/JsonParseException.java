package ahodanenok.json.parser;

import ahodanenok.json.parser.tokenizer.JsonParseState;

public class JsonParseException extends RuntimeException {

    private final JsonParseState state;

    public JsonParseException(String msg, JsonParseState state) {
        this(msg, state, null);
    }

    public JsonParseException(String msg, JsonParseState state, Throwable cause) {
        super(msg, cause);
        this.state = state;
    }

    public JsonParseState getState() {
        return state;
    }
}
