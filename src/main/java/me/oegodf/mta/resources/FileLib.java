package me.oegodf.mta.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileLib {
    private static final FileLib instance = new FileLib();
    private List<FileLoader> mFiles;

    private FileLib() {
        mFiles = new ArrayList<>();
    }

    public static FileLib get() {
        return instance;
    }

    public FileLoader file(String path) {
        Optional<FileLoader> optionalFile = mFiles.stream()
                .filter(loadedFile -> loadedFile.getPath().equals(path))
                .findFirst();
        return optionalFile.orElseGet(() -> loadNewFile(path));
    }

    private FileLoader loadNewFile(String path) {
        FileLoader file = new FileLoader(path);
        mFiles.add(file);
        return file;
    }
}
