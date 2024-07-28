package ahodanenok.json.jp;

import jakarta.json.JsonString;
import jakarta.json.JsonValue;

final class JsonStringImpl implements JsonString {

    private String string;

    @Override
    public JsonValue.ValueType getValueType() {
        return ValueType.STRING;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public CharSequence getChars() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}