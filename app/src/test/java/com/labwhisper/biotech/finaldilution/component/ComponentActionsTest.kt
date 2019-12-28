package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.component.view.ComponentsPanel
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ComponentActionsTest {

    private val appContext = mockk<ApplicationContext>(relaxed = true)
    private val activity = mockk<EditActivity>(relaxed = true)
    private val component = Component(Compound(iupacName = "name", liquid = false))
    private val solution = Solution(
        name = "solName", components = mutableListOf(component)
    )

    init {
        every { activity.applicationContext } returns appContext
        every { activity.solution } returns solution
    }

    @Test
    fun `Remove action on component updates solution state`() {
        val sut = ComponentsPanel(activity)
        sut.componentInContextMenu = component
        assertEquals(1, solution.components.size)

        sut.removeComponentSelectedInContextMenu()

        assertEquals(0, solution.components.size)
        verify { appContext.saveCurrentWorkOnSolution(solution) }
    }

}