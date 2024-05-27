package org.itdhbw.futurewars.exceptions;

/**
 * The type Failed to load file exception.
 */
public class InvalidFileFormatException extends CustomException {
    /**
     * Instantiates a new Failed to load file exception.
     *
     * @param file the file
     */
    public InvalidFileFormatException(String file) {
        super("Invalid file format: " + file);
    }
}
