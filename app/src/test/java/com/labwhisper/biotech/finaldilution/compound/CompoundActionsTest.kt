package com.labwhisper.biotech.finaldilution.compound

import com.labwhisper.biotech.finaldilution.component.view.EditCompoundFragmentCreator
import com.labwhisper.biotech.finaldilution.compound.appmodel.CompoundsPanelAppModel
import com.labwhisper.biotech.finaldilution.compound.view.CompoundsPanel
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.appmodel.EditSolutionAppModel
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.jupiter.api.Test

class CompoundActionsTest {

    private val editSolutionAppModel =
        EditSolutionAppModel(mockk(relaxed = true), mockk(relaxed = true))
    private val activity = mockk<EditActivity>(relaxed = true)
    private val compound = Compound("Compound1", false)
    private val solution = Solution("S", 1.2)
    private val solutionSubject = BehaviorSubject.createDefault(solution)
    private val appModel =
        CompoundsPanelAppModel(
            editSolutionAppModel = editSolutionAppModel,
            compoundList = mockk(relaxed = true),
            solution = solutionSubject,
            careTaker = mockk()
        )
    private val editCompoundFragmentCreator = mockk<EditCompoundFragmentCreator>(relaxed = true)
    private val sut = CompoundsPanel(activity, appModel, editCompoundFragmentCreator)

    init {
        every { activity.appModel } returns editSolutionAppModel
    }

    @Test
    fun `Delete action on compound removes it from every solution`() {
        sut.compoundInContextMenu = compound

        sut.deleteCompoundSelectedInContextMenu()

        verify { activity.showDeleteCompoundConfirmationDialog(compound) }
    }

    @Test
    fun `Edit action on compound enters compound edition`() {
        sut.compoundInContextMenu = compound

        sut.editCompoundSelectedInContextMenu()

        verify { editCompoundFragmentCreator.startCompoundEdition(activity, compound, any()) }
    }

}