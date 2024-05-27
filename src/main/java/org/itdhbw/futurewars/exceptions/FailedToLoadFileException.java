package org.itdhbw.futurewars.exceptions;

import java.io.File;

/**
 * The type Failed to load file exception.
 */
public class FailedToLoadFileException extends CustomException {
    /**
     * Instantiates a new Failed to load file exception.
     *
     * @param file the file
     */
    public FailedToLoadFileException(String file) {
        super("Failed to load file: " + file);
    }

    public FailedToLoadFileException(File file) {
        super("Failed to load file: " + file.toString());
    }
}
