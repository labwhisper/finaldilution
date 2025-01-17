package com.labwhisper.biotech.finaldilution.compound.appmodel

import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.solution.appmodel.EditSolutionAppModel
import io.reactivex.subjects.BehaviorSubject

class NewCompoundAppModel(val editSolutionAppModel: EditSolutionAppModel) {

    var initialCompound: Compound? = null
        set(value) {
            field = value
            if (anyAdvancedFieldFilled(value)) {
                advancedOptions.onNext(true)
            }
            densityOpen.onNext(value?.liquid ?: false)
        }

    private fun anyAdvancedFieldFilled(compound: Compound?) =
        !compound?.trivialName.isNullOrBlank()
                || !compound?.chemicalFormula.isNullOrBlank()
                || compound?.density != null

    var newCompound: Compound? = null

    val advancedOptions = BehaviorSubject.createDefault(false)

    val densityOpen = BehaviorSubject.create<Boolean>()

    //TODO Create interactor
    fun proceedWithCompound(compound: Compound) {
        initialCompound?.let {
            if (compound.name != it.name) {
                editSolutionAppModel.renameCompound(compound, it)
            }
            editSolutionAppModel.updateCompound(compound, it)
        } ?: editSolutionAppModel.safeSaveCompound(compound)
        newCompound = compound
    }
}