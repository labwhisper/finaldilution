package com.labwhisper.biotech.finaldilution.component.view

import com.labwhisper.biotech.finaldilution.component.EditComponentAction.STOCK_CHANGED
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateOutputPort
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateResponseModel

class EditComponentPresenter(val appModel: ComponentEditAppModel) :
    ComponentValidateOutputPort {

    override fun exposeValidatedComponentData(componentValidateResponseModel: ComponentValidateResponseModel) {
        presentData(componentValidateResponseModel)
    }

    //TODO Write test
    fun presentData(componentResponseModel: ComponentValidateResponseModel) {
        val stockOrigin = componentResponseModel.action == STOCK_CHANGED
        appModel.fromStock.value = componentResponseModel.isStock
        val desiredResult =
            if (stockOrigin) componentResponseModel.oppositeConcentrationType
            else componentResponseModel.currentConcentrationType
        val stockResult =
            if (stockOrigin) componentResponseModel.currentConcentrationType
            else componentResponseModel.oppositeConcentrationType
        appModel.desiredConcentrationType.value = desiredResult
        appModel.stockConcentrationType.value = stockResult
        appModel.stockConcentrationsAvailable.value =
            componentResponseModel.stockConcentrationsAvailable
    }

}