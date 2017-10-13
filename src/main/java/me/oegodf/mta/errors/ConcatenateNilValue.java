package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.resources.FileLoader;

public class ConcatenateNilValue extends ErrorSuggestion implements Solvable {

    @Override
    public String getDescription(MtaError error) {
        String errorText = error.getErrorText();
        String resourceName = getResourceName(errorText);
        String text = "Попытка использовать оператор .. вместе с переменной, которая является nil";
        return String.format(text, resourceName);
    }

    @Override
    public String getSuggestion(MtaError error) {
        String errorText = error.getErrorText();
        String resourceName = getResourceName(errorText);
        String suggestion = "Добавьте проверку на nil (например, if (abc == nil) then, либо окружите данную переменную скобками. Например:\noutputChatBox(\"Игрок говорит: \"..abc) замените на outputChatBox(\"Игрок говорит: \"..(abc or \"\"))";
        return suggestion;
    }

    @Override
    public boolean checkErrorPasses(MtaError error) {
        String keyword = "attempt to concatenate a nil value";
        return error.getErrorText().contains(keyword);
    }

    private String getResourceName(String line) {
        int endPosition = line.indexOf(')');
        if (endPosition > 0) {
            return line.substring(46, endPosition);
        } else {
            return "";
        }
    }

    @Override
    public ErrorLines getErrorLines(MtaError error) {
        FileLoader luaFile = new FileLoader(error.getFileName());
        luaFile.load();
        if (luaFile.loaded()) {
            return luaFile.getErrorLines(error.getFileLine());
        }
        return null;
    }

    @Override
    public String getSolution(MtaError error) {
        FileLoader luaFile = new FileLoader(error.getFileName());
        luaFile.load();
        if (luaFile.loaded()) {
            return luaFile.getFileLine(error.getFileLine());
        }
        return null;
    }
}
