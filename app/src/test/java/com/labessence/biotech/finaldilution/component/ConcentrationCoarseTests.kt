package com.labessence.biotech.finaldilution.component

import com.labessence.biotech.finaldilution.component.concentration.MgMlConcentration
import com.labessence.biotech.finaldilution.component.concentration.MilimolarConcentration
import com.labessence.biotech.finaldilution.component.concentration.MolarConcentration
import com.labessence.biotech.finaldilution.component.concentration.PercentageConcentration
import com.labessence.biotech.finaldilution.compound.Compound
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConcentrationCoarseTests {

    private var volume: Double = 0.toDouble()
    private var compound = Compound("NaOH", 40.0)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        volume = 2000.0 //[ml]
    }

    @Test
    @Throws(Exception::class)
    fun testSolidToPercentage() {
        val desired = PercentageConcentration(20.0)
        val component = Component(compound, desired)
        assertEquals(400.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testSolidToMolar() {
        val desired = MolarConcentration(0.5)
        val component = Component(compound, desired)
        assertEquals(40.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testSolidToMillimolar() {
        val desired = MilimolarConcentration(75.0)
        val component = Component(compound, desired)
        assertEquals(6.0, component.getQuantity(volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testSolidToMgMl() {
        val desired = MgMlConcentration(200.0)
        val component = Component(compound, desired)
        assertEquals(400.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testPercentageToPercentage() {
        val desired = PercentageConcentration(0.05)
        val stock = PercentageConcentration(40.0)
        val component = Component(compound, desired, stock)
        assertEquals(2.5, component.getQuantity(volume), 0.01)
    }

    @Test
    @Throws(Exception::class)
    fun testPercentageToMolar() {
        val desired = MolarConcentration(0.1)
        val stock = PercentageConcentration(40.0)
        val component = Component(compound, desired, stock)
        assertEquals(20.0, component.getQuantity(volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testPercentageToMillimolar() {
        val desired = MilimolarConcentration(300.0)
        val stock = PercentageConcentration(40.0)
        val component = Component(compound, desired, stock)
        assertEquals(60.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testPercentageToMgMl() {
        val desired = MgMlConcentration(5.0)
        val stock = PercentageConcentration(40.0)
        val component = Component(compound, desired, stock)
        assertEquals(25.0, component.getQuantity(volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testMolarToPercentage() {
        val desired = PercentageConcentration(0.2)
        val stock = MolarConcentration(5.0)
        val component = Component(compound, desired, stock)
        assertEquals(20.0, component.getQuantity(volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testMolarToMolar() {
        val desired = MolarConcentration(0.1)
        val stock = MolarConcentration(5.0)
        val component = Component(compound, desired, stock)
        assertEquals(40.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testMolarToMillimolar() {
        val desired = MilimolarConcentration(20.0)
        val stock = MolarConcentration(5.0)
        val component = Component(compound, desired, stock)
        assertEquals(8.0, component.getQuantity(volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testMolarToMgMl() {
        val desired = MgMlConcentration(0.1)
        val stock = MolarConcentration(5.0)
        val component = Component(compound, desired, stock)
        assertEquals(1.0, component.getQuantity(volume), 0.01)
    }

    @Test
    @Throws(Exception::class)
    fun testMillimolarToPercentage() {
        val desired = PercentageConcentration(0.1)
        val stock = MilimolarConcentration(200.0)
        val component = Component(compound, desired, stock)
        assertEquals(250.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testMillimolarToMolar() {
        val desired = MolarConcentration(0.05)
        val stock = MilimolarConcentration(200.0)
        val component = Component(compound, desired, stock)
        assertEquals(500.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testMillimolarToMillimolar() {
        val desired = MilimolarConcentration(8.0)
        val stock = MilimolarConcentration(200.0)
        val component = Component(compound, desired, stock)
        assertEquals(80.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testMillimolarToMgMl() {
        val desired = MgMlConcentration(0.1)
        val stock = MilimolarConcentration(200.0)
        val component = Component(compound, desired, stock)
        assertEquals(25.0, component.getQuantity(volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testMgMlToPercentage() {
        val desired = PercentageConcentration(1.0)
        val stock = MgMlConcentration(400.0)
        val component = Component(compound, desired, stock)
        assertEquals(50.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testMgMlToMolar() {
        val desired = MolarConcentration(2.0)
        val stock = MgMlConcentration(400.0)
        val component = Component(compound, desired, stock)
        assertEquals(400.0, component.getQuantity(volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testMgMlToMillimolar() {
        val desired = MilimolarConcentration(5.0)
        val stock = MgMlConcentration(400.0)
        val component = Component(compound, desired, stock)
        assertEquals(1.0, component.getQuantity(volume), 0.01)
    }

    @Test
    @Throws(Exception::class)
    fun testMgMlToMgMl() {
        val desired = MgMlConcentration(4.0)
        val stock = MgMlConcentration(400.0)
        val component = Component(compound, desired, stock)
        assertEquals(20.0, component.getQuantity(volume), 0.1)
    }

}