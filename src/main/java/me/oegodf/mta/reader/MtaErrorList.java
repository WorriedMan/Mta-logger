package me.oegodf.mta.reader;

import java.util.ArrayList;

public class MtaErrorList extends ArrayList<MtaError> {
    private int mLastServerStartId;

    public int getLastServerStartId() {
        return mLastServerStartId;
    }

    public void setLastServerStartId(int lastServerStartId) {
        mLastServerStartId = lastServerStartId;
    }
}
