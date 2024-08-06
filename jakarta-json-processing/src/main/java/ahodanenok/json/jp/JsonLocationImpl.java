package ahodanenok.json.jp;

import jakarta.json.stream.JsonLocation;

final class JsonLocationImpl implements JsonLocation {


    @Override
    public long getLineNumber() {
        return 0;
    }

    @Override
    public long getColumnNumber() {
        return 0;
    }

    @Override
    public long getStreamOffset() {
        return 0;
    }
}
