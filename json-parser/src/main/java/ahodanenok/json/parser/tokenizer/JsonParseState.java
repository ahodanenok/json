package ahodanenok.json.parser.tokenizer;

public final class JsonParseState {

    private final JsonParseLocation location;
    // todo: additional info to show in errors

    public JsonParseState(JsonParseLocation location) {
        this.location = location;
    }
}
