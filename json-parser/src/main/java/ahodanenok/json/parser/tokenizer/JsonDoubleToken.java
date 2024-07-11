package ahodanenok.json.parser.tokenizer;

final class JsonDoubleToken extends JsonToken {

    private final double value;
    private final String representation;

    JsonDoubleToken(TokenType type, double value, String representation) {
        super(type);
        this.value = value;
        this.representation = representation;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String getRepresentation() {
        return representation;
    }
}
