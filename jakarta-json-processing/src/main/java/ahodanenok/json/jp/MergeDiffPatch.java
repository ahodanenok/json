package ahodanenok.json.jp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.json.JsonMergePatch;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

final class MergeDiffPatch {

    static JsonMergePatch create(JsonValue source, JsonValue target) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        JsonValue patch = diff(source, target);
        if (patch != null) {
            return new JsonMergePatchImpl(patch);
        } else {
            return new JsonMergePatchImpl(new JsonObjectImpl(Map.of()));
        }
    }

    static JsonValue diff(JsonValue source, JsonValue target) {
        if (source.getValueType() != target.getValueType()) {
            return target;
        }

        if (!(target instanceof JsonStructure) && source.equals(target)) {
            return null;
        }

        if (target.getValueType() != JsonValue.ValueType.OBJECT) {
            return target;
        }

        JsonObject sourceObject = source.asJsonObject();
        JsonObject targetObject = target.asJsonObject();
        Map<String, JsonValue> values = new LinkedHashMap<>();
        for (Map.Entry<String, JsonValue> entry : sourceObject.entrySet()) {
            if (!targetObject.containsKey(entry.getKey())) {
                values.put(entry.getKey(), JsonValue.NULL);
            } else {
                JsonValue patch = diff(entry.getValue(), targetObject.get(entry.getKey()));
                if (patch != null) {
                    values.put(entry.getKey(), patch);
                }
            }
        }
        for (Map.Entry<String, JsonValue> entry : targetObject.entrySet()) {
            if (!sourceObject.containsKey(entry.getKey())) {
                values.put(entry.getKey(), entry.getValue());
            }
        }

        if (!values.isEmpty()) {
            return new JsonObjectImpl(values);
        } else {
            return null;
        }
    }
}
