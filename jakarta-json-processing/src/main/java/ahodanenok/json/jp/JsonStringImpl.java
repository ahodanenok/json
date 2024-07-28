package ahodanenok.json.jp;

import jakarta.json.JsonString;
import jakarta.json.JsonValue;

final class JsonStringImpl implements JsonString {

    private final String value;

    JsonStringImpl(String value) {
        this.value = value;
    }

    @Override
    public JsonValue.ValueType getValueType() {
        return ValueType.STRING;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public CharSequence getChars() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JsonString)) {
            return false;
        }

        return value.equals(((JsonString) obj).getString());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        // todo: impl
        return null;
    }
}