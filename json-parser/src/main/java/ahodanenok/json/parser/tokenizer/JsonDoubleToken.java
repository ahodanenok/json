package ahodanenok.json.parser.tokenizer;

final class JsonDoubleToken extends JsonToken {

    private final double value;

    JsonDoubleToken(TokenType type, double value) {
        super(type);
        this.value = value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
