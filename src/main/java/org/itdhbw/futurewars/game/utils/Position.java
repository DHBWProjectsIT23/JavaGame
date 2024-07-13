package org.itdhbw.futurewars.game.utils;

public class Position {
    private final boolean isConstant;
    private int x;
    private int y;

    public Position(final int x, final int y) {
        this(x, y, false);
    }

    public Position(final int x, final int y, final Boolean isConstant) {
        this.x = x;
        this.y = y;
        this.isConstant = isConstant;
    }

    public void setPosition(int x, int y) {
        if (isConstant) {
            throw new UnsupportedOperationException("Cannot change position of static object");
        }
        this.x = x;
        this.y = y;
    }

    public int calculateDistance(Position position) {
        int xDistance = Math.abs(this.x - position.getX());
        int yDistance = Math.abs(this.y - position.getY());
        return xDistance + yDistance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(isConstant);
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Position position = (Position) o;
        return isConstant == position.isConstant && x == position.x && y == position.y;
    }
}
