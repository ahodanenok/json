package ahodanenok.json.jp;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonNumberBigIntegerImplTest {

    @Test
    public void testNumberZero() {
        JsonNumber number = new JsonNumberBigIntegerImpl(BigInteger.valueOf(0));
        assertEquals(JsonValue.ValueType.NUMBER, number.getValueType());
        assertTrue(number.isIntegral());
        assertEquals(0, number.intValue());
        assertEquals(0, number.intValueExact());
        assertEquals(0, number.longValue());
        assertEquals(0, number.longValueExact());
        assertEquals(BigInteger.valueOf(0), number.bigIntegerValue());
        assertEquals(BigInteger.valueOf(0), number.bigIntegerValueExact());
        assertEquals(0, number.doubleValue());
        assertEquals(BigDecimal.valueOf(0), number.bigDecimalValue());
        assertEquals(BigInteger.valueOf(0), number.numberValue());
    }

    @Test
    public void testNumberPositive() {
        JsonNumber number = new JsonNumberBigIntegerImpl(BigInteger.valueOf(623523123));
        assertEquals(JsonValue.ValueType.NUMBER, number.getValueType());
        assertTrue(number.isIntegral());
        assertEquals(623523123, number.intValue());
        assertEquals(623523123, number.intValueExact());
        assertEquals(623523123, number.longValue());
        assertEquals(623523123, number.longValueExact());
        assertEquals(BigInteger.valueOf(623523123), number.bigIntegerValue());
        assertEquals(BigInteger.valueOf(623523123), number.bigIntegerValueExact());
        assertEquals(623523123, number.doubleValue());
        assertEquals(BigDecimal.valueOf(623523123), number.bigDecimalValue());
        assertEquals(BigInteger.valueOf(623523123), number.numberValue());
    }

    @Test
    public void testNumberNegative() {
        JsonNumber number = new JsonNumberBigIntegerImpl(BigInteger.valueOf(-2147483648));
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
        assertEquals(BigInteger.valueOf(-2147483648), number.numberValue());
    }

    @Test
    public void testEqual() {
        assertEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(0)), new JsonNumberBigIntegerImpl(BigInteger.valueOf(0)));
        assertEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(0)).hashCode(), new JsonNumberBigIntegerImpl(BigInteger.valueOf(0)).hashCode());
        assertEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(534823)), new JsonNumberBigIntegerImpl(BigInteger.valueOf(534823)));
        assertEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(534823)).hashCode(), new JsonNumberBigIntegerImpl(BigInteger.valueOf(534823)).hashCode());
        assertEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(-7766534)), new JsonNumberBigIntegerImpl(BigInteger.valueOf(-7766534)));
        assertEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(-7766534)).hashCode(), new JsonNumberBigIntegerImpl(BigInteger.valueOf(-7766534)).hashCode());
    }

    @Test
    public void testNotEqual() {
        assertNotEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(0)), new JsonNumberBigIntegerImpl(BigInteger.valueOf(-1)));
        assertNotEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(-1)), new JsonNumberBigIntegerImpl(BigInteger.valueOf(-2)));
        assertNotEquals(new JsonNumberBigIntegerImpl(BigInteger.valueOf(734)), new JsonNumberBigIntegerImpl(BigInteger.valueOf(-734)));
    }
}