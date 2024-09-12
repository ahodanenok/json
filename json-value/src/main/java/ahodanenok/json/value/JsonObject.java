package ahodanenok.json.value;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class JsonObject extends JsonValue {

    public static Builder builder() {
        return new Builder();
    }

    private final Map<String, JsonValue> values;

    public JsonObject(Map<String, JsonValue> values) {
        super(ValueType.OBJECT);
        this.values = values;
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(values.keySet());
    }

    public boolean containsValue(String name) {
        return values.containsKey(name);
    }

    public JsonValue getValue(String name) {
        return values.get(name);
    }

    public int size() {
        return values.size();
    }

    public static final class Builder {

        private final Map<String, JsonValue> values;

        private Builder() {
            values = new LinkedHashMap<>();
        }

        public Builder add(String name, JsonValue value) {
            values.put(name, value);
            return this;
        }

        public JsonObject build() {
            return new JsonObject(values);
        }
    }
}
