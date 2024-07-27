package ahodanenok.json.parser.tokenizer;

public final class JsonTokenizerConfig {

    // todo: additional configs

    int maxStringLength = -1;
    int maxNumberLength = -1;

    boolean useBigDecimal;

    public void setMaxStringLength(int length) {
        // todo: check valid
        this.maxStringLength = length;
    }

    public void setMaxNumberLength(int length) {
        // todo: check valid
        this.maxNumberLength = length;
    }

    public boolean isUseBigDecimal() {
        return useBigDecimal;
    }

    public void setUseBigDecimal(boolean use) {
        this.useBigDecimal = use;
    }
}