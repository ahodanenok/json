package ahodanenok.json.value;

public abstract class JsonValue {

    private final ValueType type;

    JsonValue(ValueType type) {
        this.type = type;
    }

    public ValueType getType() {
        return type;
    }
}
