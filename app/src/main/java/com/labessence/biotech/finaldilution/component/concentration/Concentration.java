package com.labessence.biotech.finaldilution.component.concentration;

abstract public class Concentration {

    double concentration;
    private ConcentrationType type;

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
    public abstract double calcDesiredMass(double volume, double molarMass);

    /**
     * Calculate component volume using calculated desired mass
     *
     * @param mass - mass[g] required in final solution
     * @return - volume[ml] of compound to be taken
     */
    public abstract double calcVolumeForDesiredMass(double mass, double molarMass);
}
