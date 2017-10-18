package me.oegodf.mta.reader;

public class FileAndLineParseResult {
    private String mFile;
    private Integer mLine;
    private Integer mLineEndPosition;

    FileAndLineParseResult(String file, Integer line, Integer lineEndPosition) {
        mFile = file;
        mLine = line;
        mLineEndPosition = lineEndPosition;
    }

    public String getFile() {
        return mFile;
    }

    public Integer getLine() {
        return mLine;
    }

    public Integer getLineEndPosition() {
        return mLineEndPosition;
    }
}
