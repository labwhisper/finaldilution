package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import com.labwhisper.biotech.finaldilution.compound.Compound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CompatibleConcentrationsInteractorTest {

    val sut = CompatibleConcentrationsInteractor()

    private val solidNoMass = Compound(
        iupacName = "solid no mass",
        liquid = false
    )

    private val solidWithMass = Compound(
        iupacName = "solid with mass",
        liquid = false,
        molarMass = 1.0
    )

    private val liquidNoMass = Compound(
        iupacName = "liquid no mass",
        liquid = true
    )

    private val liquidWithMassNoDensity = Compound(
        iupacName = "name",
        liquid = true,
        molarMass = 1.0
    )

    private val liquidWithMassAndDensity = Compound(
        iupacName = "name",
        liquid = true,
        molarMass = 1.0,
        density = 0.5
    )

    @Test
    fun `Solid % no mass is compatible with % and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidNoMass,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid % with mass is compatible with %, M, mM and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidWithMass,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER, MOLAR, MILIMOLAR), result
        )
    }

    @Test
    fun `Solid mgMl no mass is compatible with % and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidNoMass,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid mgMl with mass is compatible with %, M, mM and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidWithMass,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER, MOLAR, MILIMOLAR), result
        )
    }

    @Test
    fun `Solid M no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidNoMass,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid M with mass is compatible with %, M, mM and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidWithMass,
            concentrationType = MOLAR
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MOLAR, MILIMOLAR, MILIGRAM_PER_MILLILITER), result
        )
    }

    @Test
    fun `Solid mM no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidNoMass,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid mM with mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidWithMass,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(
            listOf(PERCENTAGE, MOLAR, MILIMOLAR, MILIGRAM_PER_MILLILITER), result
        )
    }

    @Test
    fun `Solid nX no mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidNoMass,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Solid nX with mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            compound = solidWithMass,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Liquid % no mass is compatible with % only`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidNoMass,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE), result)
    }

    @Test
    fun `Liquid % with mass no density is compatible with %`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassNoDensity,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE), result)
    }

    @Test
    fun `Liquid % with mass and density is compatible with %, M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassAndDensity,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE, MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mgMl no mass is compatible with nothing`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidNoMass,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(), result)
    }

    @Test
    fun `Liquid mgMl with mass is compatible with nothing`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassNoDensity,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(), result)
    }

    @Test
    fun `Liquid M no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidNoMass,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid M with mass no density is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassNoDensity,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid M with mass with density is compatible with %, M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassAndDensity,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(PERCENTAGE, MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mM no mass is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidNoMass,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mM with mass no density is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassNoDensity,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mM with mass and density is compatible with %, M and mM`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassAndDensity,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(PERCENTAGE, MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid nX no mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidNoMass,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Liquid nX with mass is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            compound = liquidWithMassNoDensity,
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