package ahodanenok.json.value;

public final class JsonNumber extends JsonValue {

    private final double value;

    public JsonNumber(double value) {
        super(ValueType.NUMBER);
        this.value = value;
    }

    public double doubleValue() {
        return value;
    }
}
