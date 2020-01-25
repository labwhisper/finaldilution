package com.labwhisper.biotech.finaldilution.component.view

import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateOutputPort
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateResponseModel

class EditComponentPresenter(val appModel: ComponentEditAppModel) :
    ComponentValidateOutputPort {

    override fun exposeValidatedComponentData(componentValidateResponseModel: ComponentValidateResponseModel) {
        presentData(componentValidateResponseModel)
    }

    fun presentData(componentResponseModel: ComponentValidateResponseModel) {
        appModel.fromStock.value = componentResponseModel.isStock
        appModel.desiredConcentrationType.value = componentResponseModel.desiredConcentrationType
        appModel.stockConcentrationType.value = componentResponseModel.stockConcentrationType
    }

}