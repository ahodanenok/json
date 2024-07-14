package ahodanenok.json.parser.pull;

public interface JsonStreamingParser {

    boolean next();

    EventType currentEvent();

    String getString();

    double getDouble();

    boolean getBoolean();

    boolean isNull();
}
