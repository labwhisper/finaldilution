package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.component.view.ComponentsPanel
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.subjects.BehaviorSubject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ComponentActionsTest {

    private val appContext = mockk<ApplicationContext>(relaxed = true)
    private val activity = mockk<EditActivity>(relaxed = true)
    private val component = Component(Compound(iupacName = "name", liquid = false))
    private val solution = Solution(
        name = "solName", components = mutableListOf(component)
    )

    init {
        every { activity.applicationContext } returns appContext
        every { activity.appModel.solution } returns BehaviorSubject.createDefault(solution)
    }

    @Test
    fun `Remove action on component updates solution state`() {
        val sut = ComponentsPanel(activity)
        sut.componentInContextMenu = component
        assertEquals(1, solution.components.size)
        val solutionWithoutComponent = solution.deepCopy().apply {
            components.clear()
        }

        sut.removeComponentSelectedInContextMenu()

        verify { activity.appModel.updateSolution(solutionWithoutComponent) }
    }

}