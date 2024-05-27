package org.itdhbw.futurewars.exceptions;

public class IllegalFileTypeException extends CustomException {
    public IllegalFileTypeException(String file, String fileType) {
        super("Illegal file type: " + file + " is not a valid " + fileType +
              " file");
    }
}