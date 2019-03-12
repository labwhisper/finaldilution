package com.labwhisper.biotech.finaldilution.solution.appmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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
            solutionGateway.loadAll()
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

    fun deleteSolution(solution: Solution) {
        solutionGateway.remove(solution)
        refresh()
    }

    fun loadSolution(newName: String): Solution? = solutionGateway.load(newName)
}
