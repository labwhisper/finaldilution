package com.labwhisper.biotech.finaldilution.compound

import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.component.concentration.MolarConcentration
import com.labwhisper.biotech.finaldilution.component.concentration.PercentageConcentration
import com.labwhisper.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labwhisper.biotech.finaldilution.solution.Solution
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class CompoundGlobalFunctionsTest {

    private val solutionGateway = TestSolutionGateway()
    private val compoundGateway = mockk<DataGatewayOperations<Compound>>(relaxed = true)
    private val sut = CompoundChangePropagator(solutionGateway, compoundGateway)
    private val c1 = Compound("C1", true)
    private val c2 = Compound("C2", false, 3.0)
    private val c1Changed = Compound("C1Changed", true)
    val component1 = Component(c1, PercentageConcentration(2.0))
    val component2 = Component(c2, MolarConcentration(3.0))
    val component1Changed = Component(c1Changed, PercentageConcentration(2.0))

    @Test
    fun `When renaming compound - rename it in gateway`() {
        sut.propagateCompoundRename(c1Changed, c1)
        verify { compoundGateway.rename(c1Changed, c1.name) }
    }

    @Test
    fun `When renaming compound - rename it in all solutions`() {
        solutionGateway.updated = mutableListOf()
        sut.propagateCompoundRename(c1Changed, c1)
        assert(solutionGateway.updated.flatMap { it.components }.contains(component1Changed))
        assert(!solutionGateway.updated.flatMap { it.components }.contains(component1))
        assert(solutionGateway.updated.map { it.name }.contains("Sol1"))
        assert(solutionGateway.updated.map { it.name }.contains("Sol2"))
        assert(!solutionGateway.updated.map { it.name }.contains("Sol3"))
    }

    inner class TestSolutionGateway : DataGatewayOperations<Solution> {

        var updated = mutableListOf<Solution>()

        override fun save(item: Solution) = Unit

        override fun rename(item: Solution, oldName: String) = Unit

        override fun update(item: Solution) {
            updated.add(item)
        }

        override fun remove(item: Solution) = Unit

        override fun load(name: String): Solution? = null

        override fun loadAll(): List<Solution> {
            return listOf(
                Solution("Sol1", components = mutableListOf(component1, component2)),
                Solution("Sol2", components = mutableListOf(component1)),
                Solution("Sol3", components = mutableListOf(component2))
            )
        }

        override fun size() = 0

    }

}