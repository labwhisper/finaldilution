package com.labwhisper.biotech.finaldilution.compound.appmodel

import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.compound.Compound

class NewCompoundAppModel(private val appState: ApplicationContext) {

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
                appState.renameCompound(compound, it)
            }
            appState.updateCompound(compound, it)
        } ?: appState.safeSaveCompound(compound)
        newCompound = compound
    }
}