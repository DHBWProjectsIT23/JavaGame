package org.itdhbw.futurewars.exceptions;

public class FailedToLoadTextureException extends CustomException {
    public FailedToLoadTextureException(String textureType, String texture) {
        super("Failed to load " + textureType + " texture: " + texture);
    }

    public FailedToLoadTextureException(String texture) {
        super("Failed to load texture: " + texture);
    }
}
