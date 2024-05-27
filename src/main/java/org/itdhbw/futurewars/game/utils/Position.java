package org.itdhbw.futurewars.game.utils;

public class Position {
    private final boolean isStatic;
    private int x;
    private int y;

    public Position(final int x, final int y, final Boolean isStatic) {
        this.x = x;
        this.y = y;
        this.isStatic = isStatic;
    }

    public Position(final int x, final int y) {
        this(x, y, false);
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        if (isStatic) {
            throw new UnsupportedOperationException(
                    "Cannot change position of static object");
        }
        this.x = x;
        this.y = y;
    }

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
