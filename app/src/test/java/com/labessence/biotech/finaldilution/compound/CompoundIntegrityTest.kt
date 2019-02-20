package com.labessence.biotech.finaldilution.compound

import org.junit.Assert.assertEquals
import org.junit.Test

class CompoundIntegrityTest {

    private val trivialName1 = "trivialName1"
    private val iupacName1 = "iupacName1"
    private val chemFormula1 = "FORMULA1"

    @Test
    fun `Trivial name is chosen to display`() {
        val compound = Compound(trivialName1, 30.0, iupacName1, chemFormula1)
        val displayedName = compound.displayName
        assertEquals(trivialName1, displayedName)
    }

    @Test
    fun `Iupac name is displayed only on empty trivial name`() {
        val compound = Compound("", 30.0, iupacName1, chemFormula1)
        val displayedName = compound.displayName
        assertEquals(iupacName1, displayedName)
    }

    @Test
    fun `Chemical formula is displayed only on empty names`() {
        val compound = Compound("", 30.0, "", chemFormula1)
        val displayedName = compound.displayName
        assertEquals(chemFormula1, displayedName)
    }
}