package ahodanenok.json.value;

import java.math.BigDecimal;

public abstract class JsonNumber extends JsonValue {

    public static JsonNumber of(double value) {
        return new JsonNumber.DoubleType(value);
    }

    public static JsonNumber of(BigDecimal value) {
        return new JsonNumber.BigDecimalType(value);
    }

    JsonNumber() {
        super(ValueType.NUMBER);
    }

    public abstract double doubleValue();

    public abstract BigDecimal bigDecimalValue();

    final static class DoubleType extends JsonNumber {

        private final double value;

        public DoubleType(double value) {
            this.value = value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public BigDecimal bigDecimalValue() {
            return BigDecimal.valueOf(value);
        }
    }

    final static class BigDecimalType extends JsonNumber {

        private final BigDecimal value;

        public BigDecimalType(BigDecimal value) {
            this.value = value;
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
