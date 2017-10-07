package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;

public class CallToNonRunning extends ErrorSuggestion {
    private String mText = "Попытка обращения к не запущенному ресурсу %s.";
    private String mSuggestion = "Добавьте в meta.xml файл ресурса %s строку <include resource=\"%s\">";

    @Override
    public String getDescription(MtaError error) {
        String errorText = error.getErrorText();
        String resourceName = getResourceName(errorText);
        return String.format(mText, resourceName);
    }

    @Override
    public String getSuggestion(MtaError error) {
        String errorText = error.getErrorText();
        String resourceName = getResourceName(errorText);
        return String.format(mSuggestion,error.getResource(),resourceName);
    }

    @Override
    public boolean checkErrorPasses(MtaError error) {
        String keyword = "Call to non-running server resource";
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


}
