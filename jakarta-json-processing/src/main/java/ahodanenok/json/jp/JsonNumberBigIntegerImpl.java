package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import jakarta.json.JsonNumber;

final class JsonNumberBigIntegerImpl implements JsonNumber {

    private final BigInteger value;

    JsonNumberBigIntegerImpl(BigInteger value) {
        this.value = Objects.requireNonNull(value);
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
        return value.intValue();
    }

    @Override
    public int intValueExact() {
        return value.intValueExact();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public long longValueExact() {
        return value.longValueExact();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return value;
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value);
    }

    @Override
    public Number numberValue() {
        return value;
    }

    @Override
    public String toString() {
        return Utils.writeValueToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JsonNumber)) {
            return false;
        }

        if (obj instanceof JsonNumberBigIntegerImpl other) {
            return value.equals(other.value);
        }

        return bigDecimalValue().equals(((JsonNumber) obj).bigDecimalValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
