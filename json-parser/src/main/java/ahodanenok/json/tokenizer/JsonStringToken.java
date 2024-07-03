package ahodanenok.json.tokenizer;

final class JsonStringToken extends JsonToken  {

    private final String value;

    JsonStringToken(TokenType type, String value) {
        super(type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
