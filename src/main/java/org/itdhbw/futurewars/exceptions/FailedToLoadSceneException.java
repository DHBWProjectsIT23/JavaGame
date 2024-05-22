package org.itdhbw.futurewars.exceptions;

public class FailedToLoadSceneException extends CustomException {

    public FailedToLoadSceneException(String scene) {
        super("Failed to load scene: " + scene);
    }
}
