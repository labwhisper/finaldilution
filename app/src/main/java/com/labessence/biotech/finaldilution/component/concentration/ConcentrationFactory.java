package com.labessence.biotech.finaldilution.component.concentration;

public class ConcentrationFactory {
    public static Concentration createConcentration(ConcentrationType type, double amount) {
        switch (type) {
            case MOLAR:
                return new MolarConcentration(amount);
            case MILIMOLAR:
                return new MilimolarConcentration(amount);
            case PERCENTAGE:
                return new PercentageConcentration(amount);
            case MILIGRAM_PER_MILLILITER:
                return new MgMlConcentration(amount);
        }
        return null;
    }
}
