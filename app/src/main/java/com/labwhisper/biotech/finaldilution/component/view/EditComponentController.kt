package com.labwhisper.biotech.finaldilution.component.view

import com.labwhisper.biotech.finaldilution.component.EditComponentAction.*
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateInputPort
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateRequestModel
import com.labwhisper.biotech.finaldilution.compound.Compound

class EditComponentController(private val componentValidateInputPort: ComponentValidateInputPort) {

    // TODO Create clickModel
    // TODO Unify

    fun changeDesireConcentration(
        compound: Compound,
        desiredConcentrationType: ConcentrationType,
        stockConcentrationType: ConcentrationType?,
        wasStockOpen: Boolean
    ) {
        val request = ComponentValidateRequestModel(
            desiredConcentrationType = desiredConcentrationType,
            stockConcentrationType = stockConcentrationType,
            wasStockOpen = wasStockOpen,
            action = DESIRED_CHANGED,
            liquid = compound.liquid,
            molarMassGiven = compound.molarMass != null,
            densityGiven = compound.density != null
        )
        componentValidateInputPort.componentChangeRequest(request)
    }

    fun changeStockConcentration(
        compound: Compound,
        desiredConcentrationType: ConcentrationType,
        stockConcentrationType: ConcentrationType?,
        wasStockOpen: Boolean
    ) {
        val request = ComponentValidateRequestModel(
            desiredConcentrationType = desiredConcentrationType,
            stockConcentrationType = stockConcentrationType,
            wasStockOpen = wasStockOpen,
            action = STOCK_CHANGED,
            liquid = compound.liquid,
            molarMassGiven = compound.molarMass != null,
            densityGiven = compound.density != null
        )
        componentValidateInputPort.componentChangeRequest(request)
    }

    fun toggleStock(
        compound: Compound,
        desiredConcentrationType: ConcentrationType,
        stockConcentrationType: ConcentrationType?,
        wasStockOpen: Boolean
    ) {
        val request = ComponentValidateRequestModel(
            desiredConcentrationType = desiredConcentrationType,
            stockConcentrationType = stockConcentrationType,
            wasStockOpen = wasStockOpen,
            action = if (wasStockOpen) STOCK_CLOSED else STOCK_OPENED,
            liquid = compound.liquid,
            molarMassGiven = compound.molarMass != null,
            densityGiven = compound.density != null
        )
        componentValidateInputPort.componentChangeRequest(request)
    }


}