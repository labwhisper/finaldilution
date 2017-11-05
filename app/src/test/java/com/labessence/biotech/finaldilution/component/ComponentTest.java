package com.labessence.biotech.finaldilution.component;

import com.labessence.biotech.finaldilution.compound.Compound;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ComponentTest {

    private double volume;
    private Compound compound;

    @Before
    public void setUp() throws Exception {
        volume = 2000; //[ml]
        compound = new Compound("NaOH", 40);
    }

    @Test
    public void testSolidToPercentage() throws Exception {
        Concentration desired = new Concentration(20, ConcentrationType.PERCENTAGE);
        Component component = new Component(compound, desired);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testSolidToMolar() throws Exception {
        Concentration desired = new Concentration(0.5, ConcentrationType.MOLAR);
        Component component = new Component(compound, desired);
        assertEquals(40, component.getQuantity(volume), 1);
    }

    @Test
    public void testSolidToMillimolar() throws Exception {
        Concentration desired = new Concentration(75, ConcentrationType.MILIMOLAR);
        Component component = new Component(compound, desired);
        assertEquals(6, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testSolidToMgMl() throws Exception {
        Concentration desired = new Concentration(200, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component(compound, desired);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testPercentageToPercentage() throws Exception {
        Concentration desired = new Concentration(0.05, ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration(40, ConcentrationType.PERCENTAGE);
        Component component = new Component(compound, desired, stock);
        assertEquals(2.5, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testPercentageToMolar() throws Exception {
        Concentration desired = new Concentration(0.1, ConcentrationType.MOLAR);
        Concentration stock = new Concentration(40, ConcentrationType.PERCENTAGE);
        Component component = new Component(compound, desired, stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testPercentageToMillimolar() throws Exception {
        Concentration desired = new Concentration(300, ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration(40, ConcentrationType.PERCENTAGE);
        Component component = new Component(compound, desired, stock);
        assertEquals(60, component.getQuantity(volume), 1);
    }

    @Test
    public void testPercentageToMgMl() throws Exception {
        Concentration desired = new Concentration(5, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration(40, ConcentrationType.PERCENTAGE);
        Component component = new Component(compound, desired, stock);
        assertEquals(25, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToPercentage() throws Exception {
        Concentration desired = new Concentration(0.2, ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration(5, ConcentrationType.MOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToMolar() throws Exception {
        Concentration desired = new Concentration(0.1, ConcentrationType.MOLAR);
        Concentration stock = new Concentration(5, ConcentrationType.MOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(40, component.getQuantity(volume), 1);
    }

    @Test
    public void testMolarToMillimolar() throws Exception {
        Concentration desired = new Concentration(20, ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration(5, ConcentrationType.MOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(8, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToMgMl() throws Exception {
        Concentration desired = new Concentration(0.1, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration(5, ConcentrationType.MOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(1, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testMillimolarToPercentage() throws Exception {
        Concentration desired = new Concentration(0.1, ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration(200, ConcentrationType.MILIMOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(250, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMolar() throws Exception {
        Concentration desired = new Concentration(0.05, ConcentrationType.MOLAR);
        Concentration stock = new Concentration(200, ConcentrationType.MILIMOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(500, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMillimolar() throws Exception {
        Concentration desired = new Concentration(8, ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration(200, ConcentrationType.MILIMOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(80, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMgMl() throws Exception {
        Concentration desired = new Concentration(0.1, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration(200, ConcentrationType.MILIMOLAR);
        Component component = new Component(compound, desired, stock);
        assertEquals(25, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMgMlToPercentage() throws Exception {
        Concentration desired = new Concentration(1, ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration(400, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component(compound, desired, stock);
        assertEquals(50, component.getQuantity(volume), 1);
    }

    @Test
    public void testMgMlToMolar() throws Exception {
        Concentration desired = new Concentration(2, ConcentrationType.MOLAR);
        Concentration stock = new Concentration(400, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component(compound, desired, stock);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testMgMlToMillimolar() throws Exception {
        Concentration desired = new Concentration(5, ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration(400, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component(compound, desired, stock);
        assertEquals(1, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testMgMlToMgMl() throws Exception {
        Concentration desired = new Concentration(4, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration(400, ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component(compound, desired, stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

}