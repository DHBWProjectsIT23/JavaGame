package org.itdhbw.futurewars.exceptions;

/**
 * The type Failed to retrieve files exception.
 */
public class FailedToRetrieveFilesException extends CustomException {
    /**
     * Instantiates a new Failed to retrieve files exception.
     *
     * @param dir the dir
     */
    public FailedToRetrieveFilesException(String dir) {
        super("Failed to load files from directory: " + dir);
    }
}
