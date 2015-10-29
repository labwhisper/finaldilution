package com.druidpyrcel.biotech.finaldilution.model;

/**
 * Created by dawid.chmielewski on 10/10/2015.
 */
public class Compound {
    private int id;
    private String shortName;
    private String longName;
    private String chemicalFormula;
    private String iupacName;
    private float molarMass;

    public Compound() {
    }

    public Compound(String shortName, float molarMass) {
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

    public float getMolarMass() {
        return molarMass;
    }

    public void setMolarMass(float molarMass) {
        this.molarMass = molarMass;
    }

    @Override
    public String toString() {
        return shortName + " [" + molarMass + "]";
    }
}
