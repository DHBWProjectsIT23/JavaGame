package org.itdhbw.futurewars.application.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ErrorHandler {
    private static final Logger LOGGER = LogManager.getLogger(ErrorHandler.class);

    private static final Map<Exception, String> EXCEPTIONS = new HashMap<>();
    private static final IntegerProperty errorCount = new SimpleIntegerProperty(0);
    private static Pair<Exception, String> lastException;

    private ErrorHandler() {
        // Prevent instantiation
    }

    public static void addException(Exception e, String message) {
        StackTraceElement lastElement = e.getStackTrace()[0];
        LOGGER.error("{} - {}: {} - {}", lastElement.getClassName(), lastElement.getLineNumber(), message,
                     e.getMessage());
        lastException = new Pair<>(e, message);
        EXCEPTIONS.put(e, message);
        errorCount.set(EXCEPTIONS.size());
    }

    public static void logExceptions() {
        for (Map.Entry<Exception, String> e : EXCEPTIONS.entrySet()) {
            LOGGER.error("{}: {}", e.getValue(), e.getKey().getMessage());
        }
    }

    public static Pair<Exception, String> getLastError() {
        return lastException;
    }

    public static Map<Exception, String> getExceptions() {
        return EXCEPTIONS;
    }

    public static boolean hasErrors() {
        return !EXCEPTIONS.isEmpty();
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

    public static IntegerProperty errorCountProperty() {
        return errorCount;
    }
}
