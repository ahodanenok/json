package ahodanenok.json.value;

import java.util.ArrayList;
import java.util.List;

public final class JsonArray extends JsonValue {

    public static Builder builder() {
        return new Builder();
    }

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

    public static final class Builder {

        private final List<JsonValue> items;

        private Builder() {
            items = new ArrayList<>();
        }

        public Builder add(JsonValue value) {
            items.add(value);
            return this;
        }

        public JsonArray build() {
            return new JsonArray(items);
        }
    }
}
