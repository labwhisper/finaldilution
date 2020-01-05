package com.labwhisper.biotech.finaldilution.compound

import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.compound.appmodel.NewCompoundAppModel
import io.mockk.Ordering
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class NewCompoundDialogTest {
    val appContext = mockk<ApplicationContext>(relaxed = true)

    @Test
    fun `Advanced options should be closed at dialog load when advanced fields are empty`() {
        val compound = Compound("iupac_name", true)
        val appModel = NewCompoundAppModel(appContext)
        appModel.initialCompound = compound
        assertEquals(false, appModel.advancedOptions)
    }

    @Test
    fun `Advanced options should be open at dialog load when formula is filled`() {
        val compound = Compound("iupac_name", true, chemicalFormula = "CHF")
        val appModel = NewCompoundAppModel(appContext)
        appModel.initialCompound = compound
        assertEquals(true, appModel.advancedOptions)
    }

    @Test
    fun `Advanced options should be open at dialog load when alternative name is filled`() {
        val compound = Compound("iupac_name", true, trivialName = "alterName")
        val appModel = NewCompoundAppModel(appContext)
        appModel.initialCompound = compound
        assertEquals(true, appModel.advancedOptions)
    }

    @Test
    fun `When proceeding without initial compound - save new compound`() {
        val compound = Compound("iupac_name", true)
        val appModel = NewCompoundAppModel(appContext)
        appModel.initialCompound = null
        appModel.proceedWithCompound(compound)

        verify { appContext.safeSaveCompound(compound) }
        verify(inverse = true) {
            appContext.renameCompound(any(), compound)
            appContext.updateCompound(any())
        }
    }

    @Test
    fun `When proceeding without renaming - compound should just be updated`() {
        val compound = Compound("iupac_name", true)
        val updated_compound = Compound("iupac_name", false)
        val appModel = NewCompoundAppModel(appContext)
        appModel.initialCompound = compound
        appModel.proceedWithCompound(updated_compound)

        verify(inverse = true) {
            appContext.safeSaveCompound(any())
            appContext.renameCompound(any(), any())
        }
        verify { appContext.updateCompound(updated_compound) }
    }

    @Test
    fun `When proceeding, compound should be renamed and then update when name has changed`() {
        val compound = Compound("iupac_name", true)
        val renamed_compound = Compound("iupac_name_renamed", true)
        val appModel = NewCompoundAppModel(appContext)
        appModel.initialCompound = compound
        appModel.proceedWithCompound(renamed_compound)

        verify(inverse = true) { appContext.safeSaveCompound(compound) }
        verify(ordering = Ordering.SEQUENCE) {
            appContext.renameCompound(renamed_compound, compound)
            appContext.updateCompound(renamed_compound)
        }
    }

}
