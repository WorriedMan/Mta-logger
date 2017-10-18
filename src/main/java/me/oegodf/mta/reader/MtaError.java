package me.oegodf.mta.reader;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import me.oegodf.mta.errors.ErrorSuggestion;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MtaError {
    private MtaLab.Error mType;
    private String mErrorText;
    private String mFileName;
    private String mResource;
    private int mFileLine;
    private int mDupAmount;
    private ErrorSuggestion mSuggestion;
    private Date mDate;
    private int mServerStartId;
    private String mHash;
    private boolean mToDelete;
    private SimpleDateFormat mDateFormat;

//    private StringProperty mErrorTextProperty;
//    private StringProperty mResourceProperty;
//    private IntegerProperty mDupProperty;
//    private StringProperty mDateProperty;

    public MtaError(MtaLab.Error type, String resource, String errorText, String fileName, int fileLine, int dup, Date date, int serverStartId, SimpleDateFormat df) {
        mType = type;
        mResource = resource;
        mErrorText = errorText;
        mFileName = fileName;
        mFileLine = fileLine;
        mDupAmount = dup;
        mDate = date;
        mServerStartId = serverStartId;
        mDateFormat = df;

//        mErrorTextProperty = new SimpleStringProperty(mErrorText);
//        mResourceProperty = new SimpleStringProperty(resource);
//        mDupProperty = new SimpleIntegerProperty(mDupAmount);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        mDateProperty = new SimpleStringProperty(dateFormat.format(date));

        generateErrorHash();
    }

    private void generateErrorHash() {
        try {
            String tempString = mResource+mErrorText+mFileName+mFileName+mDate.getTime();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(tempString.getBytes("UTF-8"));
            byte[] hashArray = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aHashArray : hashArray) {
                sb.append(Integer.toHexString((aHashArray & 0xFF) | 0x100).substring(1, 3));
            }
            mHash = sb.toString();
        } catch (Exception e) {
            mHash = "";
        }
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

    public void setDupAmount(int dupAmount) {
        mDupAmount = dupAmount;
//        mDupProperty.setValue(dupAmount);
    }

    public void setSuggestion(ErrorSuggestion suggestion) {
        mSuggestion = suggestion;
    }

    public ErrorSuggestion getSuggestion() {
        return mSuggestion;
    }

//    public StringProperty getErrorTextProperty() {
//        return mErrorTextProperty;
//    }
    public StringProperty getErrorTextProperty() {
        return new SimpleStringProperty(mErrorText);
    }

//    public StringProperty getResourceProperty() {
//        return mResourceProperty;
//    }
    public StringProperty getResourceProperty() {
        return new SimpleStringProperty(mResource);
    }

//    public IntegerProperty getDupProperty() {
//        return mDupProperty;
//    }
    public IntegerProperty getDupProperty() {
        return new SimpleIntegerProperty(mDupAmount);
    }

//    public StringProperty getDateProperty() {
//        return mDateProperty;
//    }
    public StringProperty getDateProperty() {
        return new SimpleStringProperty(mDateFormat.format(mDate));
    }

    public int getServerStartId() {
        return mServerStartId;
    }

    public String getHash() {
        return mHash;
    }

    public boolean isToDelete() {
        return mToDelete;
    }

    public void setToDelete(boolean toDelete) {
        mToDelete = toDelete;
    }
}
