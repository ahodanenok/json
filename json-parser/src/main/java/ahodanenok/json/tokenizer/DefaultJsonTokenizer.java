package ahodanenok.json.tokenizer;

import java.io.Reader;

public final class DefaultJsonTokenizer implements JsonTokenizer {

    private final Reader reader;

    public DefaultJsonTokenizer(Reader reader) {
        this.reader = reader;
    }

    @Override
    public boolean advance() {
        return false;
    }

    @Override
    public JsonToken currentToken() {
        return null;
    }
}
