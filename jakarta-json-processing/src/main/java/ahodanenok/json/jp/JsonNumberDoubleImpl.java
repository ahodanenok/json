package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.json.JsonNumber;

final class JsonNumberDoubleImpl implements JsonNumber {

    private final double value;

    JsonNumberDoubleImpl(double value) {
        this.value = value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    @Override
    public boolean isIntegral() {
        return BigDecimal.valueOf(value).scale() <= 0;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public int intValueExact() {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE || ((int) value) != value) {
            throw new ArithmeticException(String.format("Number '%s' is not representable as 'int'", value));
        }

        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public long longValueExact() {
        if (value < Long.MIN_VALUE || value > Long.MAX_VALUE || ((long) value) != value) {
            throw new ArithmeticException(String.format("Number '%s' is not representable as 'long'", value));
        }

        return (long) value;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return BigDecimal.valueOf(value).toBigInteger();
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return BigDecimal.valueOf(value).toBigIntegerExact();
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
        return Double.valueOf(value);
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

        if (obj instanceof JsonNumberDoubleImpl other) {
            return value == other.value;
        }

        return bigDecimalValue().equals(((JsonNumber) obj).bigDecimalValue());
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}
