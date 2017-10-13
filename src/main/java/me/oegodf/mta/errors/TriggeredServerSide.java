package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;

public class TriggeredServerSide extends ErrorSuggestion implements Solvable {
    @Override
    public String getDescription(MtaError error) {
        return null;
    }

    @Override
    public String getSuggestion(MtaError error) {
        return null;
    }

    @Override
    public boolean checkErrorPasses(MtaError error) {
        return error.getErrorText().contains("triggered serverside event");
    }

    @Override
    public ErrorLines getErrorLines(MtaError error) {
        return null;
    }

    @Override
    public String getSolution(MtaError error) {
        return null;
    }
}
