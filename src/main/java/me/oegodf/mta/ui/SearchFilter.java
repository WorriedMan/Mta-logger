package me.oegodf.mta.ui;

public class SearchFilter {
    private int mLastLaunch;
    private String mTextFilter;

    SearchFilter() {
        this(-1,"");
    }

    SearchFilter(int lastLaunch, String textFilter) {
        mLastLaunch = lastLaunch;
        mTextFilter = textFilter;
    }

    public int getLastLaunch() {
        return mLastLaunch;
    }

    public void setLastLaunch(int lastLaunch) {
        mLastLaunch = lastLaunch;
    }

    public String getTextFilter() {
        return mTextFilter;
    }

    public void setTextFilter(String textFilter) {
        mTextFilter = textFilter.toLowerCase();
    }
}
