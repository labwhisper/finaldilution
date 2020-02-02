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

        val currentCompatibleList =
            compatibleConcentrationsInteractor.getCompatibleConcentrations(
                request.liquid, request.molarMassGiven, request.currentConcentrationType
            )

        val bestCompatibleFromCurrent =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                request.oppositeConcentrationType,
                request.currentConcentrationType,
                currentCompatibleList
            )

        val bestPossibleCurrentCompatibleList =
            compatibleConcentrationsInteractor.getCompatibleConcentrations(
                request.liquid, request.molarMassGiven, bestPossibleCurrent
            )

        val bestCompatibleFromBestPossibleCurrent =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                request.oppositeConcentrationType,
                bestPossibleCurrent,
                bestPossibleCurrentCompatibleList
            )

        if (request.action == STOCK_CLOSED) {
            return ComponentValidateResponseModel(
                request.action,
                false,
                bestPossibleCurrent,
                bestCompatibleFromBestPossibleCurrent,
                bestPossibleCurrentCompatibleList
            )
        }

        if (possibleConcentrations.contains(request.currentConcentrationType)) {
            return ComponentValidateResponseModel(
                request.action,
                stockOpen,
                request.currentConcentrationType,
                bestCompatibleFromCurrent,
                currentCompatibleList
            )
        }

        if (currentCompatibleList.isEmpty()) {
            return ComponentValidateResponseModel(
                request.action,
                stockOpen,
                bestPossibleCurrent,
                bestCompatibleFromBestPossibleCurrent,
                bestPossibleCurrentCompatibleList
            )
        }

        return ComponentValidateResponseModel(
            request.action,
            true,
            request.currentConcentrationType,
            bestCompatibleFromCurrent,
            currentCompatibleList
        )

    }

}

