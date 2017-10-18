package me.oegodf.mta.reader;

public class LogLoadingResult {
    private MtaErrorList mErrors;
    private int mTotalErrors;
    private int mReadyNumbers;
    private boolean mCompleted;

    public LogLoadingResult() {
        mTotalErrors = 1;
    }

    public double getProgress() {
        return (double) mReadyNumbers/mTotalErrors;
    }

    public MtaErrorList getErrors() {
        return mErrors;
    }

    public void setErrors(MtaErrorList errors) {
        mErrors = errors;
    }

    public void setTotalErrors(int totalErrors) {
        mTotalErrors = totalErrors;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public int getReadyNumbers() {
        return mReadyNumbers;
    }

    public void setReadyNumbers(int readyNumbers) {
        mReadyNumbers = readyNumbers;
    }
}
