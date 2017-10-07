package me.oegodf.mta.reader;

public class MtaError {
    private MtaLab.Error mType;
    private String mErrorText;
    private String mFileName;
    private String mResource;
    private int mFileLine;
    private int mDupAmount;

    public MtaError(MtaLab.Error type, String resource, String errorText, String fileName, int fileLine, int dup) {
        mType = type;
        mResource = resource;
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

    public String getResource() {
        return mResource;
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
