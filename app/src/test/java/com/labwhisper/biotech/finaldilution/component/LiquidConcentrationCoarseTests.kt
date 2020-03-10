package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.concentration.*
import com.labwhisper.biotech.finaldilution.compound.Compound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LiquidConcentrationCoarseTests {

    val congruentConcentrationsInteractor = CongruentConcentrationsInteractor()
    private val sut = ComponentQuantityCalculator(congruentConcentrationsInteractor)

    private var volume = 2000.0

    private var liquid = Compound(
        iupacName = "methanol",
        liquid = true,
        molarMass = 32.0,
        density = 0.8
    )

    private var liquidNoDensity = Compound(
        iupacName = "methanol no density",
        liquid = true,
        molarMass = 32.0
    )

    @Test
    @Throws(Exception::class)
    fun testLiquidNoDensityToPercentage() {
        val desired = PercentageConcentration(20.0)
        val component = Component(liquidNoDensity, desired)
        assertEquals(400.0, sut.getQuantity(component, volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testLiquidNoDensityToMolar() {
        val desired = MolarConcentration(0.5)
        val component = Component(liquidNoDensity, desired)
        assertEquals(32.0, sut.getQuantity(component, volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testLiquidNoDensityToMillimolar() {
        val desired = MilimolarConcentration(75.0)
        val component = Component(liquidNoDensity, desired)
        assertEquals(4.8, sut.getQuantity(component, volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testLiquidNoDensityToMgMl() {
        val desired = MgMlConcentration(200.0)
        val component = Component(liquidNoDensity, desired)
        assertEquals(400.0, sut.getQuantity(component, volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testLiquidToPercentage() {
        val desired = PercentageConcentration(20.0)
        val component = Component(liquid, desired)
        assertEquals(400.0, sut.getQuantity(component, volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testLiquidToMolar() {
        val desired = MolarConcentration(0.5)
        val component = Component(liquid, desired)
        assertEquals(40.0, sut.getQuantity(component, volume), 1.0)
    }

    @Test
    @Throws(Exception::class)
    fun testLiquidToMillimolar() {
        val desired = MilimolarConcentration(75.0)
        val component = Component(liquid, desired)
        assertEquals(6.0, sut.getQuantity(component, volume), 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun testLiquidToMgMl() {
        val desired = MgMlConcentration(200.0)
        val component = Component(liquid, desired)
        assertEquals(400.0, sut.getQuantity(component, volume), 1.0)
    }

}