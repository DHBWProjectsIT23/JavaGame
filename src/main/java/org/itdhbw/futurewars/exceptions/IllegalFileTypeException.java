package org.itdhbw.futurewars.exceptions;

/**
 * The type Failed to load texture exception.
 */
public class IllegalFileTypeException extends CustomException {
    public IllegalFileTypeException(String file, String fileType) {
        super("Illegal file type: " + file + " is not a valid " + fileType +
              " file");
    }
}