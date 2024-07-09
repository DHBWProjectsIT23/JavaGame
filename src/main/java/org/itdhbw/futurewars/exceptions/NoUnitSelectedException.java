package org.itdhbw.futurewars.exceptions;

public class NoUnitSelectedException extends CustomException {
    public NoUnitSelectedException() {
        super("Tried to retrieve selected unit, but no unit is selected");
    }
}
