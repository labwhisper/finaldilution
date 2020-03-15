package com.labwhisper.biotech.finaldilution.component.validation

import android.util.Log
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
        Log.d(TAG, componentValidateRequestModel.toString())
        Log.d(TAG, componentResponse.toString())
        componentValidateOutputPort.exposeValidatedComponentData(componentResponse)
    }

    fun validate(request: ComponentValidateRequestModel): ComponentValidateResponseModel {

        val stockOpen = when (request.action) {
            STOCK_OPENED -> true
            STOCK_CLOSED -> false
            else -> request.wasStockOpen
        }

        val possibleConcentrations = possibleConcentrationsInteractor.getPossibleConcentrations(
            compound = request.compound
        )

        val bestPossibleCurrent =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                currentConcentrationType = request.currentConcentrationType,
                oppositeConcentrationType = request.oppositeConcentrationType,
                suitableConcentrations = possibleConcentrations
            )

        val currentCompatibleList =
            compatibleConcentrationsInteractor.getCompatibleConcentrations(
                compound = request.compound,
                concentrationType = request.currentConcentrationType
            )

        val bestCompatibleFromCurrent =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                currentConcentrationType = request.oppositeConcentrationType,
                oppositeConcentrationType = request.currentConcentrationType,
                suitableConcentrations = currentCompatibleList
            )

        val bestPossibleCurrentCompatibleList =
            compatibleConcentrationsInteractor.getCompatibleConcentrations(
                compound = request.compound,
                concentrationType = bestPossibleCurrent
            )

        val bestCompatibleFromBestPossibleCurrent =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                currentConcentrationType = request.oppositeConcentrationType,
                oppositeConcentrationType = bestPossibleCurrent,
                suitableConcentrations = bestPossibleCurrentCompatibleList
            )

        if (request.action == STOCK_CLOSED) {
            return ComponentValidateResponseModel(
                action = request.action,
                isStock = false,
                currentConcentrationType = bestPossibleCurrent,
                oppositeConcentrationType = bestCompatibleFromBestPossibleCurrent,
                stockConcentrationsAvailable = bestPossibleCurrentCompatibleList
            )
        }

        if (possibleConcentrations.contains(request.currentConcentrationType)) {
            return ComponentValidateResponseModel(
                action = request.action,
                isStock = stockOpen,
                currentConcentrationType = request.currentConcentrationType,
                oppositeConcentrationType = bestCompatibleFromCurrent,
                stockConcentrationsAvailable = currentCompatibleList
            )
        }

        if (currentCompatibleList.isEmpty()) {
            return ComponentValidateResponseModel(
                action = request.action,
                isStock = stockOpen,
                currentConcentrationType = bestPossibleCurrent,
                oppositeConcentrationType = bestCompatibleFromBestPossibleCurrent,
                stockConcentrationsAvailable = bestPossibleCurrentCompatibleList
            )
        }

        return ComponentValidateResponseModel(
            action = request.action,
            isStock = true,
            currentConcentrationType = request.currentConcentrationType,
            oppositeConcentrationType = bestCompatibleFromCurrent,
            stockConcentrationsAvailable = currentCompatibleList
        )

    }

    companion object {

        private const val TAG = "Component validator"
    }

}

