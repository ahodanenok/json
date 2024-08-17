package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonNumberWrapperImplTest {

    @Test
    public void testNumberZero() {
        JsonNumber number = new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0));
        assertEquals(JsonValue.ValueType.NUMBER, number.getValueType());
        assertFalse(number.isIntegral());
        assertEquals(0, number.intValue());
        assertEquals(0, number.intValueExact());
        assertEquals(0, number.longValue());
        assertEquals(0, number.longValueExact());
        assertEquals(BigInteger.valueOf(0), number.bigIntegerValue());
        assertEquals(BigInteger.valueOf(0), number.bigIntegerValueExact());
        assertEquals(0, number.doubleValue());
        assertEquals(BigDecimal.valueOf(0f), number.bigDecimalValue());
        assertEquals(BigDecimal.valueOf(0f), number.numberValue());
    }

    @Test
    public void testNumberPositiveDecimal() {
        JsonNumber number = new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(3.14));
        assertEquals(JsonValue.ValueType.NUMBER, number.getValueType());
        assertFalse(number.isIntegral());
        assertEquals(3, number.intValue());
        assertThrows(ArithmeticException.class, () -> number.intValueExact());
        assertEquals(3, number.longValue());
        assertThrows(ArithmeticException.class, () -> number.longValueExact());
        assertEquals(BigInteger.valueOf(3), number.bigIntegerValue());
        assertThrows(ArithmeticException.class, () -> number.bigIntegerValueExact());
        assertEquals(3.14, number.doubleValue());
        assertEquals(BigDecimal.valueOf(3.14), number.bigDecimalValue());
        assertEquals(BigDecimal.valueOf(3.14), number.numberValue());
    }

    @Test
    public void testNumberNegativeDecimal() {
        JsonNumber number = new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-489.25678));
        assertEquals(JsonValue.ValueType.NUMBER, number.getValueType());
        assertFalse(number.isIntegral());
        assertEquals(-489, number.intValue());
        assertThrows(ArithmeticException.class, () -> number.intValueExact());
        assertEquals(-489, number.longValue());
        assertThrows(ArithmeticException.class, () -> number.longValueExact());
        assertEquals(BigInteger.valueOf(-489), number.bigIntegerValue());
        assertThrows(ArithmeticException.class, () -> number.bigIntegerValueExact());
        assertEquals(-489.25678, number.doubleValue());
        assertEquals(BigDecimal.valueOf(-489.25678), number.bigDecimalValue());
        assertEquals(BigDecimal.valueOf(-489.25678), number.numberValue());
    }

    @Test
    public void testNumberPositiveIntegral() {
        JsonNumber number = new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(534823));
        assertEquals(JsonValue.ValueType.NUMBER, number.getValueType());
        assertFalse(number.isIntegral());
        assertEquals(534823, number.intValue());
        assertEquals(534823, number.intValueExact());
        assertEquals(534823, number.longValue());
        assertEquals(534823, number.longValueExact());
        assertEquals(BigInteger.valueOf(534823), number.bigIntegerValue());
        assertEquals(BigInteger.valueOf(534823), number.bigIntegerValueExact());
        assertEquals(534823, number.doubleValue());
        assertEquals(BigDecimal.valueOf(534823f), number.bigDecimalValue());
        assertEquals(BigDecimal.valueOf(534823f), number.numberValue());
    }

    @Test
    public void testNumberNegativeIntegral() {
        JsonNumber number = new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-2147483648));
        assertEquals(JsonValue.ValueType.NUMBER, number.getValueType());
        assertTrue(number.isIntegral());
        assertEquals(-2147483648, number.intValue());
        assertEquals(-2147483648, number.intValueExact());
        assertEquals(-2147483648, number.longValue());
        assertEquals(-2147483648, number.longValueExact());
        assertEquals(BigInteger.valueOf(-2147483648), number.bigIntegerValue());
        assertEquals(BigInteger.valueOf(-2147483648), number.bigIntegerValueExact());
        assertEquals(-2147483648, number.doubleValue());
        assertEquals(BigDecimal.valueOf(-2147483648), number.bigDecimalValue());
        assertEquals(BigDecimal.valueOf(-2147483648), number.numberValue());
    }

    @Test
    public void testEqual() {
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0)));
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0)).hashCode(), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0)).hashCode());
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(534823)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(534823)));
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(534823)).hashCode(), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(534823)).hashCode());
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7766534)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7766534)));
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7766534)).hashCode(), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7766534)).hashCode());
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(6039.38510633)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(6039.38510633)));
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(6039.38510633)).hashCode(), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(6039.38510633)).hashCode());
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7918376.9123566)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7918376.9123566)));
        assertEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7918376.9123566)).hashCode(), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-7918376.9123566)).hashCode());
    }

    @Test
    public void testNotEqual() {
        assertNotEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0.1)));
        assertNotEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(0)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-0.1)));
        assertNotEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-1)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-2.1)));
        assertNotEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(734)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-734)));
        assertNotEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(3.14)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(3.141)));
        assertNotEquals(new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-23.01)), new JsonNumberWrapperImpl(ahodanenok.json.value.JsonNumber.of(-23.02)));
    }
}
