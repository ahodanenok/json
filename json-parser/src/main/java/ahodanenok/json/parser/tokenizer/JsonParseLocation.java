package ahodanenok.json.parser.tokenizer;

public final class JsonParseLocation {

    private final int row;
    private final int col;
    private final int pos;

    JsonParseLocation(int row, int col, int pos) {
        this.row = row;
        this.col = col;
        this.pos = pos;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public int getPosition() {
        return pos;
    }
}
