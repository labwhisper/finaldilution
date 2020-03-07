package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.concentration.Concentration
import com.labwhisper.biotech.finaldilution.component.concentration.MolarConcentration
import com.labwhisper.biotech.finaldilution.compound.Compound
import java.io.Serializable

data class Component(
    val compound: Compound,
    var desiredConcentration: Concentration = MolarConcentration(0.0),
    var availableConcentration: Concentration? = null
) :
    Serializable, Comparable<Component> {

    override fun compareTo(other: Component) = compound.compareTo(other.compound)

    val fromStock get() = availableConcentration != null

    val resultInVolume
        //FIXME for non-molar - all, for molar only with density
        get() = fromStock ||
                (compound.liquid &&
                        (!(compound.density == null && desiredConcentration.type.isMolarLike())))

    val noVolumeBecauseOfNoDensity
        get() = compound.liquid && compound.density == null && desiredConcentration.type
            .isMolarLike()

    private var solutionVolume = 0.0


    override fun toString(): String {
        return "$desiredConcentration ${compound.displayName}"
    }

    fun setSolutionVolume(solutionVolume: Double) {
        this.solutionVolume = solutionVolume
    }
}
