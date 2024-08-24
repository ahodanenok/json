package ahodanenok.json.parser;

import java.math.BigDecimal;

public interface JsonStreamingParser {

    boolean hasNext();

    boolean next();

    EventType currentEvent();

    String getToken();

    String getString();

    int getInt();

    long getLong();

    double getDouble();

    BigDecimal getBigDecimal();

    boolean getBoolean();

    boolean isNull();
}
