package ahodanenok.json.writer;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrettyPrintingJsonOutputTest {

    @Test
    public void testWriteNull() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeNull();
        assertEquals("null", writer.toString());
    }

    @Test
    public void testWriteTrue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBoolean(true);
        assertEquals("true", writer.toString());
    }

    @Test
    public void testWriteFalse() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBoolean(false);
        assertEquals("false", writer.toString());
    }

    @Test
    public void testWriteInteger() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeNumber(1);
        assertEquals("1", writer.toString());
    }

    @Test
    public void testWriteLong() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeNumber(1L);
        assertEquals("1", writer.toString());
    }

    @Test
    public void testWriteDouble() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeNumber(1d);
        assertEquals("1.0", writer.toString());
    }

    @Test
    public void testWriteBigInteger() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeNumber(BigInteger.valueOf(1));
        assertEquals("1", writer.toString());
    }

    @Test
    public void testWriteBigDecimal() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeNumber(BigDecimal.valueOf(1d));
        assertEquals("1.0", writer.toString());
    }

    @Test
    public void testWriteString() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeString("test");
        assertEquals("\"test\"", writer.toString());
    }

    @Test
    public void testWriteEmptyArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginArray();
        out.writeEndArray();
        assertEquals("[]", writer.toString());
    }

    @Test
    public void testWriteOneElementArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginArray();
        out.writeNumber(1);
        out.writeEndArray();
        assertEquals(
              "[" + System.lineSeparator()
            + "  1" + System.lineSeparator()
            + "]", writer.toString());
    }

    @Test
    public void testWriteMultipleElementArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginArray();
        out.writeNumber(1);
        out.writeValueSeparator();
        out.writeBoolean(true);
        out.writeValueSeparator();
        out.writeNull();
        out.writeEndArray();
        assertEquals(
              "[" + System.lineSeparator()
            + "  1," + System.lineSeparator()
            + "  true," + System.lineSeparator()
            + "  null" + System.lineSeparator()
            + "]", writer.toString());
    }

    @Test
    public void testWriteNestedArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginArray();
        out.writeNumber(1);
        out.writeValueSeparator();
        out.writeBeginArray();
        out.writeNumber(2);
        out.writeValueSeparator();
        out.writeNumber(3);
        out.writeEndArray();
        out.writeEndArray();
        assertEquals(
              "[" + System.lineSeparator()
            + "  1," + System.lineSeparator()
            + "  [" + System.lineSeparator()
            + "    2," + System.lineSeparator()
            + "    3" + System.lineSeparator()
            + "  ]" + System.lineSeparator()
            + "]", writer.toString());
    }

    @Test
    public void testWriteArrayWithObjects() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginArray();
        out.writeBeginObject();
        out.writeString("a");
        out.writeNameSeparator();
        out.writeString("aa");
        out.writeValueSeparator();
        out.writeString("b");
        out.writeNameSeparator();
        out.writeString("bb");
        out.writeEndObject();
        out.writeValueSeparator();
        out.writeBeginObject();
        out.writeString("c");
        out.writeNameSeparator();
        out.writeString("cc");
        out.writeValueSeparator();
        out.writeString("d");
        out.writeNameSeparator();
        out.writeString("dd");
        out.writeEndObject();
        out.writeEndArray();
        assertEquals(
              "[" + System.lineSeparator()
            + "  {" + System.lineSeparator()
            + "    \"a\": \"aa\"," + System.lineSeparator()
            + "    \"b\": \"bb\"" + System.lineSeparator()
            + "  }," + System.lineSeparator()
            + "  {" + System.lineSeparator()
            + "    \"c\": \"cc\"," + System.lineSeparator()
            + "    \"d\": \"dd\"" + System.lineSeparator()
            + "  }" + System.lineSeparator()
            + "]", writer.toString());
    }

    @Test
    public void testWriteEmptyObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginObject();
        out.writeEndObject();
        assertEquals("{}", writer.toString());
    }

    @Test
    public void testWriteOneElementObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginObject();
        out.writeString("test");
        out.writeNameSeparator();
        out.writeBoolean(true);
        out.writeEndObject();
        assertEquals(
              "{" + System.lineSeparator()
            + "  \"test\": true" + System.lineSeparator()
            + "}", writer.toString());
    }

    @Test
    public void testWriteMultipleElementObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginObject();
        out.writeString("a");
        out.writeNameSeparator();
        out.writeNumber(1);
        out.writeValueSeparator();
        out.writeString("b");
        out.writeNameSeparator();
        out.writeNumber(2);
        out.writeValueSeparator();
        out.writeString("c");
        out.writeNameSeparator();
        out.writeNumber(3);
        out.writeEndObject();
        assertEquals(
              "{" + System.lineSeparator()
            + "  \"a\": 1," + System.lineSeparator()
            + "  \"b\": 2," + System.lineSeparator()
            + "  \"c\": 3" + System.lineSeparator()
            + "}", writer.toString());
    }

    @Test
    public void testWriteNestedObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginObject();
        out.writeBeginObject();
        out.writeString("x");
        out.writeNameSeparator();
        out.writeString("y");
        out.writeEndObject();
        out.writeEndObject();
        assertEquals(
              "{" + System.lineSeparator()
            + "  {" + System.lineSeparator()
            + "    \"x\": \"y\"" + System.lineSeparator()
            + "  }" + System.lineSeparator()
            + "}", writer.toString());
    }

    @Test
    public void testWriteObjectWithNestedArrays() throws Exception {
        StringWriter writer = new StringWriter();
        JsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.writeBeginObject();
        out.writeString("a");
        out.writeNameSeparator();
        out.writeBeginArray();
        out.writeNumber(1);
        out.writeValueSeparator();
        out.writeNumber(2);
        out.writeEndArray();
        out.writeValueSeparator();
        out.writeString("b");
        out.writeNameSeparator();
        out.writeBeginArray();
        out.writeNull();
        out.writeEndArray();
        out.writeEndObject();
        assertEquals(
              "{" + System.lineSeparator()
            + "  \"a\": [" + System.lineSeparator()
            + "    1," + System.lineSeparator()
            + "    2" + System.lineSeparator()
            + "  ]," + System.lineSeparator()
            + "  \"b\": [" + System.lineSeparator()
            + "    null" + System.lineSeparator()
            + "  ]" + System.lineSeparator()
            + "}", writer.toString());
    }

    @Test
    public void testCustomProperties() throws Exception {
        StringWriter writer = new StringWriter();
        PrettyPrintingJsonOutput out = new PrettyPrintingJsonOutput(new DefaultJsonOutput(writer));
        out.setWhitespace("  ");
        out.setIndent("\t");
        out.setLineSeparator("\n");
        out.writeBeginArray();
        out.writeBeginObject();
        out.writeString("bool");
        out.writeNameSeparator();
        out.writeBoolean(true);
        out.writeEndObject();
        out.writeValueSeparator();
        out.writeString("test");
        out.writeEndArray();
        assertEquals(
              "[\n"
            + "\t{\n"
            + "\t\t\"bool\":  true\n"
            + "\t},\n"
            + "\t\"test\"\n"
            + "]", writer.toString());
    }
}
