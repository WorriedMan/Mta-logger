package me.oegodf.mta;

import me.oegodf.mta.errors.CallToNonRunning;
import me.oegodf.mta.errors.ErrorSuggestion;

import java.util.ArrayList;
import java.util.List;

public class ErrorParserLoader {
    private List<ErrorSuggestion> mErrorSuggestions;

    ErrorParserLoader() {
        mErrorSuggestions = new ArrayList<>();
        mErrorSuggestions.add(new CallToNonRunning());
    }

    List<ErrorSuggestion> getErrorSuggestions() {
        return mErrorSuggestions;
    }

    String getErrorSuggestion(MtaError error) {
        for (ErrorSuggestion suggestion : mErrorSuggestions) {
            if (suggestion.checkErrorPasses(error)) {
                System.out.println("------------------------");
                System.out.println("ERROR: " + error.getErrorText());
                System.out.println("DESCRIPT: " + suggestion.getDescription(error));
                System.out.println("SUGGEST: " + suggestion.getSuggestion(error));
                return suggestion.getSuggestion(error);
            }
        }
        return null;
    }
}
