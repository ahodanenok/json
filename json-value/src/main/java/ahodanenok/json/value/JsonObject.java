package ahodanenok.json.value;

import java.util.Map;

public final class JsonObject extends JsonValue {

    private final Map<String, JsonValue> values;

    public JsonObject(Map<String, JsonValue> values) {
        super(ValueType.OBJECT);
        this.values = values;
    }

    public boolean containsValue(String name) {
        return values.containsKey(name);
    }

    public JsonValue getValue(String name) {
        // todo: not found
        return values.get(name);
    }

    public int size() {
        return values.size();
    }
}