package ahodanenok.json.parser.tokenizer;

import java.math.BigDecimal;

abstract class JsonNumberToken extends JsonToken {

    JsonNumberToken() {
        super(TokenType.NUMBER);
    }

    final static class DoubleType extends JsonNumberToken {

        private final double value;
        private final String representation;

        DoubleType(double value, String representation) {
            this.value = value;
            this.representation = representation;
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
