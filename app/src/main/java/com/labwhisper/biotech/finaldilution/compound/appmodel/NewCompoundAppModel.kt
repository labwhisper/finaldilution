package com.labwhisper.biotech.finaldilution.compound.appmodel

import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.solution.appmodel.EditSolutionAppModel

class NewCompoundAppModel(val editSolutionAppModel: EditSolutionAppModel) {

    var initialCompound: Compound? = null
        set(value) {
            field = value
            if (anyAdvancedFieldFilled(value)) {
                advancedOptions = true
            }
        }

    private fun anyAdvancedFieldFilled(compound: Compound?) =
        !compound?.trivialName.isNullOrBlank() || !compound?.chemicalFormula.isNullOrBlank()

    var newCompound: Compound? = null

    var advancedOptions: Boolean = false

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