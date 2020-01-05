package com.labwhisper.biotech.finaldilution.compound

import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.component.view.EditCompoundFragmentCreator
import com.labwhisper.biotech.finaldilution.compound.appmodel.CompoundsPanelAppModel
import com.labwhisper.biotech.finaldilution.compound.view.CompoundsPanel
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class CompoundActionsTest {

    private val appContext = mockk<ApplicationContext>(relaxed = true)
    private val activity = mockk<EditActivity>(relaxed = true)
    private val compound = Compound("Compound1", false)
    private val solution = Solution("S", 1.2)
    private val appModel =
        CompoundsPanelAppModel(appContext, mockk(relaxed = true), solution, mockk())
    private val editCompoundFragmentCreator = mockk<EditCompoundFragmentCreator>(relaxed = true)
    private val sut = CompoundsPanel(activity, appModel, editCompoundFragmentCreator)

    init {
        every { activity.applicationContext } returns appContext
    }

    @Test
    fun `Delete action on compound removes it from every solution`() {
        sut.compoundInContextMenu = compound

        sut.deleteCompoundSelectedInContextMenu()

        verify { appContext.removeCompoundFromEverywhere(compound) }
    }

    @Test
    fun `Edit action on compound enters compound edition`() {
        sut.compoundInContextMenu = compound

        sut.editCompoundSelectedInContextMenu()

        verify { editCompoundFragmentCreator.startCompoundEdition(activity, compound, any()) }
    }

    @Test
    fun `When there was no change, do not reload solution`() {
        sut.handleCompoundNameChange(Compound("OldName", false), null)
        verify(inverse = true) { activity.propagateAllChanges() }
    }

    @Test
    fun `If new compound was added, reload solution`() {
        sut.handleCompoundNameChange(null, Compound("NewName", true))
        verify { activity.propagateAllChanges() }
    }

    @Test
    fun `When compound name wasn't changed do not reload solution`() {
        sut.handleCompoundNameChange(Compound("OldName", false), Compound("OldName", true))
        verify(inverse = true) { activity.propagateAllChanges() }
    }

    @Test
    fun `When compound name was changed reload solution`() {
        sut.handleCompoundNameChange(Compound("OldName", false), Compound("NewName", false))
        verify { activity.propagateAllChanges() }
    }

}