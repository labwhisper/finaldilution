package com.labwhisper.biotech.finaldilution.component.view

import androidx.lifecycle.MutableLiveData
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType

class ComponentEditAppModel {

    private val initialConcentrationType = ConcentrationType.MOLAR

    var fromStock = MutableLiveData<Boolean>()
    val desiredConcentrationType =
        MutableLiveData<ConcentrationType>().apply { value = initialConcentrationType }
    val stockConcentrationType = MutableLiveData<ConcentrationType?>()
    // TODO Add validation (for blink)
    // TODO Add EditTexts Values (to be preserved on config change)
    //
}
// ViewModel - has strings in it, has flags for buttons, has boolean flag for disabled button e.g.
