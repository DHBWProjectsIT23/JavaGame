package org.itdhbw.futurewars.exceptions;

public class FailedToRetrieveFilesException extends CustomException {
    public FailedToRetrieveFilesException(String dir) {
        super("Failed to load files from directory: " + dir);
    }
}
