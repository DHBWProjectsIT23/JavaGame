package org.itdhbw.futurewars.exceptions;

/**
 * The type Custom exception.
 */
public abstract class CustomException extends Exception {
    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     */
    protected CustomException(String message) {
        super(message);
    }
}
