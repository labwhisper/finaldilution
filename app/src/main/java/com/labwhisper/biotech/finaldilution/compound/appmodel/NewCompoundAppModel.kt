package com.labwhisper.biotech.finaldilution.compound.appmodel

import com.labwhisper.biotech.finaldilution.compound.Compound

class NewCompoundAppModel {

    var initialCompound: Compound? = null
        set(value) {
            field = value
            if (anyAdvancedFieldFilled(value)) {
                advancedOptions = true
            }
        }

    private fun anyAdvancedFieldFilled(compound: Compound?) =
        !compound?.trivialName.isNullOrBlank() || !compound?.chemicalFormula.isNullOrBlank()

    var advancedOptions: Boolean = false
}