package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.concentration.MolarConcentration
import com.labwhisper.biotech.finaldilution.component.concentration.PercentageConcentration
import com.labwhisper.biotech.finaldilution.compound.Compound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ComponentDisplayTest {

    private val solid = Component(Compound("solid1", false, 30.0), MolarConcentration(20.0))
    private val solidStock = Component(
        compound = Compound("solid1", false, 30.0),
        desiredConcentration = MolarConcentration(20.0),
        availableConcentration = MolarConcentration(10.0)
    )
    private val liquid = Component(Compound("liquid1", true), PercentageConcentration(10.0))
    private val liquidWithDensity = Component(
        Compound("liquid1", true, density = 20.0), PercentageConcentration(10.0)
    )
    private val liquidStock = Component(
        compound = Compound("solid1", false, 30.0),
        desiredConcentration = PercentageConcentration(20.0),
        availableConcentration = PercentageConcentration(10.0)
    )


    @Test
    fun `Solid component (no stock) with zero amount should display g`() {
        assertEquals("0 g", solid.getAmountString(0.0))
    }

    @Test
    fun `Solid component (no stock) with amount greater then 1kg should display kg`() {
        assertEquals("1.2 kg", solid.getAmountString(1200.0))
    }

    @Test
    fun `Solid component (no stock) with amount greater then 1g should display g`() {
        assertEquals("1.2 g", solid.getAmountString(1.2))
    }

    @Test
    fun `Solid component (no stock) with amount lesser then 1g should display mg`() {
        assertEquals("800 mg", solid.getAmountString(0.8))
    }

    @Test
    fun `Solid component (no stock) with amount lesser then 1mg should still display mg`() {
        assertEquals("0.08 mg", solid.getAmountString(0.00008))
    }

    @Test
    fun `Solid component (no stock) with amount lesser then 10ug should display ug`() {
        assertEquals("8 \u03bcg", solid.getAmountString(0.000008))
    }


    @Test
    fun `Liquid component (no stock) no density with zero amount should display g`() {
        assertEquals("0 g", liquid.getAmountString(0.0))
    }

    @Test
    fun `Liquid component (no stock) no density with amount greater then 1l should display kg`() {
        assertEquals("1.2 kg", liquid.getAmountString(1200.0))
    }

    @Test
    fun `Liquid component (no stock) no density with amount greater then 1ml should display g`() {
        assertEquals("1.2 g", liquid.getAmountString(1.2))
    }

    @Test
    fun `Liquid component (no stock) no density with amount lesser then 1ml should display mg`() {
        assertEquals("800 mg", liquid.getAmountString(0.8))
    }

    @Test
    fun `Liquid component (no stock) no density with amount lesser then 1ul should still display ug`() {
        assertEquals("0.8 mg", liquid.getAmountString(0.0008))
    }

    @Test
    fun `Liquid component (no stock) no density with amount lesser then 1ul should display ug`() {
        assertEquals("8 \u03bcg", liquid.getAmountString(0.000008))
    }

    @Test
    fun `Liquid component (no stock) with density with zero amount should display ml`() {
        assertEquals("0 ml", liquidWithDensity.getAmountString(0.0))
    }

    @Test
    fun `Liquid component (no stock) with density with amount greater then 1l should display l`() {
        assertEquals("1.2 l", liquidWithDensity.getAmountString(1200.0))
    }

    @Test
    fun `Liquid component (no stock) with density with amount greater then 1ml should display ml`() {
        assertEquals("1.2 ml", liquidWithDensity.getAmountString(1.2))
    }

    @Test
    fun `Liquid component (no stock) with density with amount lesser then 1ml should display ul`() {
        assertEquals("800 \u03bcl", liquidWithDensity.getAmountString(0.8))
    }

    @Test
    fun `Liquid component (no stock) with density with amount lesser then 1ul should still display ul`() {
        assertEquals("0.8 \u03bcl", liquidWithDensity.getAmountString(0.0008))
    }

    @Test
    fun `Liquid component (no stock) with density with amount lesser then 1ul should display nl`() {
        assertEquals("8 nl", liquidWithDensity.getAmountString(0.000008))
    }


    @Test
    fun `Solid component from stock with zero amount should display ml`() {
        assertEquals("0 ml", solidStock.getAmountString(0.0))
    }

    @Test
    fun `Solid component from stock with amount greater then 1kg should display l`() {
        assertEquals("1.2 l", solidStock.getAmountString(1200.0))
    }

    @Test
    fun `Solid component from stock with amount greater then 1g should display ml`() {
        assertEquals("1.2 ml", solidStock.getAmountString(1.2))
    }

    @Test
    fun `Solid component from stock with amount lesser then 1ml should display ul`() {
        assertEquals("800 \u03bcl", solidStock.getAmountString(0.8))
    }

    @Test
    fun `Solid component from stock with amount lesser then 1ul should still display ul`() {
        assertEquals("0.8 \u03bcl", solidStock.getAmountString(0.0008))
    }

    @Test
    fun `Solid component from stock with amount lesser then 1ul should display nl`() {
        assertEquals("8 nl", solidStock.getAmountString(0.000008))
    }


    @Test
    fun `Liquid component from stock with zero amount should display ml`() {
        assertEquals("0 ml", liquidStock.getAmountString(0.0))
    }

    @Test
    fun `Liquid component from stock with amount greater then 1l should display l`() {
        assertEquals("1.2 l", liquidStock.getAmountString(1200.0))
    }

    @Test
    fun `Liquid component from stock with amount greater then 1ml should display ml`() {
        assertEquals("1.2 ml", liquidStock.getAmountString(1.2))
    }

    @Test
    fun `Liquid component from stock with amount lesser then 1ml should display ul`() {
        assertEquals("800 \u03bcl", liquidStock.getAmountString(0.8))
    }

    @Test
    fun `Liquid component from stock with amount lesser then 1ul should still display ul`() {
        assertEquals("0.8 \u03bcl", liquidStock.getAmountString(0.0008))
    }

    @Test
    fun `Liquid component from stock with amount lesser then 1ul should display nl`() {
        assertEquals("8 nl", liquidStock.getAmountString(0.000008))
    }

}