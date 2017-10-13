package me.oegodf.mta.resources;

import java.util.ArrayList;
import java.util.List;

public class FileLines {
    private int mErrorLineNum;
    private int mStartLine;
    private int mEndLine;
    private List<String> mLines;

    FileLines() {
        this(null);
    }

    FileLines(List<String> lines) {
        this(lines, -1, -1);
    }

    FileLines(List<String> lines, Integer startLine) {
        this(lines, startLine, startLine);
    }

    FileLines(List<String> lines, Integer startLine, Integer endLine) {
        this(lines, startLine, endLine, -1);
    }

    FileLines(List<String> lines, Integer startLine, Integer endLine, Integer errorLineNum) {
        if (startLine == null) {
            startLine = -1;
        }
        if (endLine == null) {
            endLine = startLine;
        }
        if (errorLineNum == null) {
            errorLineNum = -1;
        }
        mErrorLineNum = errorLineNum;
        mStartLine = startLine;
        mEndLine = endLine;
        mLines = lines;
    }

    String[] toArray() {
        return mLines.toArray(new String[]{});
    }

    public int getErrorLineNum() {
        return mErrorLineNum;
    }

    public int getStartLine() {
        return mStartLine;
    }
}
