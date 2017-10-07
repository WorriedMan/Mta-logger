package me.oegodf.mta.errors;

import me.oegodf.mta.MtaError;

public abstract class ErrorSuggestion {
        public abstract String getDescription(MtaError error);
        public abstract String getSuggestion(MtaError error);
        public abstract boolean checkErrorPasses(MtaError error);

}
