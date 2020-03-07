package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.concentration.MolarConcentration
import com.labwhisper.biotech.finaldilution.component.concentration.PercentageConcentration
import com.labwhisper.biotech.finaldilution.compound.Compound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ComponentDisplayTest {

    private val sut = ComponentQuantityCalculator()
    private val solid = Component(Compound("solid1", false, 30.0), MolarConcentration(20.0))
    private val solidStock = Component(
        compound = Compound("solid1", false, 30.0),
        desiredConcentration = MolarConcentration(20.0),
        availableConcentration = MolarConcentration(10.0)
    )
    private val liquid = Component(Compound("liquid1", true), MolarConcentration(10.0))
    private val liquidPercentage =
        Component(Compound("liquid1", true), PercentageConcentration(10.0))
    private val liquidWithDensity = Component(
        Compound("liquid1", true, density = 20.0), MolarConcentration(10.0)
    )
    private val liquidStock = Component(
        compound = Compound("solid1", false, 30.0),
        desiredConcentration = PercentageConcentration(20.0),
        availableConcentration = PercentageConcentration(10.0)
    )


    @Test
    fun `Solid component (no stock) with zero amount should display g`() {
        assertEquals("0 g", sut.getAmountString(solid, 0.0))
    }

    @Test
    fun `Solid component (no stock) with amount greater then 1kg should display kg`() {
        assertEquals("1.2 kg", sut.getAmountString(solid, 1200.0))
    }

    @Test
    fun `Solid component (no stock) with amount greater then 1g should display g`() {
        assertEquals("1.2 g", sut.getAmountString(solid, 1.2))
    }

    @Test
    fun `Solid component (no stock) with amount lesser then 1g should display mg`() {
        assertEquals("800 mg", sut.getAmountString(solid, 0.8))
    }

    @Test
    fun `Solid component (no stock) with amount lesser then 1mg should still display mg`() {
        assertEquals("0.08 mg", sut.getAmountString(solid, 0.00008))
    }

    @Test
    fun `Solid component (no stock) with amount lesser then 10ug should display ug`() {
        assertEquals("8 \u03bcg", sut.getAmountString(solid, 0.000008))
    }


    @Test
    fun `Liquid % component (no stock) no density with zero amount should display ml`() {
        assertEquals("0 ml", sut.getAmountString(liquidPercentage, 0.0))
    }

    @Test
    fun `Liquid % component (no stock) no density with amount greater then 1l should display l`() {
        assertEquals("1.2 l", sut.getAmountString(liquidPercentage, 1200.0))
    }

    @Test
    fun `Liquid % component (no stock) no density with amount greater then 1ml should display ml`() {
        assertEquals("1.2 ml", sut.getAmountString(liquidPercentage, 1.2))
    }

    @Test
    fun `Liquid % component (no stock) no density with amount lesser then 1ml should display ul`() {
        assertEquals("800 \u03bcl", sut.getAmountString(liquidPercentage, 0.8))
    }

    @Test
    fun `Liquid % component (no stock) no density with amount lesser then 1ul should still display ul`() {
        assertEquals("0.8 \u03bcl", sut.getAmountString(liquidPercentage, 0.0008))
    }

    @Test
    fun `Liquid % component (no stock) no density with amount lesser then 1ul should display nl`() {
        assertEquals("8 nl", sut.getAmountString(liquidPercentage, 0.000008))
    }


    @Test
    fun `Liquid component (no stock) no density with zero amount should display g`() {
        assertEquals("0 g", sut.getAmountString(liquid, 0.0))
    }

    @Test
    fun `Liquid component (no stock) no density with amount greater then 1l should display kg`() {
        assertEquals("1.2 kg", sut.getAmountString(liquid, 1200.0))
    }

    @Test
    fun `Liquid component (no stock) no density with amount greater then 1ml should display g`() {
        assertEquals("1.2 g", sut.getAmountString(liquid, 1.2))
    }

    @Test
    fun `Liquid component (no stock) no density with amount lesser then 1ml should display mg`() {
        assertEquals("800 mg", sut.getAmountString(liquid, 0.8))
    }

    @Test
    fun `Liquid component (no stock) no density with amount lesser then 1ul should still display ug`() {
        assertEquals("0.8 mg", sut.getAmountString(liquid, 0.0008))
    }

    @Test
    fun `Liquid component (no stock) no density with amount lesser then 1ul should display ug`() {
        assertEquals("8 \u03bcg", sut.getAmountString(liquid, 0.000008))
    }


    @Test
    fun `Liquid component (no stock) with density with zero amount should display ml`() {
        assertEquals("0 ml", sut.getAmountString(liquidWithDensity, 0.0))
    }

    @Test
    fun `Liquid component (no stock) with density with amount greater then 1l should display l`() {
        assertEquals("1.2 l", sut.getAmountString(liquidWithDensity, 1200.0))
    }

    @Test
    fun `Liquid component (no stock) with density with amount greater then 1ml should display ml`() {
        assertEquals("1.2 ml", sut.getAmountString(liquidWithDensity, 1.2))
    }

    @Test
    fun `Liquid component (no stock) with density with amount lesser then 1ml should display ul`() {
        assertEquals("800 \u03bcl", sut.getAmountString(liquidWithDensity, 0.8))
    }

    @Test
    fun `Liquid component (no stock) with density with amount lesser then 1ul should still display ul`() {
        assertEquals("0.8 \u03bcl", sut.getAmountString(liquidWithDensity, 0.0008))
    }

    @Test
    fun `Liquid component (no stock) with density with amount lesser then 1ul should display nl`() {
        assertEquals("8 nl", sut.getAmountString(liquidWithDensity, 0.000008))
    }


    @Test
    fun `Solid component from stock with zero amount should display ml`() {
        assertEquals("0 ml", sut.getAmountString(solidStock, 0.0))
    }

    @Test
    fun `Solid component from stock with amount greater then 1kg should display l`() {
        assertEquals("1.2 l", sut.getAmountString(solidStock, 1200.0))
    }

    @Test
    fun `Solid component from stock with amount greater then 1g should display ml`() {
        assertEquals("1.2 ml", sut.getAmountString(solidStock, 1.2))
    }

    @Test
    fun `Solid component from stock with amount lesser then 1ml should display ul`() {
        assertEquals("800 \u03bcl", sut.getAmountString(solidStock, 0.8))
    }

    @Test
    fun `Solid component from stock with amount lesser then 1ul should still display ul`() {
        assertEquals("0.8 \u03bcl", sut.getAmountString(solidStock, 0.0008))
    }

    @Test
    fun `Solid component from stock with amount lesser then 1ul should display nl`() {
        assertEquals("8 nl", sut.getAmountString(solidStock, 0.000008))
    }


    @Test
    fun `Liquid component from stock with zero amount should display ml`() {
        assertEquals("0 ml", sut.getAmountString(liquidStock, 0.0))
    }

    @Test
    fun `Liquid component from stock with amount greater then 1l should display l`() {
        assertEquals("1.2 l", sut.getAmountString(liquidStock, 1200.0))
    }

    @Test
    fun `Liquid component from stock with amount greater then 1ml should display ml`() {
        assertEquals("1.2 ml", sut.getAmountString(liquidStock, 1.2))
    }

    @Test
    fun `Liquid component from stock with amount lesser then 1ml should display ul`() {
        assertEquals("800 \u03bcl", sut.getAmountString(liquidStock, 0.8))
    }

    @Test
    fun `Liquid component from stock with amount lesser then 1ul should still display ul`() {
        assertEquals("0.8 \u03bcl", sut.getAmountString(liquidStock, 0.0008))
    }

    @Test
    fun `Liquid component from stock with amount lesser then 1ul should display nl`() {
        assertEquals("8 nl", sut.getAmountString(liquidStock, 0.000008))
    }

}