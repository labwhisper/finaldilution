package com.labwhisper.biotech.finaldilution.component.view

import androidx.lifecycle.MutableLiveData
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType

class ComponentEditAppModel {

    private val initialConcentrationType = ConcentrationType.PERCENTAGE

    var fromStock = MutableLiveData<Boolean>()
    val desiredConcentrationType = MutableLiveData<ConcentrationType>().apply {
        value = initialConcentrationType
    }
    val stockConcentrationType = MutableLiveData<ConcentrationType>().apply {
        value = initialConcentrationType
    }
    val stockConcentrationsAvailable = MutableLiveData<List<ConcentrationType>>().apply {
        value = listOf()
    }
    // TODO Add validation (for blink)
    // TODO Add EditTexts Values (to be preserved on config change)
    //
}
