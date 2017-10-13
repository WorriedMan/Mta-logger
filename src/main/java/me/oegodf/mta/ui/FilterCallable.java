package me.oegodf.mta.ui;

import javafx.collections.ObservableList;
import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.reader.MtaErrorList;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FilterCallable implements Callable<MtaErrorList> {
    private MtaErrorList mList;
    private SearchFilter mSearchFilter;

    FilterCallable(MtaErrorList list, SearchFilter searchFilter) {
        mList = list;
        mSearchFilter = searchFilter;
    }

    @Override
    public MtaErrorList call() throws Exception {
        TimeUnit.MILLISECONDS.sleep(300);
        return mList.stream().filter(error -> {
            boolean lastLaunchPass = checkLastLaunchPass(error);
            boolean textPass = checkTextPass(error);
            return lastLaunchPass && textPass;
        }).collect(Collectors.toCollection(MtaErrorList::new));

    }

    private boolean checkTextPass(MtaError error) {
        String textFilter = mSearchFilter.getTextFilter();
        return Objects.equals(textFilter, "") || error.getErrorText().toLowerCase().contains(textFilter) || error.getResource().toLowerCase().contains(textFilter) || error.getFileName().toLowerCase().contains(textFilter);
    }

    private boolean checkLastLaunchPass(MtaError error) {
        if (mSearchFilter.getLastLaunch() != -1) {
            return error.getServerStartId() == mList.getLastServerStartId();
        }
        return true;
    }
}
