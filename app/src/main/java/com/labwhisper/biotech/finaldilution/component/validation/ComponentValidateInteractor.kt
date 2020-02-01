package com.labwhisper.biotech.finaldilution.component.validation

import com.labwhisper.biotech.finaldilution.component.EditComponentAction.STOCK_CLOSED
import com.labwhisper.biotech.finaldilution.component.EditComponentAction.STOCK_OPENED
import com.labwhisper.biotech.finaldilution.component.concentration.ChooseMostSuitableConcentrationInteractor
import com.labwhisper.biotech.finaldilution.component.concentration.CompatibleConcentrationsInteractor
import com.labwhisper.biotech.finaldilution.component.concentration.PossibleConcentrationsInteractor

class ComponentValidateInteractor(
    private val componentValidateOutputPort: ComponentValidateOutputPort,
    private val possibleConcentrationsInteractor: PossibleConcentrationsInteractor,
    private val chooseMostSuitableConcentrationInteractor: ChooseMostSuitableConcentrationInteractor,
    private val compatibleConcentrationsInteractor: CompatibleConcentrationsInteractor
) : ComponentValidateInputPort {

    override fun componentChangeRequest(componentValidateRequestModel: ComponentValidateRequestModel) {
        onComponentChange(componentValidateRequestModel)
    }

    private fun onComponentChange(componentValidateRequestModel: ComponentValidateRequestModel) {
        val componentResponse = validate(componentValidateRequestModel)
        componentValidateOutputPort.exposeValidatedComponentData(componentResponse)
    }

    fun validate(request: ComponentValidateRequestModel): ComponentValidateResponseModel {

        val stockOpen = when (request.action) {
            STOCK_OPENED -> true
            STOCK_CLOSED -> false
            else -> request.wasStockOpen
        }

        val possibleConcentrations = possibleConcentrationsInteractor.getPossibleConcentrations(
            liquid = request.liquid,
            molarMassGiven = request.molarMassGiven
        )

        val bestPossibleCurrent =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                request.currentConcentrationType,
                request.oppositeConcentrationType,
                possibleConcentrations
            )

        val bestPossibleOpposite =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                request.oppositeConcentrationType,
                request.currentConcentrationType,
                possibleConcentrations
            )

        if (request.action == STOCK_CLOSED) {
            return ComponentValidateResponseModel(
                request.action,
                stockOpen,
                bestPossibleCurrent,
                bestPossibleOpposite
            )
        }

        if (possibleConcentrations.contains(request.currentConcentrationType)) {
            return ComponentValidateResponseModel(
                request.action,
                stockOpen,
                request.currentConcentrationType,
                bestPossibleOpposite
            )
        }


        val compatibleList =
            compatibleConcentrationsInteractor.getCompatibleConcentrations(
                request.liquid, request.currentConcentrationType
            )
        if (compatibleList.isEmpty()) {
            return ComponentValidateResponseModel(
                request.action,
                stockOpen,
                bestPossibleCurrent,
                bestPossibleCurrent
            )
        }

        val bestCompatible =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                request.oppositeConcentrationType,
                request.currentConcentrationType,
                compatibleList
            )

        return ComponentValidateResponseModel(
            request.action,
            true,
            request.currentConcentrationType,
            bestCompatible
        )

    }

}

