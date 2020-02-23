package com.labwhisper.biotech.finaldilution.compound.appmodel

import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.CompoundSearch
import com.labwhisper.biotech.finaldilution.solution.CareTaker
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.appmodel.EditSolutionAppModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class CompoundsPanelAppModel(
    val editSolutionAppModel: EditSolutionAppModel,
    val compoundList: Observable<List<Compound>>,
    var solution: BehaviorSubject<Solution>,
    var careTaker: CareTaker<Solution>
) {

    val filterSequence: BehaviorSubject<CharSequence> = BehaviorSubject.createDefault("")

    val filteredCompoundList = Observable.combineLatest(
        compoundList, filterSequence, BiFunction { compounds: List<Compound>, text: CharSequence? ->
            CompoundSearch.searchForCompound(compounds, text.toString()).toMutableList()
        })

    fun deleteCompound(compound: Compound) {
        editSolutionAppModel.removeCompoundFromEverywhere(compound)
    }

}
