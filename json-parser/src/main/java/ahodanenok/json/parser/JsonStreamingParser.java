package ahodanenok.json.parser;

import java.math.BigDecimal;

public interface JsonStreamingParser {

    boolean next();

    EventType currentEvent();

    String getString();

    int getInt();

    long getLong();

    double getDouble();

    BigDecimal getBigDecimal();

    boolean getBoolean();

    boolean isNull();
}
