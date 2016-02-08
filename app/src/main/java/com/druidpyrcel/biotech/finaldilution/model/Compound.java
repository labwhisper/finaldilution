package com.druidpyrcel.biotech.finaldilution.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "COMPOUND".
 */
public class Compound implements java.io.Serializable {

    private String shortName;
    private double molarMass;

    // KEEP FIELDS - put your custom fields here
    private String longName;
    private String chemicalFormula;
    private String iupacName;
    // KEEP FIELDS END

    public Compound() {
    }

    public Compound(String shortName) {
        this.shortName = shortName;
    }

    public Compound(String shortName, double molarMass) {
        this.shortName = shortName;
        this.molarMass = molarMass;
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

    // KEEP METHODS - put your custom methods here
    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }

    public void setChemicalFormula(String chemicalFormula) {
        this.chemicalFormula = chemicalFormula;
    }

    public String getIupacName() {
        return iupacName;
    }

    public void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    @Override
    public String toString() {
        return getShortName() + " [" + getMolarMass() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Compound)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Compound secondCompound = (Compound) o;
        return getShortName().equals(secondCompound.getShortName());
    }
    // KEEP METHODS END

}
