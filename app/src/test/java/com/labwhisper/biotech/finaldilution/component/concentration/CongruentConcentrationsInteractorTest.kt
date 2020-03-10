package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CongruentConcentrationsInteractorTest {

    val sut = CongruentConcentrationsInteractor()

    @Test
    fun `Solid % is congruent with % and mgMl`() {
        val result = sut.getCongruentConcentrations(
            liquid = false,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid mgMl is congruent with % and mgMl`() {
        val result = sut.getCongruentConcentrations(
            liquid = false,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid M is congruent with M and mM`() {
        val result = sut.getCongruentConcentrations(
            liquid = false,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid mM is congruent with M and mM`() {
        val result = sut.getCongruentConcentrations(
            liquid = false,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid nX is congruent with nX`() {
        val result = sut.getCongruentConcentrations(
            liquid = false,
            concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Liquid % is congruent with % only`() {
        val result = sut.getCongruentConcentrations(
            liquid = true,
            concentrationType = PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE), result)
    }

    @Test
    fun `Liquid mgMl is congruent with nothing`() {
        val result = sut.getCongruentConcentrations(
            liquid = true,
            concentrationType = MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(), result)
    }

    @Test
    fun `Liquid M is congruent with M and mM`() {
        val result = sut.getCongruentConcentrations(
            liquid = true,
            concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mM is congruent with M and mM`() {
        val result = sut.getCongruentConcentrations(
            liquid = true,
            concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid nX is congruent with nX`() {
        val result = sut.getCongruentConcentrations(
            liquid = true,
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