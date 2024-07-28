package ahodanenok.json.jp;

import java.util.Collection;
import java.util.Map;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

final class JsonBuilderFactoryImpl implements JsonBuilderFactory {

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return null;
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(JsonObject object) {
        return null;
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(Map<String, Object> object) {
        return null;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return null;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(JsonArray array) {
        return null;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(Collection<?> collection) {
        return null;
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return null;
    }
}