package ahodanenok.json.writer;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultJsonOutputTest {

    @Test
    public void testWriteBeginArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeBeginArray();
        assertEquals("[", writer.toString());
    }

    @Test
    public void testWriteEndArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeEndArray();
        assertEquals("]", writer.toString());
    }

    @Test
    public void testWriteBeginObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeBeginObject();
        assertEquals("{", writer.toString());
    }

    @Test
    public void testWriteEndObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeEndObject();
        assertEquals("}", writer.toString());
    }

    @Test
    public void testWriteValueSeparator() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeValueSeparator();
        assertEquals(",", writer.toString());
    }

    @Test
    public void testWriteNameSeparator() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeNameSeparator();
        assertEquals(":", writer.toString());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        '',     \"\"
        Hello World,     \"Hello World\"
        a\"b,     \"a\\\"b\"
        a\\b,     \"a\\\\b\"
        a/b,     \"a/b\"
        a\bb,     \"a\\bb\"
        a\fb,     \"a\\fb\"
        'a\nb',     \"a\\nb\"
        'a\rb',     \"a\\rb\"
        a\uFFFFb,     \"a\uFFFFb\"
        a\u0000b,     \"a\\u0000b\"
        a\u0001b,     \"a\\u0001b\"
        a\u0002b,     \"a\\u0002b\"
        a\u0003b,     \"a\\u0003b\"
        a\u0004b,     \"a\\u0004b\"
        a\u0005b,     \"a\\u0005b\"
        a\u0006b,     \"a\\u0006b\"
        a\u0007b,     \"a\\u0007b\"
        a\u0008b,     \"a\\bb\"
        a\u0009b,     \"a\\tb\"
        'a\u000Ab',     \"a\\nb\"
        a\u000Bb,     \"a\\u000bb\"
        a\u000Cb,     \"a\\fb\"
        'a\u000Eb',     \"a\\u000eb\"
        'a\u000Fb',     \"a\\u000fb\"
        'a\u0010b',     \"a\\u0010b\"
        'a\u0011b',     \"a\\u0011b\"
        'a\u0012b',     \"a\\u0012b\"
        'a\u0013b',     \"a\\u0013b\"
        'a\u0014b',     \"a\\u0014b\"
        'a\u0015b',     \"a\\u0015b\"
        'a\u0016b',     \"a\\u0016b\"
        'a\u0017b',     \"a\\u0017b\"
        'a\u0018b',     \"a\\u0018b\"
        'a\u0019b',     \"a\\u0019b\"
        'a\u001Ab',     \"a\\u001ab\"
        'a\u001bb',     \"a\\u001bb\"
        'a\u001Cb',     \"a\\u001cb\"
        'a\u001Db',     \"a\\u001db\"
        'a\u001eb',     \"a\\u001eb\"
        'a\u001Fb',     \"a\\u001fb\"
        'a\u0020b',     \"a b\"

    """)
    public void testWriteString(String str, String expected) throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeString(str);
        assertEquals(expected, writer.toString());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        0,     0
        -1,     -1
        1,     1
        1234567,     1234567
        -394389252,     -394389252
        2147483647,     2147483647
        -2147483648,     -2147483648
    """)
    public void testWriteNumberInteger(int n, String expected) throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeNumber(n);
        assertEquals(expected, writer.toString());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        0,     0
        -1,     -1
        1,     1
        1234567,     1234567
        -394389252,     -394389252
        2147483647,     2147483647
        -2147483648,     -2147483648,
        4939291293952,     4939291293952
        -22125969392367,     -22125969392367
        9223372036854775807,     9223372036854775807
        -9223372036854775808,     -9223372036854775808
    """)
    public void testWriteNumberLong(long n, String expected) throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeNumber(n);
        assertEquals(expected, writer.toString());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        0,     0.0
        0.0,     0.0
        -1,     -1.0
        -1.0,     -1.0
        1,     1.0
        1.0,     1.0
        1.532,     1.532
        -26.2621,    -26.2621
        1234567,     1234567.0
        -394389252,     -3.94389252E8
        2147483647,     2.147483647E9
        -2147483648,     -2.147483648E9,
        4939291293952,     4.939291293952E12
        -22125969392367,     -2.2125969392367E13
        9007199254740991,     9.007199254740991E15
        -9007199254740993,     -9.007199254740992E15
    """)
    public void testWriteNumberDouble(double n, String expected) throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeNumber(n);
        assertEquals(expected, writer.toString());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        0,     0
        -1,     -1
        1,     1
        1234567,     1234567
        -394389252,     -394389252
        2147483647,     2147483647
        -2147483648,     -2147483648,
        4939291293952,     4939291293952
        -22125969392367,     -22125969392367
        9223372036854775807,     9223372036854775807
        -9223372036854775808,     -9223372036854775808
        2502103015012030120102501023012050125201050120321512,     2502103015012030120102501023012050125201050120321512
        -896329493593423489320391010952035096921120925860192383486,     -896329493593423489320391010952035096921120925860192383486
    """)
    public void testWriteNumberBigInteger(BigInteger n, String expected) throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeNumber(n);
        assertEquals(expected, writer.toString());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        0,     0
        0.0,     0.0
        -1,     -1
        -1.0,     -1.0
        1,     1
        1.0,     1.0
        1.532,     1.532
        -26.2621,    -26.2621
        1234567,     1234567
        -394389252,     -394389252
        2147483647,     2147483647
        -2147483648,     -2147483648,
        4939291293952,     4939291293952
        -22125969392367,     -22125969392367
        9007199254740991,     9007199254740991
        -9007199254740993,     -9007199254740993
        359619284060128385810209395.19290590019029967689239210002858182820688223,     359619284060128385810209395.19290590019029967689239210002858182820688223
        -45929299693818823050012002039519.9296991929366993939886810293910295123,      -45929299693818823050012002039519.9296991929366993939886810293910295123
    """)
    public void testWriteNumberBigDecimal(BigDecimal n, String expected) throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeNumber(n);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteBooleanTrue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeBoolean(true);
        assertEquals("true", writer.toString());
    }

    @Test
    public void testWriteBooleanFalse() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeBoolean(false);
        assertEquals("false", writer.toString());
    }

    @Test
    public void testWriteBooleanNull() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeNull();
        assertEquals("null", writer.toString());
    }

    @Test
    public void testFlush() throws Exception {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        JsonOutput output = new DefaultJsonOutput(bufferedWriter);
        output.writeNumber(1);
        assertEquals("", writer.toString());
        output.flush();
        assertEquals("1", writer.toString());
    }

    @Test
    public void testClose() throws Exception {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        JsonOutput output = new DefaultJsonOutput(bufferedWriter);
        output.writeBoolean(true);
        assertEquals("", writer.toString());
        output.close();
        assertEquals("true", writer.toString());
    }

    @Test
    public void testWriteSequence() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput output = new DefaultJsonOutput(writer);
        output.writeBeginArray();
        output.writeString("a");
        output.writeNameSeparator();
        output.writeNumber(1);
        output.writeBeginObject();
        output.writeBoolean(true);
        output.writeValueSeparator();
        output.writeNumber(23L);
        output.writeValueSeparator();
        output.writeNull();
        output.writeValueSeparator();
        output.writeNumber(BigInteger.valueOf(555));
        output.writeEndObject();
        output.writeBoolean(false);
        output.writeNumber(3.14);
        output.writeNameSeparator();
        output.writeString("b");
        output.writeValueSeparator();
        output.writeNumber(BigDecimal.valueOf(-621.25012));
        output.writeEndArray();
        assertEquals("[\"a\":1{true,23,null,555}false3.14:\"b\",-621.25012]", writer.toString());
    }
}
