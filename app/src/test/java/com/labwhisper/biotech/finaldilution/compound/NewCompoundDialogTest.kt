package com.labwhisper.biotech.finaldilution.compound

import com.labwhisper.biotech.finaldilution.compound.appmodel.NewCompoundAppModel
import junit.framework.Assert.assertEquals
import org.junit.Test


class NewCompoundDialogTest {

    @Test
    fun `Advanced options should be closed at dialog load when advanced fields are empty`() {
        val compound = Compound("iupac_name", true)
        val appModel = NewCompoundAppModel()
        appModel.initialCompound = compound
        assertEquals(false, appModel.advancedOptions)
    }

    @Test
    fun `Advanced options should be open at dialog load when formula is filled`() {
        val compound = Compound("iupac_name", true, chemicalFormula = "CHF")
        val appModel = NewCompoundAppModel()
        appModel.initialCompound = compound
        assertEquals(true, appModel.advancedOptions)
    }

    @Test
    fun `Advanced options should be open at dialog load when alternative name is filled`() {
        val compound = Compound("iupac_name", true, trivialName = "alterName")
        val appModel = NewCompoundAppModel()
        appModel.initialCompound = compound
        assertEquals(true, appModel.advancedOptions)
    }

}
