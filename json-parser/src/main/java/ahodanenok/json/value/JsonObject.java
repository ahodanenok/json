package ahodanenok.json.value;

import java.util.Map;

public final class JsonObject extends JsonValue {

    private final Map<String, JsonValue> fields;

    public JsonObject(Map<String, JsonValue> fields) {
        super(ValueType.OBJECT);
        this.fields = fields;
    }

    public JsonValue getField(String name) {
        // todo: not found
        return fields.get(name);
    }

    public int size() {
        return fields.size();
    }
}