package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.EditComponentAction.*
import com.labwhisper.biotech.finaldilution.component.concentration.ChooseMostSuitableConcentrationInteractor
import com.labwhisper.biotech.finaldilution.component.concentration.CompatibleConcentrationsInteractor
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import com.labwhisper.biotech.finaldilution.component.concentration.PossibleConcentrationsInteractor
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateInteractor
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateOutputPort
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateRequestModel
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateResponseModel
import com.labwhisper.biotech.finaldilution.compound.Compound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ComponentValidateInteractorTest {

    //TODO Tests for Disabled Stock Concentrations

    private val sut = ComponentValidateInteractor(
        object : ComponentValidateOutputPort {
            override fun exposeValidatedComponentData(
                componentValidateResponseModel: ComponentValidateResponseModel
            ) = Unit
        },
        PossibleConcentrationsInteractor(),
        ChooseMostSuitableConcentrationInteractor(),
        CompatibleConcentrationsInteractor()
    )

    @ParameterizedTest
    @MethodSource("testCaseProvider")
    fun `Compound options are validated properly`(requestModel: ComponentValidateRequestModel) {


        val responseModel = sut.validate(requestModel)

        checkIfClickedButtonIsNotChangedUnlessItsMgMlForStock(requestModel, responseModel)
        checkIfStockWasntClosedOnConcentrationChange(requestModel, responseModel)
        checkIfStockWasOpenedOnDesiredNx(requestModel, responseModel)
        checkIfNxWasUncheckedOnStockClose(requestModel, responseModel)
        checkIfNxIsPropagatedInBothDirections(requestModel, responseModel)
        checkIfNxIsDismissedOnStockClose(requestModel, responseModel)
        checkIfNxIsDismissedInBothDirections(requestModel, responseModel)
        checkIfMgMlIsPropagatedInBothDirectionsForSolids(requestModel, responseModel)
        checkIfMolarsArePropagatedInBothDirectionsWhenNoMolarMass(requestModel, responseModel)
    }

    private fun checkIfClickedButtonIsNotChangedUnlessItsMgMlForStock(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {

        if (requestModel.action !in listOf(DESIRED_CHANGED, STOCK_CHANGED)) {
            return
        }

        if (requestModel.compound.liquid && requestModel.currentConcentrationType == MILIGRAM_PER_MILLILITER) {
            return
        }
        assertEquals(
            requestModel.currentConcentrationType,
            responseModel.currentConcentrationType
        )
    }

    private fun checkIfStockWasntClosedOnConcentrationChange(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.wasStockOpen && requestModel.action in listOf(
                DESIRED_CHANGED,
                STOCK_CHANGED
            )
        ) {
            assertEquals(true, responseModel.isStock)
        }
    }

    private fun checkIfStockWasOpenedOnDesiredNx(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.currentConcentrationType == NX
            && requestModel.action == DESIRED_CHANGED
        ) {
            assertEquals(true, responseModel.isStock)
        }
    }

    private fun checkIfNxWasUncheckedOnStockClose(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.wasStockOpen && requestModel.action == STOCK_CLOSED
            && requestModel.currentConcentrationType == NX
        ) {
            assertNotEquals(true, responseModel.currentConcentrationType == NX)
        }
    }

    private fun checkIfNxIsPropagatedInBothDirections(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.action == STOCK_CLOSED) {
            return
        }

        if (requestModel.currentConcentrationType == NX) {
            assertEquals(NX, responseModel.oppositeConcentrationType)
        }
    }

    private fun checkIfNxIsDismissedOnStockClose(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.action == STOCK_CLOSED && requestModel.currentConcentrationType == NX) {
            assertNotEquals(NX, responseModel.currentConcentrationType)
        }
    }

    private fun checkIfNxIsDismissedInBothDirections(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.currentConcentrationType != NX
        ) {
            assertNotEquals(NX, responseModel.oppositeConcentrationType)
        }
    }

    private fun checkIfMgMlIsPropagatedInBothDirectionsForSolids(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.compound.liquid) return

        if (!requestModel.compound.molarMassGiven && requestModel.action != STOCK_CLOSED
            && (requestModel.currentConcentrationType == PERCENTAGE ||
                    requestModel.currentConcentrationType == MILIGRAM_PER_MILLILITER)
            && (requestModel.oppositeConcentrationType != PERCENTAGE &&
                    requestModel.oppositeConcentrationType != MILIGRAM_PER_MILLILITER)
        ) {
            assertEquals(
                requestModel.currentConcentrationType, responseModel.oppositeConcentrationType
            )
        }
    }

    private fun checkIfMolarsArePropagatedInBothDirectionsWhenNoMolarMass(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {

        if (!requestModel.compound.molarMassGiven && requestModel.action != STOCK_CLOSED
            && (requestModel.currentConcentrationType == MOLAR ||
                    requestModel.currentConcentrationType == MILIMOLAR)
            && (requestModel.oppositeConcentrationType != MOLAR &&
                    requestModel.oppositeConcentrationType != MILIMOLAR)
        ) {
            assertEquals(
                requestModel.currentConcentrationType, responseModel.oppositeConcentrationType
            )
        }
    }

    companion object {
        @JvmStatic
        fun testCaseProvider(): Stream<Arguments> {
            val argumentList = mutableListOf<Arguments>()
            for (current in ConcentrationType.values()) {
                for (opposite in ConcentrationType.values()) {
                    for (stockOpen in setOf(true, false)) {
                        for (action in setOf(
                            STOCK_CHANGED, DESIRED_CHANGED, STOCK_OPENED, STOCK_CLOSED
                        )) {
                            for (liquid in setOf(true, false)) {
                                for (molarMass in setOf(1.0, null)) {
                                    argumentList.add(
                                        arguments(
                                            ComponentValidateRequestModel(
                                                currentConcentrationType = current,
                                                oppositeConcentrationType = opposite,
                                                wasStockOpen = stockOpen,
                                                action = action,
                                                compound = Compound(
                                                    iupacName = "Name",
                                                    liquid = liquid,
                                                    molarMass = molarMass
                                                )
                                            )
                                        )
                                    )
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
