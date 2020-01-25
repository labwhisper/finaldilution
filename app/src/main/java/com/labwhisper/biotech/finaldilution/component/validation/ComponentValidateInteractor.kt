package com.labwhisper.biotech.finaldilution.component.validation

import com.labwhisper.biotech.finaldilution.component.EditComponentAction.*
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*

class ComponentValidateInteractor(
    private val componentValidateOutputPort: ComponentValidateOutputPort
) : ComponentValidateInputPort {

    override fun componentChangeRequest(componentValidateRequestModel: ComponentValidateRequestModel) {
        onComponentChange(componentValidateRequestModel)
    }

    private fun onComponentChange(componentValidateRequestModel: ComponentValidateRequestModel) {
        val componentResponse = validate(componentValidateRequestModel)
        componentValidateOutputPort.exposeValidatedComponentData(componentResponse)
    }

    fun validate(request: ComponentValidateRequestModel): ComponentValidateResponseModel {

        if (nxIsNotReflected(request)) return ComponentValidateResponseModel(true, NX, NX)

        if (selectedMgMlNoMass(request)) return ComponentValidateResponseModel(
            true,
            MILIGRAM_PER_MILLILITER,
            MILIGRAM_PER_MILLILITER
        )

        if (selectedDesireMolarNoMass(request)) {
            if (request.stockConcentrationType != MOLAR && request.stockConcentrationType != MILIMOLAR)
                return ComponentValidateResponseModel(
                    true,
                    request.desiredConcentrationType,
                    request.desiredConcentrationType
                )
        }

        if (selectedStockMolarNoMass(request)) {
            if (request.desiredConcentrationType != MOLAR && request.desiredConcentrationType != MILIMOLAR)
                return ComponentValidateResponseModel(
                    true,
                    request.stockConcentrationType ?: MOLAR,
                    request.stockConcentrationType
                )
        }

        val isStockOpen = request.wasStockOpen ||
                (request.action == DESIRED_CHANGED
                        && request.desiredConcentrationType == NX)

        if (nxDesiredLeftSingle(request))
            return ComponentValidateResponseModel(
                true,
                MILIGRAM_PER_MILLILITER,
                request.stockConcentrationType
            )

        if (nxStockLeftSingle(request))
            return ComponentValidateResponseModel(
                true,
                request.desiredConcentrationType,
                MILIGRAM_PER_MILLILITER
            )

        return ComponentValidateResponseModel(
            isStockOpen, request.desiredConcentrationType, request.stockConcentrationType
        )
    }

    private fun selectedStockMolarNoMass(request: ComponentValidateRequestModel) =
        (!request.molarMassGiven && stockOrigin(request)
                && (request.stockConcentrationType?.isMolarLike() == true))

    private fun selectedDesireMolarNoMass(request: ComponentValidateRequestModel) =
        (!request.molarMassGiven && noStockOrigin(request)
                && request.desiredConcentrationType.isMolarLike())

    private fun selectedMgMlNoMass(request: ComponentValidateRequestModel): Boolean {
        return ((!request.molarMassGiven && noStockOrigin(request)
                && request.desiredConcentrationType == MILIGRAM_PER_MILLILITER)
                || (!request.molarMassGiven && stockOrigin(request)
                && request.stockConcentrationType == MILIGRAM_PER_MILLILITER))
    }

    private fun nxIsNotReflected(request: ComponentValidateRequestModel): Boolean {
        return ((stockOrigin(request) && request.stockConcentrationType == NX)
                || (noStockOrigin(request) && request.desiredConcentrationType == NX))
    }

    private fun nxStockLeftSingle(request: ComponentValidateRequestModel): Boolean {
        return (noStockOrigin(request)
                && request.desiredConcentrationType != NX
                && request.stockConcentrationType == NX)
    }

    private fun nxDesiredLeftSingle(request: ComponentValidateRequestModel): Boolean {
        return (stockOrigin(request)
                && request.stockConcentrationType != NX
                && request.desiredConcentrationType == NX)
    }

    private fun stockOrigin(request: ComponentValidateRequestModel) =
        request.action == STOCK_CHANGED || request.action == STOCK_CLOSED

    private fun noStockOrigin(request: ComponentValidateRequestModel): Boolean {
        return (request.action == DESIRED_CHANGED
                || request.action == STOCK_OPENED
                || request.action == NO_ACTION)
    }

}

