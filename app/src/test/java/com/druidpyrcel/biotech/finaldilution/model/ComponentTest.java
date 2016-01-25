package com.druidpyrcel.biotech.finaldilution.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComponentTest {


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPercentageToMolar() throws Exception {
        double volume = 2.0;
        Compound compound = new Compound("MgCl2", 95.211);
        Concentration desiredConcentration = new Concentration(0.2, Concentration.ConcentrationType.MOLAR);
        Concentration stockConcentration = new Concentration(30, Concentration.ConcentrationType.PERCENTAGE);
        Component component = new Component(compound, desiredConcentration, stockConcentration);
        assertEquals(127, component.getQuantity(volume), 1);
    }

}