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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ComponentValidateInteractorTest {

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
//
//    @Test
//    fun ``(){
//
//    }

//      -> concentrationChange -> getPossibleConcList
//              -> isPossible -> (don't open/close stock) -> validate other (set to possible)
//              -> isNotPossible -> isStockPossible -> open stock -> getCompatibilityList
//                      -> validate other (set to compatible)
//      -> stockOpened (same as desiredConcentrationChange)
//      -> currentClosed -> getPossibleConcList
//              -> isPossible -> ok
//              -> isNotPossible -> validate this (set to possible) (don't open stock)

    //TODO Because action is removed from interactor, it should be added in fragment?

    @ParameterizedTest
    @MethodSource("testCaseProvider")
    fun `Compound options are validated properly`(requestModel: ComponentValidateRequestModel) {


        val responseModel = sut.validate(requestModel)

        checkIfClickedButtonIsNotChangedUnlessItsMgMlForStock(requestModel, responseModel)
        checkIfStockWasntClosed(requestModel, responseModel)
        checkIfStockWasOpenedOnDesiredNx(requestModel, responseModel)
        checkIfNxWasUncheckedOnStockClose(requestModel, responseModel)
        checkIfNxIsPropagatedInBothDirections(requestModel, responseModel)
        checkIfNxIsDismissedInBothDirections(requestModel, responseModel)
        checkIfMgMlIsPropagatedInBothDirectionsForSolids(requestModel, responseModel)
        checkIfMolarsArePropagatedInBothDirectionsWhenNoMolarMass(requestModel, responseModel)
    }

    private fun checkIfClickedButtonIsNotChangedUnlessItsMgMlForStock(
        requestModel: ComponentValidateRequestModel,
        responseModel: ComponentValidateResponseModel
    ) {
        if (requestModel.liquid && requestModel.currentConcentrationType == MILIGRAM_PER_MILLILITER) {
            return
        }
        assertEquals(
            requestModel.currentConcentrationType,
            responseModel.currentConcentrationType
        )
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
        if (requestModel.currentConcentrationType == NX) {
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
        if (requestModel.currentConcentrationType == NX) {
            assertEquals(NX, responseModel.oppositeConcentrationType)
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
        if (requestModel.liquid) return

        if (!requestModel.molarMassGiven
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

        if (!requestModel.molarMassGiven && requestModel.action == STOCK_CHANGED
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
            for (desired in ConcentrationType.values()) {
                for (stock in ConcentrationType.values()) {
                    for (stockOpen in setOf(true, false)) {
                        for (action in setOf(STOCK_CHANGED, DESIRED_CHANGED)) {
                            for (liquid in setOf(true, false)) {
                                for (molarMassGiven in setOf(true, false)) {
                                    for (densityGiven in setOf(true, false)) {
                                        argumentList.add(
                                            arguments(
                                                ComponentValidateRequestModel(
                                                    desired,
                                                    stock,
                                                    stockOpen,
                                                    action,
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
