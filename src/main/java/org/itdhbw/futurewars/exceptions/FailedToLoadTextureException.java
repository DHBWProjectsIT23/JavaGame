package org.itdhbw.futurewars.exceptions;

/**
 * The type Failed to load texture exception.
 */
public class FailedToLoadTextureException extends CustomException {
    /**
     * Instantiates a new Failed to load texture exception.
     *
     * @param textureType the texture type
     * @param texture     the texture
     */
    public FailedToLoadTextureException(String textureType, String texture) {
        super("Failed to load " + textureType + " texture: " + texture);
    }

    /**
     * Instantiates a new Failed to load texture exception.
     *
     * @param texture the texture
     */
    public FailedToLoadTextureException(String texture) {
        super("Failed to load texture: " + texture);
    }
}
