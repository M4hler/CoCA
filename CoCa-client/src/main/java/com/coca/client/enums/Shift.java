package com.coca.client.enums;

public enum Shift {
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT;

    public Shift next() {
        var values = Shift.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}
