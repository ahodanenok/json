package ahodanenok.json.value;

import java.util.List;

public final class JsonArray extends JsonValue {

    private final List<JsonValue> items;

    public JsonArray(List<JsonValue> items) {
        super(ValueType.ARRAY);
        this.items = items;
    }

    public JsonValue getItem(int pos) {
        return items.get(pos);
    }

    public int size() {
        return items.size();
    }
}
