package ahodanenok.json.value;

public final class JsonString extends JsonValue {

    public static JsonString of(String value) {
        return new JsonString(value);
    }

    private final String value;

    private JsonString(String value) {
        super(ValueType.STRING);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
