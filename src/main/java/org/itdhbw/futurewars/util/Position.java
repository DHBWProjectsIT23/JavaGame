package org.itdhbw.futurewars.util;

public class Position {
    private final Boolean isStatic;
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

    public void setX(final int x) {
        if (Boolean.TRUE.equals(isStatic)) {
            throw new UnsupportedOperationException("Cannot change static position");
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        if (Boolean.TRUE.equals(isStatic)) {
            throw new UnsupportedOperationException("Cannot change static position");
        }
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
