package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CompatibleConcentrationsInteractorTest {

    val sut = CompatibleConcentrationsInteractor()

    @Test
    fun `Solid % no mass is compatible with % and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = false,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid % with mass is compatible with %, M, mM and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = true,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER, MOLAR, MILIMOLAR), result
        )
    }

    @Test
    fun `Solid mgMl no mass is compatible with % and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = false,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid mgMl with mass is compatible with %, M, mM and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = true,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER, MOLAR, MILIMOLAR), result
        )
    }

    @Test
    fun `Solid M no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = false,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid M with mass is compatible with %, M, mM and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = true,
            concentrationType = MOLAR
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MOLAR, MILIMOLAR, MILIGRAM_PER_MILLILITER), result
        )
    }

    @Test
    fun `Solid mM no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = false,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid mM with mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = true,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MOLAR, MILIMOLAR, MILIGRAM_PER_MILLILITER), result
        )
    }

    @Test
    fun `Solid nX no mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = false,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Solid nX with mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false,
            molarMassGiven = true,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Liquid % no mass is compatible with % only`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = false,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE), result)
    }

    @Test
    fun `Liquid % with mass is compatible with %, M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = true,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE, MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mgMl no mass is compatible with nothing`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = false,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(), result)
    }

    @Test
    fun `Liquid mgMl with mass is compatible with nothing`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = true,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(), result)
    }

    @Test
    fun `Liquid M no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = false,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid M with mass is compatible with %, M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = true,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(PERCENTAGE, MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mM no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = false,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mM with mass is compatible with %, M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = true,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(PERCENTAGE, MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid nX no mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = false,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Liquid nX with mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true,
            molarMassGiven = true,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    private fun assertListsEquivalent(
        expectedList: List<ConcentrationType>,
        result: List<ConcentrationType>
    ) {
        assertEquals(expectedList.size, result.size)
        assertEquals(result.containsAll(expectedList), true)
    }

}