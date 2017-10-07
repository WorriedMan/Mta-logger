package me.oegodf.mta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        ErrorReader reader = new ErrorReader("server.log");
        reader.load();
    }
}
