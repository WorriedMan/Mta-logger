package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.resources.FileLib;
import me.oegodf.mta.resources.FileLoader;

public abstract class ErrorSuggestion {
    public abstract String getDescription(MtaError error);

    public abstract String getSuggestion(MtaError error);

    public abstract boolean checkErrorPasses(MtaError error);

    public ErrorLines getErrorLines(MtaError error) {
        FileLoader luaFile = FileLib.get().file(error.getFileName());
        luaFile.load();
        if (luaFile.loaded()) {
            return luaFile.getErrorLines(error.getFileLine());
        }
        return null;
    }
}
