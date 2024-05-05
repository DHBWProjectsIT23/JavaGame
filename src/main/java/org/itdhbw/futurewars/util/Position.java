package org.itdhbw.futurewars.util;

public class Position {
    private final boolean isStatic;
    private int x;
    private int y;

    public Position(final int x, final int y, final Boolean isStatic) {
        this.x = x;
        this.y = y;
        this.isStatic = isStatic;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        if (isStatic) {
            throw new UnsupportedOperationException("Cannot change position of static object");
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                       "x=" + x +
                       ", y=" + y +
                       '}';
    }
}
