package com.labessence.biotech.finaldilution.component;

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

    @Override
    public String toString() {
        switch (this.value) {
            case 0:
                return "%";
            case 1:
                return "M";
            case 2:
                return "mM";
            case 3:
                return "mg/ml";
            default:
                return "";
        }
    }
}
