package me.oegodf.mta.reader;

import me.oegodf.mta.errors.CallToNonRunning;
import me.oegodf.mta.errors.ConcatenateNilValue;
import me.oegodf.mta.errors.ErrorSuggestion;
import me.oegodf.mta.errors.Solvable;

import java.util.ArrayList;
import java.util.List;

public class ErrorParser {
    private List<ErrorSuggestion> mErrorSuggestions;

    ErrorParser() {
        mErrorSuggestions = new ArrayList<>();
        mErrorSuggestions.add(new CallToNonRunning());
        mErrorSuggestions.add(new ConcatenateNilValue());
    }

    List<ErrorSuggestion> getErrorSuggestions() {
        return mErrorSuggestions;
    }

    String getErrorSuggestion(MtaError error) {
        for (ErrorSuggestion suggestion : mErrorSuggestions) {
            if (suggestion.checkErrorPasses(error)) {
                error.setSuggestion(suggestion);
                System.out.println("------------------------");
                System.out.println("ERROR: " + error.getErrorText());
                System.out.println("DESCRIPT: " + suggestion.getDescription(error));
                System.out.println("SUGGEST: " + suggestion.getSuggestion(error));
                System.out.println("SERV START: " + error.getServerStartId());
                if (suggestion instanceof Solvable) {
                    String solution = ((Solvable) suggestion).getSolution(error);
                    if (solution != null) {
                        System.out.println("SOLUTION: " + solution);
                    }
                }
                return suggestion.getSuggestion(error);
            }
        }
        return null;
    }
}
