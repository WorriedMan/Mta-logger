package me.oegodf.mta.reader;

import me.oegodf.mta.errors.*;

import java.util.ArrayList;
import java.util.List;

public class ErrorSuggestionParser {
    private List<ErrorSuggestion> mErrorSuggestions;

    ErrorSuggestionParser() {
        mErrorSuggestions = new ArrayList<>();
        mErrorSuggestions.add(new CallToNonRunning());
        mErrorSuggestions.add(new ConcatenateNilValue());
        mErrorSuggestions.add(new LineDeprecated());
    }

    List<ErrorSuggestion> getErrorSuggestions() {
        return mErrorSuggestions;
    }

    ErrorSuggestion getErrorSuggestion(MtaError error) {
        for (ErrorSuggestion suggestion : mErrorSuggestions) {
            if (suggestion.checkErrorPasses(error)) {
                error.setSuggestion(suggestion);
                return suggestion;
            }
        }
        return null;
    }
}
