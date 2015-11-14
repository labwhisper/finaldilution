package com.druidpyrcel.biotech.finaldilution.model;

public class Compound {
    private int id;
    private String shortName;
    private String longName;
    private String chemicalFormula;
    private String iupacName;
    private double molarMass;

    public Compound() {
    }

    public Compound(String shortName, double molarMass) {
        super();
        this.shortName = shortName;
        this.molarMass = molarMass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

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

    public double getMolarMass() {
        return molarMass;
    }

    public void setMolarMass(double molarMass) {
        this.molarMass = molarMass;
    }

    @Override
    public String toString() {
        return shortName + " [" + molarMass + "]";
    }
}
