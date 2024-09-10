package ahodanenok.json.jp;

import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

final class DiffPatch {

    static JsonPatch create(JsonStructure source, JsonStructure target) {
        JsonPatchBuilder patchBuilder = new JsonPatchBuilderImpl();
        diff(source, target, "", patchBuilder);

        return patchBuilder.build();
    }

    static void diff(JsonValue source, JsonValue target, String path, JsonPatchBuilder patchBuilder) {
        if (source.getValueType() != target.getValueType()) {
            patchBuilder.replace(path, target);
        } else if (source.getValueType() == JsonValue.ValueType.OBJECT) {
            JsonObject sourceObject = source.asJsonObject();
            JsonObject targetObject = target.asJsonObject();

            for (Map.Entry<String, JsonValue> entry : sourceObject.entrySet()) {
                if (!targetObject.containsKey(entry.getKey())) {
                    patchBuilder.remove(path + "/" + entry.getKey());
                } else {
                    diff(entry.getValue(), targetObject.get(entry.getKey()),
                        path + "/" + entry.getKey(), patchBuilder);
                }
            }

            for (Map.Entry<String, JsonValue> entry : targetObject.entrySet()) {
                if (!sourceObject.containsKey(entry.getKey())) {
                    patchBuilder.add(path + "/" + entry.getKey(), entry.getValue());
                }
            }
        } else if (source.getValueType() == JsonValue.ValueType.ARRAY) {
            JsonArray sourceArray = source.asJsonArray();
            JsonArray targetArray = target.asJsonArray();

            for (int i = 0, n = Math.min(sourceArray.size(), targetArray.size()); i < n; i++) {
                diff(sourceArray.get(i), targetArray.get(i), path + "/" + i, patchBuilder);
            }

            if (sourceArray.size() > targetArray.size()) {
                for (int i = targetArray.size(); i < sourceArray.size(); i++) {
                    patchBuilder.remove(path + "/" + i);
                }
            } else if (sourceArray.size() < targetArray.size()) {
                for (int i = sourceArray.size(); i < targetArray.size(); i++) {
                    patchBuilder.add(path + "/" + i, targetArray.get(i));
                }
            }
        }
    }
}
