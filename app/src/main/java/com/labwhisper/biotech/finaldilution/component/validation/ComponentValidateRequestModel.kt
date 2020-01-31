package com.labwhisper.biotech.finaldilution.component.validation

import com.labwhisper.biotech.finaldilution.component.EditComponentAction
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType

data class ComponentValidateRequestModel(
    val currentConcentrationType: ConcentrationType,
    val oppositeConcentrationType: ConcentrationType,
    val wasStockOpen: Boolean,
    val action: EditComponentAction,
    val liquid: Boolean,
    val molarMassGiven: Boolean,
    val densityGiven: Boolean
)

