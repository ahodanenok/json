package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.json.JsonNumber;

final class JsonNumberIntegerImpl implements JsonNumber {

    private int value;

    JsonNumberIntegerImpl(int value) {
        this.value = value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    @Override
    public boolean isIntegral() {
        return true;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public int intValueExact() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public long longValueExact() {
        return value;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return BigInteger.valueOf(value);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(value);
    }

    @Override
    public Number numberValue() {
        return Integer.valueOf(value);
    }

    @Override
    public String toString() {
        // todo: impl
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JsonNumber)) {
            return false;
        }

        if (obj instanceof JsonNumberIntegerImpl other) {
            return value == other.value;
        }

        return bigDecimalValue().equals(((JsonNumber) obj).bigDecimalValue());
    }

    @Override
    public int hashCode() {
        return value;
    }
}
