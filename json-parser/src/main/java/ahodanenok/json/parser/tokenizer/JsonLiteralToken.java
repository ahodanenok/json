package ahodanenok.json.parser.tokenizer;

final class JsonLiteralToken extends JsonToken  {

    private String literal;

    JsonLiteralToken(TokenType type, String literal) {
        super(type);
        this.literal = literal;
    }

    @Override
    public String getRepresentation() {
        return literal;
    }
}
