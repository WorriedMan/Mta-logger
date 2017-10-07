package me.oegodf.mta.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileLoader {
    private String mPath;
    private String[] mFileLines;

    public FileLoader(String path) {
        mPath = "F:/server/mods/deathmatch/resources/" + path;
    }

    public void load() {
        try {
            Path path = Paths.get(mPath);
            mFileLines = Files.lines(path).toArray(String[]::new);
        } catch (IOException ignored) {
        }
    }

    public boolean loaded() {
        return mFileLines != null;
    }

    public String getFileLine(int lineNum) {
        if (loaded()) {
            lineNum = lineNum-1;
            if (lineNum <= mFileLines.length) {
                return mFileLines[lineNum];
            }
        }
        return null;
    }
}
