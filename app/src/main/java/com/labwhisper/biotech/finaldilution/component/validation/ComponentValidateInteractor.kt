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
                request.currentConcentrationType,
                request.oppositeConcentrationType,
                possibleConcentrations
            )

        val currentCompatibleList =
            compatibleConcentrationsInteractor.getCompatibleConcentrations(
                liquid = request.compound.liquid,
                molarMassGiven = request.compound.molarMassGiven,
                concentrationType = request.currentConcentrationType
            )

        val bestCompatibleFromCurrent =
            chooseMostSuitableConcentrationInteractor.chooseMostSuitableConcentration(
                request.oppositeConcentrationType,
                request.currentConcentrationType,
                currentCompatibleList
            )

        val bestPossibleCurrentCompatibleList =
            compatibleConcentrationsInteractor.getCompatibleConcentrations(
                request.compound.liquid, request.compound.molarMassGiven, bestPossibleCurrent
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

    companion object {

        private const val TAG = "Component validator"
    }

}

