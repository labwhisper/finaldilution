package com.labwhisper.biotech.finaldilution.component.validation

import com.labwhisper.biotech.finaldilution.component.EditComponentAction
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType

data class ComponentValidateResponseModel(
    val action: EditComponentAction,
    val isStock: Boolean,
    val currentConcentrationType: ConcentrationType,
    val oppositeConcentrationType: ConcentrationType
)
