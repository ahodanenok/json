package ahodanenok.json.parser;

public interface JsonStreamingParser {

    boolean next();

    EventType currentEvent();

    String getString();

    double getDouble();

    boolean getBoolean();

    boolean isNull();
}
