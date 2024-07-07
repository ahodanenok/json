package ahodanenok.json.parser.tokenizer;

final class JsonStringToken extends JsonToken  {

    private final String value;

    JsonStringToken(TokenType type, String value) {
        super(type);
        this.value = value;
    }

    @Override
    public String stringValue() {
        return value;
    }
}
