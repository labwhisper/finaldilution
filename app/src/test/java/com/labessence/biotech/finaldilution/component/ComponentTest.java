package com.labessence.biotech.finaldilution.component;

import com.labessence.biotech.finaldilution.compound.Compound;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComponentTest {

    private double volume;
    private Compound compound;

    @Before
    public void setUp() throws Exception {
        volume = 2000; //[ml]
        compound = new Compound("NaOH");
        compound.setMolarMass(40.0);
    }

    @Test
    public void testSolidToPercentage() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(20.0);
        desired.setType(ConcentrationType.PERCENTAGE);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testSolidToMolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.5);
        desired.setType(ConcentrationType.MOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        assertEquals(40, component.getQuantity(volume), 1);
    }

    @Test
    public void testSolidToMillimolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(75.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        assertEquals(6, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testSolidToMgMl() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(200.0);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testPercentageToPercentage() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.05);
        desired.setType(ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration();
        desired.setAmount(40.0);
        desired.setType(ConcentrationType.PERCENTAGE);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(2.5, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testPercentageToMolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.1);
        desired.setType(ConcentrationType.MOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(40.0);
        desired.setType(ConcentrationType.PERCENTAGE);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testPercentageToMillimolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(300.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(40.0);
        desired.setType(ConcentrationType.PERCENTAGE);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(60, component.getQuantity(volume), 1);
    }

    @Test
    public void testPercentageToMgMl() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(5.0);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration();
        desired.setAmount(40.0);
        desired.setType(ConcentrationType.PERCENTAGE);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(25, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToPercentage() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.2);
        desired.setType(ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration();
        desired.setAmount(5.0);
        desired.setType(ConcentrationType.MOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToMolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.1);
        desired.setType(ConcentrationType.MOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(5.0);
        desired.setType(ConcentrationType.MOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(40, component.getQuantity(volume), 1);
    }

    @Test
    public void testMolarToMillimolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(20.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(5.0);
        desired.setType(ConcentrationType.MOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(8, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMolarToMgMl() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.1);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration();
        desired.setAmount(5.0);
        desired.setType(ConcentrationType.MOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(1, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testMillimolarToPercentage() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.1);
        desired.setType(ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration();
        desired.setAmount(200.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(250, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.05);
        desired.setType(ConcentrationType.MOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(200.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(500, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMillimolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(8.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(200.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(80, component.getQuantity(volume), 1);
    }

    @Test
    public void testMillimolarToMgMl() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(0.1);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration();
        desired.setAmount(200.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(25, component.getQuantity(volume), 0.1);
    }

    @Test
    public void testMgMlToPercentage() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(1.0);
        desired.setType(ConcentrationType.PERCENTAGE);
        Concentration stock = new Concentration();
        desired.setAmount(400.0);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(50, component.getQuantity(volume), 1);
    }

    @Test
    public void testMgMlToMolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(2.0);
        desired.setType(ConcentrationType.MOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(400.0);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(400, component.getQuantity(volume), 1);
    }

    @Test
    public void testMgMlToMillimolar() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(5.0);
        desired.setType(ConcentrationType.MILIMOLAR);
        Concentration stock = new Concentration();
        desired.setAmount(400.0);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(1, component.getQuantity(volume), 0.01);
    }

    @Test
    public void testMgMlToMgMl() throws Exception {
        Concentration desired = new Concentration();
        desired.setAmount(4.0);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Concentration stock = new Concentration();
        desired.setAmount(400.0);
        desired.setType(ConcentrationType.MILIGRAM_PER_MILLILITER);
        Component component = new Component();
        component.setCompound(compound);
        component.setDesiredConcentration(desired);
        component.setAvailableConcentration(stock);
        assertEquals(20, component.getQuantity(volume), 0.1);
    }

}