package ahodanenok.json.value;

public final class JsonString extends JsonValue {

    private final String value;

    public JsonString(String value) {
        super(ValueType.STRING);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
