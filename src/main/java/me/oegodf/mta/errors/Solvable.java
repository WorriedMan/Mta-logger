package me.oegodf.mta.errors;

import me.oegodf.mta.reader.MtaError;

import java.util.List;

public interface Solvable {
    String getSolution(MtaError error);
}
