package ahodanenok.json.parser.pull;

public enum EventType {

    BEGIN_OBJECT,
    END_OBJECT,
    OBJECT_KEY,
    BEGIN_ARRAY,
    END_ARRAY,
    STRING,
    NUMBER,
    BOOLEAN,
    NULL
}
