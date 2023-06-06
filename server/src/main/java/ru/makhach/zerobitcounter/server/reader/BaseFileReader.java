package ru.makhach.zerobitcounter.server.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseFileReader implements FileReader {
    public byte[] readBytes(String stringPath) {
        Path path = Paths.get(stringPath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }
}
