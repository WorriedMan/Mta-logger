package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.resources.FileLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineDeprecated extends ErrorSuggestion {
    private Pattern functionsPattern = Pattern.compile("(\\b\\w+\\b) is deprecated and may not work in future versions\\. Please replace with (\\b\\w+\\b)");

    @Override
    public String getDescription(MtaError error) {
        String errorText = error.getErrorText();
        String[] functions = getFunctionName(errorText);
        return "Функция "+functions[0]+" является устаревшей и может не работать в будущих версиях МТА";
    }

    @Override
    public String getSuggestion(MtaError error) {
        String errorText = error.getErrorText();
        String[] functions = getFunctionName(errorText);
        return "Замените функцию "+functions[0]+" на функцию "+functions[1]+" в указанной строке (возможна смена аргументов)";
    }

    @Override
    public boolean checkErrorPasses(MtaError error) {
        String keyword = "is deprecated and may not work in future versions";
        return error.getErrorText().contains(keyword);
    }

    private String[] getFunctionName(String line) {
        String deprecatedFunction = "";
        String newFunction = "";
        Matcher matcher = functionsPattern.matcher(line);
        if (matcher.find()) {
            deprecatedFunction = matcher.group(1);
            newFunction = matcher.group(2);
        }
        return new String[]{deprecatedFunction, newFunction};
    }
}
