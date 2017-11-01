package com.labessence.biotech.finaldilution.compound;

import com.labessence.biotech.finaldilution.genericitem.Item;

public class Compound implements Item {

    private String shortName;
    private double molarMass;
    private String longName;
    private String chemicalFormula;
    private String iupacName;

    public Compound(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public double getMolarMass() {
        return molarMass;
    }

    public void setMolarMass(double molarMass) {
        this.molarMass = molarMass;
    }

    @Override
    public String toString() {
        return getShortName() + " [" + getMolarMass() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Compound compound = (Compound) o;

        if (!getShortName().equals(compound.getShortName())) return false;
        return chemicalFormula != null ? chemicalFormula.equals(compound.chemicalFormula) : compound.chemicalFormula == null;

    }

    @Override
    public int hashCode() {
        int result = getShortName().hashCode();
        result = 31 * result + (chemicalFormula != null ? chemicalFormula.hashCode() : 0);
        return result;
    }

    @Override
    public String getName() {
        return shortName;
    }
}
