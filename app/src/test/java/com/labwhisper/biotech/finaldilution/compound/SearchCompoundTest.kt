package com.labwhisper.biotech.finaldilution.compound

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class SearchCompoundTest {

    val compound1 = Compound("Sodium sulfate", false, 30.0, "", "Na2SO4")
    val compound2 = Compound("Sodium azide", false, 30.0, "", "NaN3")
    val compound3 = Compound("Sulfuric acid", false, 30.0, "", "H2SO4")
    val compound4 = Compound("Ethylenediaminetetraacetic acid", false, 30.0, "EDTA", "C10H16N2O8")
    val compound5 = Compound("EDTIUM", false, 30.0, "", "")
    val compound6 = Compound("Glycerin", true, 92.0938, null, "HOCH2CH(OH)CH2OH")
    val compounds = listOf(compound1, compound2, compound3, compound4, compound5, compound6)

    @Test
    fun `Compounds which iupac name contains a search string are found`() {
        val result = CompoundSearch.searchForCompound(compounds, "sulf")
        assertEquals(listOf(compound1, compound3), result)
    }

    @Test
    fun `Compounds which iupac name contains an upper case search string are found`() {
        val result = CompoundSearch.searchForCompound(compounds, "SULF")
        assertEquals(listOf(compound1, compound3), result)
    }

    @Test
    fun `Compounds which trivial name contains a search string are found`() {
        val result = CompoundSearch.searchForCompound(compounds, "edt")
        assertEquals(listOf(compound4, compound5), result)
    }

    @Test
    fun `Compounds which chemical formula name contains a search string are found`() {
        val result = CompoundSearch.searchForCompound(compounds, "Na")
        assertEquals(listOf(compound1, compound2), result)
    }

    @Test
    fun `Compounds that contains string in every fiels are found`() {
        val result = CompoundSearch.searchForCompound(compounds, "so")
        assertEquals(listOf(compound1, compound2, compound3), result)
    }

    @Test
    fun `Search containing gly finds Glycerine`() {
        val result = CompoundSearch.searchForCompound(compounds, "gly")
        assertEquals(listOf(compound6), result)
    }

}