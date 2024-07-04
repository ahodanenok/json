package ahodanenok.json.parser.tokenizer;

public interface JsonTokenizer {

    boolean advance();

    JsonToken currentToken();
}
