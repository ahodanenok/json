package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;

final class JsonNumberIntegerImpl implements jakarta.json.JsonNumber {

    private int value;

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    @Override
    public boolean isIntegral() {
        return false;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public int intValueExact() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public long longValueExact() {
        return 0;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return null;
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return null;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return null;
    }

    @Override
    public Number numberValue() {
        return null;
    }

    @Override
    public String toString() {
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
}
