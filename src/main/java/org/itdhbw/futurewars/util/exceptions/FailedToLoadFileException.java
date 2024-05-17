package org.itdhbw.futurewars.util.exceptions;

public class FailedToLoadFileException extends CustomException {
    public FailedToLoadFileException(String file) {
        super("Failed to load file: " + file);
    }
}
