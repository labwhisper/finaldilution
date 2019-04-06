package com.labwhisper.biotech.finaldilution.compound.appmodel

import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.compound.CompoundSearch
import com.labwhisper.biotech.finaldilution.compound.view.CompoundListAdapter
import com.labwhisper.biotech.finaldilution.solution.Solution

class CompoundsPanelAppModel(
    private val appState: ApplicationContext,
    val compoundListAdapter: CompoundListAdapter,
    val solution: Solution
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

}
