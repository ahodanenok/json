package ahodanenok.json.value;

public abstract class JsonValue {

    public static final JsonNull NULL = new JsonNull();

    private final ValueType type;

    JsonValue(ValueType type) {
        this.type = type;
    }

    public final ValueType getType() {
        return type;
    }

    public final JsonNumber asNumber() {
        return (JsonNumber) this;
    }

    public final JsonString asString() {
        return (JsonString) this;
    }

    public final JsonBoolean asBoolean() {
        return (JsonBoolean) this;
    }

    public final JsonArray asArray() {
        return (JsonArray) this;
    }

    public final JsonObject asObject() {
        return (JsonObject) this;
    }

    public final boolean isNull() {
        return getType() == ValueType.NULL;
    }
}
