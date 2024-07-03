package ahodanenok.json.tokenizer;

final class JsonDoubleToken extends JsonToken {

    private final double value;

    JsonDoubleToken(TokenType type, double value) {
        super(type);
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
