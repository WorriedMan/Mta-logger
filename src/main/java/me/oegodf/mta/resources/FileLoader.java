package me.oegodf.mta.resources;

import me.oegodf.mta.errors.ErrorLines;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileLoader {
    private String mPath;
    private String[] mFileLines;
    private static int LINES_TOP = 15;
    private static int LINES_BOTTOM = 15;

    FileLoader(String path) {
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

    public ErrorLines getErrorLines(int line) {
        int startLine = line-LINES_TOP;
        int endLine = line+LINES_BOTTOM;
        if (startLine < 0) {
            startLine = 0;
        }
        if (endLine > mFileLines.length) {
            endLine = mFileLines.length;
        }
        FileLines lines = getFileLinesArrange(startLine,endLine);
        return new ErrorLines(lines.toArray(),lines.getStartLine(),getFileLine(line));
    }

    private FileLines getFileLinesArrange(int startLine, int endLine) {
        List<String> lines = new ArrayList<>();
        Integer startedAtLine = null;
        Integer endedAtLine  = null;
        for (int i = startLine; i < endLine+1; i++ ) {
            String line = getFileLine(i);
            if (line != null) {
                if (startedAtLine == null) {
                    startedAtLine = i;
                } else {
                    endedAtLine = i;
                }
                lines.add(line);
            }
        }
        return new FileLines(lines,startedAtLine,endedAtLine);
    }

    public String getFileLine(int lineNum) {
        if (loaded()) {
            lineNum = lineNum-1;
            if (lineNum <= mFileLines.length && lineNum >= 0) {
                return mFileLines[lineNum];
            }
        }
        return null;
    }

    public String getPath() {
        return mPath;
    }
}
