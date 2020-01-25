package com.labwhisper.biotech.finaldilution.component.validation

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType

data class ComponentValidateResponseModel(
    val isStock: Boolean,
    val desiredConcentrationType: ConcentrationType,
    val stockConcentrationType: ConcentrationType?

    //FIXME:
    // - only one response model for concentration
    // - list of valid states

    // - StateFix class
    //      - if current state is within valid states - do nothing
    //      - otherwise apply fix method interface
    //      - simplest implementation: set nearest
)
