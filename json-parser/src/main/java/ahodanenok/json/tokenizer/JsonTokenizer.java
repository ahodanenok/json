package ahodanenok.json.tokenizer;

public interface JsonTokenizer {

    boolean advance();

    JsonToken currentToken();
}
