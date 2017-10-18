package me.oegodf.mta.reader;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import me.oegodf.mta.main.Settings;

import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ErrorReader implements FlowableOnSubscribe<LogLoadingResult> {
    private String mFile;
    private MtaLab mLab;
    private MtaErrorList mMtaErrorList;
    private ErrorSuggestionParser mErrorParser;
    private boolean mPrevLineIsServerStartLine;
    private int mPrevServerStartId;
    private MtaError mPrevError;
    private FlowableEmitter<LogLoadingResult> mEmitter;
    private LogLoadingResult mLoadingResult;
    private long mLastSize;
    private CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
            .onMalformedInput(CodingErrorAction.IGNORE);
    private long mReadOffset;

    public ErrorReader(String file) {
        mMtaErrorList = new MtaErrorList();
        mFile = file;
        mLab = new MtaLab();
        mErrorParser = new ErrorSuggestionParser();
        mPrevServerStartId = 0;
    }

    @Override
    public void subscribe(FlowableEmitter<LogLoadingResult> emitter) throws Exception {
        mEmitter = emitter;
        Path path = Paths.get(mFile);
        loadMtaErrors(path);
        watchFileChanges(path);
    }

    private void watchFileChanges(Path path) throws Exception {
        while (Files.exists(path)) {
            TimeUnit.SECONDS.sleep(1);
            if (Files.size(path) != mLastSize) {
//                loadMtaErrors(path);
            }
        }
        throw new FileNotFoundException();
    }

    private void loadMtaErrors(Path path) throws IOException {
        if (Files.exists(path)) {
            mLoadingResult = new LogLoadingResult();
            List<String> list = readAllLines(path);
            mLoadingResult.setTotalErrors(list.size());
            mMtaErrorList = new MtaErrorList();
            list.forEach(this::checkLine);
            mMtaErrorList.setLastServerStartId(mPrevServerStartId);
            mLoadingResult.setCompleted(true);
            sendLoadingResult(true);
            mLastSize = Files.size(path);
            mLoadingResult = new LogLoadingResult();
            mMtaErrorList = new MtaErrorList();
        }
    }

    private List<String> readAllLines(Path path) {
        List<String> result = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(path)){
            inputStream.skip(mReadOffset);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, decoder));
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                result.add(line);
            }
            reader.close();
            inputStream.close();
        } catch (IOException ignored) {
        }
        return result;
    }

    private void sendLoadingResult(boolean set) {
        if (set) {
            mLoadingResult.setErrors(mMtaErrorList);
        }
        mEmitter.onNext(mLoadingResult);
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
                if (mPrevError != null && Objects.equals(error.getHash(), mPrevError.getHash())) {
                    mPrevError.setDupAmount(mPrevError.getDupAmount() + error.getDupAmount() + 1);
                } else {
                    error.setSuggestion(mErrorParser.getErrorSuggestion(error));
                    mPrevError = error;
                    mMtaErrorList.add(error);
                    sendLoadingResult(false);
                }
            }

        }
        mLoadingResult.setReadyNumbers(mLoadingResult.getReadyNumbers() + 1);
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
            mPrevError.setDupAmount(mPrevError.getDupAmount() + error.getDupAmount() + 1);
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

    public boolean checkFileSize() {
        Path logPath = Paths.get(mFile);
        if (Files.exists(logPath)) {
            try {
                long size = Files.size(logPath);
                long maxSize = Settings.get().maxFileLength();
                if (size > maxSize) {
                    mReadOffset = size - maxSize;
                    return true;
                }
            } catch (IOException ignored) {
            }
        }
        return false;
    }
}
