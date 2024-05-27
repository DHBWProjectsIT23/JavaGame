package org.itdhbw.futurewars.exceptions;

public class InvalidFileFormatException extends CustomException {
    public InvalidFileFormatException(String file) {
        super("Invalid file format: " + file);
    }
}
