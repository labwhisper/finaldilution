package com.labessence.biotech.finaldilution.component;

import com.labessence.biotech.finaldilution.compound.Compound;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ConcentrationCoarseTests {

    private double volume;
    private Compound compound;

    @Before
    public void setUp() throws Exception {
        volume = 2000; //[ml]
        compound = new Compound("NaOH", 40);
    }

    @Test
    public void testSolidToPercentage() throws Exception {
        Concentration desired = new PercentageConcentration(20);
        Component component = new Component(compound, desired);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testSolidToMolar() throws Exception {
        Concentration desired = new MolarConcentration(0.5);
        Component component = new Component(compound, desired);
        assertEquals(40, component.getQuantity(volume), 1);
    }

    @Test
    public void testSolidToMillimolar() throws Exception {
        Concentration desired = new MilimolarConcentration(75);
        Component component = new Component(compound, desired);
        assertEquals(6, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testSolidToMgMl() throws Exception {
        Concentration desired = new MgMlConcentration(200);
        Component component = new Component(compound, desired);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testPercentageToPercentage() throws Exception {
        Concentration desired = new PercentageConcentration(0.05);
        Concentration stock = new PercentageConcentration(40);
        Component component = new Component(compound, desired, stock);
        assertEquals(2.5, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testPercentageToMolar() throws Exception {
        Concentration desired = new MolarConcentration(0.1);
        Concentration stock = new PercentageConcentration(40);
        Component component = new Component(compound, desired, stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testPercentageToMillimolar() throws Exception {
        Concentration desired = new MilimolarConcentration(300);
        Concentration stock = new PercentageConcentration(40);
        Component component = new Component(compound, desired, stock);
        assertEquals(60, component.getQuantity(volume), 1);
    }

    @Test
    public void testPercentageToMgMl() throws Exception {
        Concentration desired = new MgMlConcentration(5);
        Concentration stock = new PercentageConcentration(40);
        Component component = new Component(compound, desired, stock);
        assertEquals(25, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToPercentage() throws Exception {
        Concentration desired = new PercentageConcentration(0.2);
        Concentration stock = new MolarConcentration(5);
        Component component = new Component(compound, desired, stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToMolar() throws Exception {
        Concentration desired = new MolarConcentration(0.1);
        Concentration stock = new MolarConcentration(5);
        Component component = new Component(compound, desired, stock);
        assertEquals(40, component.getQuantity(volume), 1);
    }

    @Test
    public void testMolarToMillimolar() throws Exception {
        Concentration desired = new MilimolarConcentration(20);
        Concentration stock = new MolarConcentration(5);
        Component component = new Component(compound, desired, stock);
        assertEquals(8, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToMgMl() throws Exception {
        Concentration desired = new MgMlConcentration(0.1);
        Concentration stock = new MolarConcentration(5);
        Component component = new Component(compound, desired, stock);
        assertEquals(1, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testMillimolarToPercentage() throws Exception {
        Concentration desired = new PercentageConcentration(0.1);
        Concentration stock = new MilimolarConcentration(200);
        Component component = new Component(compound, desired, stock);
        assertEquals(250, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMolar() throws Exception {
        Concentration desired = new MolarConcentration(0.05);
        Concentration stock = new MilimolarConcentration(200);
        Component component = new Component(compound, desired, stock);
        assertEquals(500, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMillimolar() throws Exception {
        Concentration desired = new MilimolarConcentration(8);
        Concentration stock = new MilimolarConcentration(200);
        Component component = new Component(compound, desired, stock);
        assertEquals(80, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMgMl() throws Exception {
        Concentration desired = new MgMlConcentration(0.1);
        Concentration stock = new MilimolarConcentration(200);
        Component component = new Component(compound, desired, stock);
        assertEquals(25, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMgMlToPercentage() throws Exception {
        Concentration desired = new PercentageConcentration(1);
        Concentration stock = new MgMlConcentration(400);
        Component component = new Component(compound, desired, stock);
        assertEquals(50, component.getQuantity(volume), 1);
    }

    @Test
    public void testMgMlToMolar() throws Exception {
        Concentration desired = new MolarConcentration(2);
        Concentration stock = new MgMlConcentration(400);
        Component component = new Component(compound, desired, stock);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testMgMlToMillimolar() throws Exception {
        Concentration desired = new MilimolarConcentration(5);
        Concentration stock = new MgMlConcentration(400);
        Component component = new Component(compound, desired, stock);
        assertEquals(1, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testMgMlToMgMl() throws Exception {
        Concentration desired = new MgMlConcentration(4);
        Concentration stock = new MgMlConcentration(400);
        Component component = new Component(compound, desired, stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

}