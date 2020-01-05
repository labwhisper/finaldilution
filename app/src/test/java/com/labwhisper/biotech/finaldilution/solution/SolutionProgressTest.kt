package com.labwhisper.biotech.finaldilution.solution

import com.labwhisper.biotech.finaldilution.InstantTaskExecutorExtension
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labwhisper.biotech.finaldilution.solution.appmodel.StartupAppModel
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class SolutionProgressTest {

    private val solutionGateway = mockk<DataGatewayOperations<Solution>>(relaxed = true)
    private val solution = Solution(
        "S1", 0.08, mutableListOf(
            Component(Compound("C1", true)),
            Component(Compound("C2", false)),
            Component(Compound("C3", true))
        )
    )

    init {
        every { solutionGateway.loadAll() } returns listOf()
    }

    @Test
    fun `Solution checklist is not cleaned on entry, when it's state was not done`() {
        solution.isFilledInWithWater = true
        solution.componentsAdded = solution.components.takeLast(2).toMutableSet()
        val startupAppModel = StartupAppModel(solutionGateway)

        startupAppModel.cleanSolutionProgressIfDone(solution)

        assertEquals(2, solution.componentsAdded.size)
        assertEquals(true, solution.isFilledInWithWater)
    }

    @Test
    fun `Solution checklist is not cleaned on entry, when it is not filled with water`() {
        solution.isFilledInWithWater = false
        solution.componentsAdded = solution.components.toMutableSet()
        val startupAppModel = StartupAppModel(solutionGateway)

        startupAppModel.cleanSolutionProgressIfDone(solution)

        assertEquals(3, solution.componentsAdded.size)
        assertEquals(false, solution.isFilledInWithWater)
    }

    @Test
    fun `Solution checklist is cleaned on entry, when it was entirely done before`() {
        solution.isFilledInWithWater = true
        solution.componentsAdded = solution.components.toMutableSet()
        val startupAppModel = StartupAppModel(solutionGateway)

        startupAppModel.cleanSolutionProgressIfDone(solution)

        assertEquals(0, solution.componentsAdded.size)
        assertEquals(false, solution.isFilledInWithWater)
    }

}