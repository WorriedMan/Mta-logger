package me.oegodf.mta.reader;

import me.oegodf.mta.errors.ErrorChars;
import me.oegodf.mta.errors.ErrorLines;
import me.oegodf.mta.errors.ErrorSuggestion;
import me.oegodf.mta.errors.Solvable;
import me.oegodf.mta.ui.LoggerController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ErrorReader implements Callable<MtaErrorList> {
    private String mFile;
    private MtaLab mLab;
    private MtaErrorList mMtaErrorList;
    private ErrorParser mErrorParser;
    private boolean mPrevLineIsServerStartLine;
    private int mPrevServerStartId;
    private MtaError mPrevError;

    public ErrorReader(String file) {
        mMtaErrorList = new MtaErrorList();
        mFile = file;
        mLab = new MtaLab();
        mErrorParser = new ErrorParser();
        mPrevServerStartId = 0;
    }

    @Override
    public MtaErrorList call() throws Exception {
        Path path = Paths.get(mFile);
        Stream<String> stream = Files.lines(path);
        stream.forEach(this::checkLine);
        mMtaErrorList.forEach(this::checkDuppedErrors);
        deleteDuppedErrors();
        mMtaErrorList.setLastServerStartId(mPrevServerStartId);
        return mMtaErrorList;
    }

    private void checkLine(String line) {
        int length = line.length();
        if (mPrevLineIsServerStartLine) {
            if (line.contains("= Multi Theft Auto: San Andreas")) {
                mPrevServerStartId++;
            }
        }
        mPrevLineIsServerStartLine = checkLineIsServerStartLine(line);
        if (length > 22) {
            MtaError error = mLab.createError(line, mPrevServerStartId);
            if (error != null) {
                mErrorParser.getErrorSuggestion(error);
                mMtaErrorList.add(error);
            }
        }
    }

    private boolean checkLineIsServerStartLine(String line) {
        return Pattern.matches("={66}", line);
    }

    private void checkDuppedErrors(MtaError error) {
        if (mPrevError == null) {
            mPrevError = error;
            return;
        }
        if (Objects.equals(error.getHash(), mPrevError.getHash())) {
            mPrevError.setDupAmount(mPrevError.getDupAmount()+error.getDupAmount()+1);
            error.setToDelete(true);
        } else {
            mPrevError = error;
        }
    }

    private void deleteDuppedErrors() {
        mMtaErrorList = mMtaErrorList.stream()
                .filter(error -> !error.isToDelete())
                .collect(Collectors.toCollection(MtaErrorList::new));
    }
}
