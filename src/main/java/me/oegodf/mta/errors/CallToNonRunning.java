package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;
import me.oegodf.mta.resources.FileLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallToNonRunning extends ErrorSuggestion {
    private Pattern resourcePattern = Pattern.compile("Call to non-running server resource \\((\\b\\w+\\b)\\)");

    @Override
    public String getDescription(MtaError error) {
        String errorText = error.getErrorText();
        String resourceName = getResourceName(errorText);
        String text = "Попытка обращения к не запущенному ресурсу %s";
        return String.format(text, resourceName);
    }

    @Override
    public String getSuggestion(MtaError error) {
        String errorText = error.getErrorText();
        String resourceName = getResourceName(errorText);
        String suggestion = "Добавьте в meta.xml файл ресурса %s строку <include resource=\"%s\">";
        return String.format(suggestion, error.getResource(), resourceName);
    }

    @Override
    public boolean checkErrorPasses(MtaError error) {
        String keyword = "Call to non-running server resource";
        return error.getErrorText().contains(keyword);
    }

    private String getResourceName(String line) {
        Matcher matcher = resourcePattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
