package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import com.labwhisper.biotech.finaldilution.compound.Compound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PossibleConcentrationsInteractorTest {

    val sut = PossibleConcentrationsInteractor()

    @Test
    fun `Solid with no molar mass can have only % and MgMl concentrations`() {
        val compound = Compound(
            iupacName = "Name1",
            liquid = false,
            molarMass = null
        )
        val result = sut.getPossibleConcentrations(compound)
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid with molar mass can have M, mM, MgMl and % concentrations`() {
        val compound = Compound(
            iupacName = "Name1",
            liquid = false,
            molarMass = 23.0
        )
        val result = sut.getPossibleConcentrations(compound)
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR, PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Liquid with no molar mass can have only % concentrations`() {
        val compound = Compound(
            iupacName = "Name1",
            liquid = true,
            molarMass = null
        )
        val result = sut.getPossibleConcentrations(compound)
        assertListsEquivalent(listOf(PERCENTAGE), result)
    }

    @Test
    fun `Liquid with molar mass can have M, mM and % concentrations`() {
        val compound = Compound(
            iupacName = "Name1",
            liquid = true,
            molarMass = 23.0
        )
        val result = sut.getPossibleConcentrations(compound)
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR, PERCENTAGE), result)
    }

    private fun assertListsEquivalent(
        expectedList: List<ConcentrationType>,
        result: List<ConcentrationType>
    ) {
        assertEquals(expectedList.size, result.size)
        assertEquals(result.containsAll(expectedList), true)
    }

}