package ahodanenok.json.jp;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.json.JsonValue;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonObject;

final class JsonMergePatchImpl implements JsonMergePatch {

    private final JsonValue patch;

    JsonMergePatchImpl(JsonValue patch) {
        this.patch = patch;
    }

    @Override
    public JsonValue apply(JsonValue target) {
        return applyInternal(target, patch);
    }

    private JsonValue applyInternal(JsonValue target, JsonValue patch) {
        if (patch.getValueType() != JsonValue.ValueType.OBJECT) {
            return patch;
        }

        Map<String, JsonValue> values;
        if (target != null && target.getValueType() == JsonValue.ValueType.OBJECT) {
            values = new LinkedHashMap<>(target.asJsonObject());
        } else {
            values = new LinkedHashMap<>();
        }

        for (Map.Entry<String, JsonValue> entry : patch.asJsonObject().entrySet()) {
            if (entry.getValue() == JsonValue.NULL) {
                values.remove(entry.getKey());
            } else {
                values.put(
                    entry.getKey(),
                    applyInternal(values.get(entry.getKey()), entry.getValue()));
            }
        }

        return new JsonObjectImpl(values);
    }

    @Override
    public JsonValue toJsonValue() {
        return patch;
    }
}