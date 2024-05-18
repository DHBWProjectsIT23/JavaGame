package org.itdhbw.futurewars.exceptions;

public class FailedToLoadFileException extends CustomException {
    public FailedToLoadFileException(String file) {
        super("Failed to load file: " + file);
    }
}
