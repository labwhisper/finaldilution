package com.labwhisper.biotech.finaldilution.solution.appmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.labwhisper.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labwhisper.biotech.finaldilution.solution.Solution

class StartupAppModel(val solutionGateway: DataGatewayOperations<Solution>) {

    val solutionList: LiveData<List<Solution>>

    init {
        solutionList = MutableLiveData()
        refresh()
    }

    fun refresh() {
        (solutionList as MutableLiveData<List<Solution>>).value =
            solutionGateway.loadAll().sorted()
    }

    //TODO Create interactors for those methods, AppModel/ViewModel should contain state only
    fun cleanSolutionProgressIfDone(solution: Solution) {
        if (solution.done) {
            solution.componentsAdded.clear()
            solution.isFilledInWithWater = false
            updateSolution(solution)
        }
    }

    fun addNewSolution(newName: String) {
        if (newName.isBlank()) {
            return
        }
        if (solutionGateway.load(newName) != null) {
            return
        }
        solutionGateway.save(Solution(newName))
        refresh()
    }

    fun addNewSolutionFilled(newSolution: Solution) {
        if (solutionGateway.load(newSolution.name) != null) {
            return
        }
        solutionGateway.save(newSolution)
        refresh()
    }

    fun deleteSolution(solution: Solution) {
        solutionGateway.remove(solution)
        refresh()
    }

    fun loadSolution(newName: String): Solution? = solutionGateway.load(newName)

    fun updateSolution(solution: Solution) {
        solutionGateway.update(solution)
    }

    fun renameSolution(solution: Solution, oldName: String) =
        solutionGateway.rename(solution, oldName)
}
