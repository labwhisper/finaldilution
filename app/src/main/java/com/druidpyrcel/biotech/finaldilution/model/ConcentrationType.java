package com.druidpyrcel.biotech.finaldilution.model;

public enum ConcentrationType {
    PERCENTAGE(0),
    MOLAR(1),
    MILIMOLAR(2),
    MILIGRAM_PER_MILLILITER(3);

    private final int value;

    ConcentrationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
