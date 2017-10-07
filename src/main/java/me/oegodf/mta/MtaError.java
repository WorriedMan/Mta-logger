package me.oegodf.mta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MtaError {
    private MtaLab.Error mType;
    private String mErrorText;
    private String mFileName;
    private int mFileLine;
    private int mDupAmount;

    public MtaError(MtaLab.Error type, String errorText, String fileName, int fileLine, int dup) {
        mType = type;
        mErrorText = errorText;
        mFileName = fileName;
        mFileLine = fileLine;
        mDupAmount = dup;
    }

    public MtaLab.Error getType() {
        return mType;
    }

    public String getErrorText() {
        return mErrorText;
    }

    public String getFileName() {
        return mFileName;
    }

    public int getFileLine() {
        return mFileLine;
    }
    public int getDupAmount() {
        return mDupAmount;
    }
}