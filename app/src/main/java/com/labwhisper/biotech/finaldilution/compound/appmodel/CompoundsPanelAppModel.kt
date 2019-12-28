package com.labwhisper.biotech.finaldilution.compound.appmodel

import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.CompoundSearch
import com.labwhisper.biotech.finaldilution.compound.view.CompoundListAdapter
import com.labwhisper.biotech.finaldilution.solution.CareTaker
import com.labwhisper.biotech.finaldilution.solution.Solution

class CompoundsPanelAppModel(
    private val appState: ApplicationContext,
    val compoundListAdapter: CompoundListAdapter,
    var solution: Solution,
    var careTaker: CareTaker<Solution>
) {

    init {
        compoundListAdapter.compoundList = compoundList
        compoundListAdapter.compoundsInSolution =
            solution.components.map { it.compound }.toMutableList()
    }

    val compoundList
        get() = appState.loadAllCompoundsSorted().toMutableList()

    fun filterCompoundList(text: CharSequence?) {
        compoundListAdapter.compoundList =
            CompoundSearch.searchForCompound(compoundList, text.toString()).toMutableList()
        compoundListAdapter.notifyDataSetChanged()
    }

    fun deleteCompound(compound: Compound) {
        appState.saveCurrentWorkOnSolution(solution)
        appState.removeCompoundFromEverywhere(compound)
        compoundListAdapter.compoundList = compoundList
        compoundListAdapter.notifyDataSetChanged()
    }

}
