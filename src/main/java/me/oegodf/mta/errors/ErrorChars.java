package me.oegodf.mta.errors;

public class ErrorChars {
    private String mText;
    private int mStart;
    private int mEnd;

    ErrorChars(String text, int start, int end) {
        mText = text;
        mStart = start;
        mEnd = end;
    }

    public String getText() {
        return mText;
    }

    public int getStart() {
        return mStart;
    }

    public int getEnd() {
        return mEnd;
    }
}
