package me.oegodf.mta.errors;

public class ErrorLines {
    private int mStartIndex;
    private String[] mErrorLines;
    private int mErrorLineNum;

    public ErrorLines(String[] lines, int index, String errorLineText) {
        mErrorLines = lines;
        mStartIndex = index;
        if (errorLineText != null) {
            int i = 0;
            for (String line : lines) {
                i++;
                if (line.equals(errorLineText)) {
                    break;
                }
            }
            mErrorLineNum = i;
        } else {
            mErrorLineNum = -1;
        }
    }

    public ErrorLines(String[] lines, int index, int errorLine) {
        mErrorLines = lines;
        mStartIndex = index;
        mErrorLineNum = errorLine;
    }

    public int[] getErrorLinePosition() {
        return new int[]{calculateLengthBefore(), getErrorLineLength()};
    }

    private int calculateLengthBefore() {
        int length = 0;
        for (int i = 0; i < mErrorLineNum; i++) {
            length = length + mErrorLines[i].length();
        }
        return length + mErrorLineNum;
    }

    private int getErrorLineLength() {
        return mErrorLines[mErrorLineNum].length();
    }

    public int getStartIndex() {
        return mStartIndex;
    }

    public String[] getErrorLines() {
        return mErrorLines;
    }

    public int getErrorLineNum() {
        return mErrorLineNum;
    }

    public String getErrorLine() {
        return mErrorLines[mErrorLineNum];
    }
}
