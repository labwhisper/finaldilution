package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.EditComponentAction.*
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateInteractor
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateOutputPort
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateRequestModel
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateResponseModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ComponentValidateInteractorTest {

    private val sut = ComponentValidateInteractor(object : ComponentValidateOutputPort {
        override fun exposeValidatedComponentData(componentValidateResponseModel: ComponentValidateResponseModel) {
        }
    })

    @ParameterizedTest
    @MethodSource("testCaseProvider")
    fun `Compound options are validated properly`(requestModel: ComponentValidateRequestModel) {

        val responseModel = sut.validate(requestModel)

        checkIfClickedButtonIsNotChanged(requestModel, responseModel)
        checkIfStockWasntClosed(requestModel, responseModel)
        checkIfStockWasOpenedOnDesiredNx(requestModel, responseModel)
        checkIfNxWasUncheckedOnStockClose(requestModel, responseModel)
        checkIfNxIsPropagatedInBothDirections(requestModel, responseModel)
        checkIfNxIsDismissedInBothDirections(requestModel, responseModel)
        checkIfMgMlIsPropagatedInBothDirections(requestModel, responseModel)
        checkIfMolarsArePropagatedInBothDirectionsWhenNoMolarMass(requestModel, responseModel)
    }

    private fun checkIfClickedButtonIsNotChanged(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.action != STOCK_CHANGED) {
            assertEquals(
                requestModel.desiredConcentrationType,
                responseModel.desiredConcentrationType
            )
        }

        if (requestModel.action == STOCK_CHANGED) {
            assertEquals(
                requestModel.stockConcentrationType,
                responseModel.stockConcentrationType
            )
        }
    }

    private fun checkIfStockWasntClosed(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.wasStockOpen) {
            assertEquals(true, responseModel.isStock)
        }
    }

    private fun checkIfStockWasOpenedOnDesiredNx(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.action != STOCK_CHANGED && requestModel.desiredConcentrationType == NX) {
            assertEquals(true, responseModel.isStock)
        }
    }

    private fun checkIfNxWasUncheckedOnStockClose(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.wasStockOpen && requestModel.action == STOCK_CLOSED
            && requestModel.desiredConcentrationType == NX
        ) {
            assertNotEquals(true, responseModel.desiredConcentrationType == NX)
        }
    }

    private fun checkIfNxIsPropagatedInBothDirections(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.action != STOCK_CHANGED && requestModel.desiredConcentrationType == NX) {
            assertEquals(NX, responseModel.stockConcentrationType)
        }

        if (requestModel.action == STOCK_CHANGED && requestModel.stockConcentrationType == NX) {
            assertEquals(NX, responseModel.desiredConcentrationType)
        }
    }

    private fun checkIfNxIsDismissedInBothDirections(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.action != STOCK_CHANGED && requestModel.desiredConcentrationType != NX
        ) {
            assertNotEquals(NX, responseModel.stockConcentrationType)
        }

        if (requestModel.action == STOCK_CHANGED && requestModel.stockConcentrationType != NX
        ) {
            assertNotEquals(NX, responseModel.desiredConcentrationType)
        }
    }

    private fun checkIfMgMlIsPropagatedInBothDirections(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (!requestModel.molarMassGiven && requestModel.action == STOCK_CHANGED
            && requestModel.stockConcentrationType == MILIGRAM_PER_MILLILITER
        ) {
            assertEquals(MILIGRAM_PER_MILLILITER, responseModel.desiredConcentrationType)
        }

        if (!requestModel.molarMassGiven && requestModel.action != STOCK_CHANGED
            && requestModel.desiredConcentrationType == MILIGRAM_PER_MILLILITER
        ) {
            assertEquals(MILIGRAM_PER_MILLILITER, responseModel.stockConcentrationType)
        }
    }

    private fun checkIfMolarsArePropagatedInBothDirectionsWhenNoMolarMass(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {

        if (!requestModel.molarMassGiven && requestModel.action == STOCK_CHANGED
            && (requestModel.stockConcentrationType == MOLAR ||
                    requestModel.stockConcentrationType == MILIMOLAR)
            && (requestModel.desiredConcentrationType != MOLAR &&
                    requestModel.desiredConcentrationType != MILIMOLAR)
        ) {
            assertEquals(
                requestModel.stockConcentrationType, responseModel.desiredConcentrationType
            )
        }

        if (!requestModel.molarMassGiven && requestModel.action != STOCK_CHANGED
            && (requestModel.desiredConcentrationType == MOLAR ||
                    requestModel.desiredConcentrationType == MILIMOLAR)
            && (requestModel.stockConcentrationType != MOLAR &&
                    requestModel.stockConcentrationType != MILIMOLAR)
        ) {
            assertEquals(
                requestModel.desiredConcentrationType, responseModel.stockConcentrationType
            )
        }

    }

    companion object {
        @JvmStatic
        fun testCaseProvider(): Stream<Arguments> {
            val argumentList = mutableListOf<Arguments>()
            for (desired in ConcentrationType.values()) {
                for (stock in ConcentrationType.values()) {
                    for (stockOpen in setOf(true, false)) {
                        for (stockChange in setOf(STOCK_CHANGED, DESIRED_CHANGED)) {
                            for (liquid in setOf(true, false)) {
                                for (molarMassGiven in setOf(true, false)) {
                                    for (densityGiven in setOf(true, false)) {
                                        argumentList.add(
                                            arguments(
                                                ComponentValidateRequestModel(
                                                    desired,
                                                    stock,
                                                    stockOpen,
                                                    stockChange,
                                                    liquid,
                                                    molarMassGiven,
                                                    densityGiven
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return argumentList.stream()
        }
    }

}
