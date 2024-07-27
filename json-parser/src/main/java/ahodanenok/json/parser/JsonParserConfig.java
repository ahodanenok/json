package ahodanenok.json.parser;

import ahodanenok.json.parser.tokenizer.JsonTokenizerConfig;

public final class JsonParserConfig {

    // todo: additional configs

    int nestingMax = -1;
    final JsonTokenizerConfig tokenizerConfig = new JsonTokenizerConfig();

    public void setNestingMax(int nestingMax) {
        // todo: check valid
        this.nestingMax = nestingMax;
    }

    public void setMaxStringLength(int length) {
        tokenizerConfig.setMaxStringLength(length);
    }

    public void setMaxNumberLength(int length) {
        tokenizerConfig.setMaxNumberLength(length);
    }

    public boolean isUseBigDecimal() {
        return tokenizerConfig.isUseBigDecimal();
    }

    public void setUseBigDecimal(boolean use) {
        tokenizerConfig.setUseBigDecimal(use);
    }
}
