package org.itdhbw.futurewars.exceptions;

import java.io.File;

public class FailedToLoadFileException extends CustomException {
    public FailedToLoadFileException(String file) {
        super("Failed to load file: " + file);
    }

    public FailedToLoadFileException(File file) {
        super("Failed to load file: " + file.toString());
    }
}
