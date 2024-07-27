package ahodanenok.json.value;

public final class JsonBoolean extends JsonValue {

    public static final JsonBoolean TRUE = new JsonBoolean(true);
    public static final JsonBoolean FALSE = new JsonBoolean(false);

    public static JsonBoolean of(boolean value) {
        return value ? JsonBoolean.TRUE : JsonBoolean.FALSE;
    }

    private final boolean value;

    JsonBoolean(boolean value) {
        super(ValueType.BOOLEAN);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
