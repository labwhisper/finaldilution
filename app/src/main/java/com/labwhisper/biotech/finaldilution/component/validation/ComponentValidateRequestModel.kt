package com.labwhisper.biotech.finaldilution.component.validation

import com.labwhisper.biotech.finaldilution.component.EditComponentAction
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType
import com.labwhisper.biotech.finaldilution.compound.Compound

data class ComponentValidateRequestModel(
    val currentConcentrationType: ConcentrationType,
    val oppositeConcentrationType: ConcentrationType,
    val wasStockOpen: Boolean,
    val action: EditComponentAction,
    val compound: Compound
) {
    override fun toString(): String {
        return "Request [$currentConcentrationType $oppositeConcentrationType $wasStockOpen " +
                "$action ${compound}]"
    }
}

