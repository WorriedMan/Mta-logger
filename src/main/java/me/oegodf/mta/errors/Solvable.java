package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;

public interface Solvable {
    public String getSolution(MtaError error);
}
