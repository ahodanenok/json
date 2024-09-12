package ahodanenok.json.value;

import java.math.BigDecimal;

public abstract sealed class JsonNumber extends JsonValue {

    public static JsonNumber of(double value) {
        return new JsonNumber.BigDecimalType(BigDecimal.valueOf(value));
    }

    public static JsonNumber of(BigDecimal value) {
        return new JsonNumber.BigDecimalType(value);
    }

    private JsonNumber() {
        super(ValueType.NUMBER);
    }

    public abstract int intValue();

    public abstract long longValue();

    public abstract double doubleValue();

    public abstract BigDecimal bigDecimalValue();

    private static final class BigDecimalType extends JsonNumber {

        private final BigDecimal value;

        private BigDecimalType(BigDecimal value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value.intValue();
        }

        @Override
        public long longValue() {
            return value.longValue();
        }

        @Override
        public double doubleValue() {
            return value.doubleValue();
        }

        @Override
        public BigDecimal bigDecimalValue() {
            return value;
        }
    }
}
