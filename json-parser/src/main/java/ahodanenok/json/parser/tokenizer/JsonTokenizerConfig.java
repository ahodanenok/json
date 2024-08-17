package ahodanenok.json.parser.tokenizer;

public final class JsonTokenizerConfig {

    // todo: additional configs

    int maxStringLength = -1;
    int maxNumberLength = -1;

    public void setMaxStringLength(int length) {
        // todo: check valid
        this.maxStringLength = length;
    }

    public void setMaxNumberLength(int length) {
        // todo: check valid
        this.maxNumberLength = length;
    }
}
