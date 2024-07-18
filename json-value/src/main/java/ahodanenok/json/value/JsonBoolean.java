package ahodanenok.json.value;

public final class JsonBoolean extends JsonValue {

    private final boolean value;

    public JsonBoolean(boolean value) {
        super(ValueType.BOOLEAN);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
