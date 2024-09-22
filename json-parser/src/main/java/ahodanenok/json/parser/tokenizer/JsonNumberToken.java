package ahodanenok.json.parser.tokenizer;

import java.math.BigDecimal;

abstract sealed class JsonNumberToken extends JsonToken {

    JsonNumberToken() {
        super(TokenType.NUMBER);
    }

    final static class IntegerType extends JsonNumberToken {

        private final int value;
        private final String representation;

        IntegerType(int value, String representation) {
            this.value = value;
            this.representation = representation;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
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
        public String getRepresentation() {
            return representation;
        }
    }

    final static class LongType extends JsonNumberToken {

        private final long value;
        private final String representation;

        LongType(long value, String representation) {
            this.value = value;
            this.representation = representation;
        }

        @Override
        public int intValue() {
            return (int) value;
        }

        @Override
        public long longValue() {
            return value;
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
        public String getRepresentation() {
            return representation;
        }
    }

    final static class BigDecimalType extends JsonNumberToken {

        private final BigDecimal value;
        private final String representation;

        BigDecimalType(BigDecimal value, String representation) {
            this.value = value;
            this.representation = representation;
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

        @Override
        public String getRepresentation() {
            return representation;
        }
    }
}
