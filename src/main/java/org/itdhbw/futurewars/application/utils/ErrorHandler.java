package org.itdhbw.futurewars.application.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ErrorHandler {
    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getSimpleName());

    private static final Map<Exception, String> EXCEPTIONS = new HashMap<>();
    private static final IntegerProperty errorCount = new SimpleIntegerProperty(0);
    private static Pair<Exception, String> lastException;

    private ErrorHandler() {
        // Prevent instantiation
    }

    public static void addVerboseException(Exception e, String message) {
        addException(e, message);
        showErrorPopup();
    }

    public static void addException(Exception e, String message) {
        StackTraceElement lastElement = e.getStackTrace()[0];
        LOGGER.severe(lastElement.getClassName() + " - " + lastElement.getLineNumber() + ": " + message + " - " +
                      e.getMessage());
        lastException = new Pair<>(e, message);
        EXCEPTIONS.put(e, message);
        errorCount.set(EXCEPTIONS.size());
    }

    public static void showErrorPopup() {
        LOGGER.info("Showing error popup...");
        if (EXCEPTIONS.isEmpty()) {
            LOGGER.info("No exceptions to show!");
            return;
        }
        if (EXCEPTIONS.size() > 1) {
            ErrorPopup.showErrorPopup("Multiple errors occurred!");
        } else {
            ErrorPopup.showErrorPopup(lastException.getValue(), lastException.getKey());
        }
    }

    public static Map<Exception, String> getExceptions() {
        return EXCEPTIONS;
    }

    public static IntegerProperty errorCountProperty() {
        return errorCount;
    }
}
