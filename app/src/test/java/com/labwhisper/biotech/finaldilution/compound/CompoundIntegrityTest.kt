package com.labwhisper.biotech.finaldilution.compound

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CompoundIntegrityTest {

    private val trivialName1 = "trivialName1"
    private val iupacName1 = "iupacName1"
    private val chemFormula1 = "FORMULA1"

    @Test
    fun `Trivial name is chosen to display`() {
        val compound = Compound(iupacName1, false, 30.0, trivialName1, chemFormula1)
        val displayedName = compound.displayName
        assertEquals(trivialName1, displayedName)
    }

    @Test
    fun `Iupac name is displayed only on empty trivial name`() {
        val compound = Compound(iupacName1, false, 30.0, "", chemFormula1)
        val displayedName = compound.displayName
        assertEquals(iupacName1, displayedName)
    }

    @Test
    fun `Chemical formula is displayed only on empty names`() {
        val compound = Compound("", false, 30.0, "", chemFormula1)
        val displayedName = compound.displayName
        assertEquals(chemFormula1, displayedName)
    }

    @Test
    fun `Molar mass is displayed for not null solid`() {
        val compound = Compound(iupacName1, false, 30.0, "", chemFormula1)
        val displayedMass = compound.displayMass
        assertEquals("[30]", displayedMass)
    }

    @Test
    fun `"undefined" mass is displayed for null solid`() {
        val compound = Compound(iupacName1, false, null, "", chemFormula1)
        val displayedMass = compound.displayMass
        assertEquals("[undefined]", displayedMass)
    }

    @Test
    fun `"liquid" mass is displayed for null liquid`() {
        val compound = Compound(iupacName1, true, null, "", chemFormula1)
        val displayedMass = compound.displayMass
        assertEquals("[liquid]", displayedMass)
    }

}