package org.itdhbw.futurewars.exceptions;

public class FileDoesNotExistException extends CustomException {
    public FileDoesNotExistException(String file) {
        super("File does not exist: " + file);
    }
}
