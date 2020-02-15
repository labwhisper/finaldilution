package com.labwhisper.biotech.finaldilution.compound

import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labwhisper.biotech.finaldilution.solution.Solution

class CompoundChangePropagator(
    private val solutionGateway: DataGatewayOperations<Solution>,
    private val compoundGateway: DataGatewayOperations<Compound>
) {


    fun propagateCompoundRename(compound: Compound, oldCompound: Compound) {
        compoundGateway.rename(compound, oldCompound.name)
        updateCompoundInAllSolutions(oldCompound, compound)
    }

    fun propagateCompoundRemoval(compound: Compound) {
        for (solution in solutionGateway.loadAll()) {
            val component = solution.getComponentWithCompound(compound)
            if (component != null) {
                solution.removeComponent(component)
                solutionGateway.update(solution)
            }
        }
        compoundGateway.remove(compound)
    }

    fun propagateCompoundUpdate(
        compound: Compound,
        oldCompound: Compound
    ) {
        compoundGateway.update(compound)
        updateCompoundInAllSolutions(oldCompound, compound)
    }

    private fun updateCompoundInAllSolutions(
        oldCompound: Compound,
        compound: Compound
    ) {
        for (solution in solutionGateway.loadAll()) {
            val oldComponent = solution.getComponentWithCompound(oldCompound)
            oldComponent?.let {
                val newComponent = Component(
                    compound,
                    oldComponent.desiredConcentration,
                    oldComponent.availableConcentration
                ).apply { setSolutionVolume(solution.volume) }
                solution.removeComponent(oldComponent)
                solution.addComponent(newComponent)
                solutionGateway.update(solution)
            }
        }
    }

}