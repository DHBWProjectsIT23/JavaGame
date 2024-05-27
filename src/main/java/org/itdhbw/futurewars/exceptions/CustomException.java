package org.itdhbw.futurewars.exceptions;

public abstract class CustomException extends Exception {
    protected CustomException(String message) {
        super(message);
    }
}
