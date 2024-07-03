package ahodanenok.json.tokenizer;

public final class DefaultJsonTokenizer implements JsonTokenizer {

    @Override
    public boolean advance() {
        return false;
    }

    @Override
    public JsonToken currentToken() {
        return null;
    }
}
