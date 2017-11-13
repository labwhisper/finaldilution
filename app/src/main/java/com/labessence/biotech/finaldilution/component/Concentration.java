package com.labessence.biotech.finaldilution.component;

public class Concentration {

    double concentration;
    private ConcentrationType type;

    public Concentration() {
    }

    public Concentration(double concentration, ConcentrationType type) {
        this.concentration = concentration;
        this.type = type;
    }

    public double getConcentration() {
        return concentration;
    }

    public void setConcentration(double concentration) {
        this.concentration = concentration;
    }

    public ConcentrationType getType() {
        return type;
    }

    public void setType(ConcentrationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getConcentration() + " [" + type.toString() + "]";
    }

    /**
     * Calculate desired mass depending on desired concentration
     *
     * @param volume - volume[ml] of the final solution
     * @return - desired mass[g]
     */
    double calcDesiredMass(double volume, double molarMass) {
        //TODO make abstract
        return 0;
    }

    ;

    /**
     * Calculate component volume using calculated desired mass
     *
     * @param mass - mass[g] required in final solution
     * @return - volume[ml] of compound to be taken
     */
    double calcVolumeForDesiredMass(double mass, double molarMass) {
        return 0;
    }
}
