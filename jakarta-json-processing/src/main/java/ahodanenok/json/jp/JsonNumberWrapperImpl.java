package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import jakarta.json.JsonNumber;

final class JsonNumberWrapperImpl implements JsonNumber {

    private final ahodanenok.json.value.JsonNumber number;

    JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber number) {
        this.number = Objects.requireNonNull(number);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    @Override
    public boolean isIntegral() {
        return number.bigDecimalValue().scale() <= 0;
    }

    @Override
    public int intValue() {
        return (int) number.doubleValue();
    }

    @Override
    public int intValueExact() {
        double value = number.doubleValue();
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE || ((int) value) != value) {
            throw new ArithmeticException(String.format("Number '%s' is not representable as 'int'", value));
        }

        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) number.doubleValue();
    }

    @Override
    public long longValueExact() {
        double value = number.doubleValue();
        if (value < Long.MIN_VALUE || value > Long.MAX_VALUE || ((long) value) != value) {
            throw new ArithmeticException(String.format("Number '%s' is not representable as 'long'", value));
        }

        return (long) value;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return bigDecimalValue().toBigInteger();
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return bigDecimalValue().toBigIntegerExact();
    }

    @Override
    public double doubleValue() {
        return number.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return number.bigDecimalValue();
    }

    @Override
    public Number numberValue() {
        return number.bigDecimalValue();
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

        return bigDecimalValue().equals(((JsonNumber) obj).bigDecimalValue());
    }

    @Override
    public int hashCode() {
        return number.bigDecimalValue().hashCode();
    }
}
