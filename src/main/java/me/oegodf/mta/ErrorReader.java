package me.oegodf.mta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

class ErrorReader {
    private String mFile;
    private MtaLab mLab;

    ErrorReader(String file) {
        mFile = file;
        mLab = new MtaLab();
    }

    void load() throws IOException {
        Path path = Paths.get(mFile);
        Stream<String> stream = Files.lines(path);
        stream.forEach(this::checkLine);
    }

    private void checkLine(String line) {
        int length = line.length();
        if (length > 22) {
            MtaError error = mLab.createError(line);
        }
    }
}
