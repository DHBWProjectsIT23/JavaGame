package org.itdhbw.futurewars.game.utils;

/**
 * The type Position.
 */
public class Position {
    private final boolean isStatic;
    private int x;
    private int y;

    /**
     * Instantiates a new Position.
     *
     * @param x        the x
     * @param y        the y
     * @param isStatic the is static
     */
    public Position(final int x, final int y, final Boolean isStatic) {
        this.x = x;
        this.y = y;
        this.isStatic = isStatic;
    }

    /**
     * Instantiates a new Position.
     *
     * @param x the x
     * @param y the y
     */
    public Position(final int x, final int y) {
        this(x, y, false);
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }


    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets position.
     *
     * @param x the x
     * @param y the y
     */
    public void setPosition(int x, int y) {
        if (isStatic) {
            throw new UnsupportedOperationException(
                    "Cannot change position of static object");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate distance int.
     *
     * @param position the position
     * @return the int
     */
    public int calculateDistance(Position position) {
        int xDistance = Math.abs(this.x - position.getX());
        int yDistance = Math.abs(this.y - position.getY());
        return xDistance + yDistance;
    }

    @Override
    public String toString() {
        return "Position{" + "x=" + x + ", y=" + y + '}';
    }
}
