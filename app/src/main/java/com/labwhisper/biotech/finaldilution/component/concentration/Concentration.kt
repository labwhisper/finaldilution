package com.labwhisper.biotech.finaldilution.component.concentration

import java.io.Serializable
import java.text.DecimalFormat

abstract class Concentration(open var concentration: Double, var type: ConcentrationType) :
    Serializable {

    override fun toString(): String {
        return "${DecimalFormat("0.###").format(concentration)}${type.unit()}"
    }

    /**
     * Calculate desired mass depending on desired concentration
     *
     * @param volume - volume[ml] of the final solution
     * @return - desired mass[g]
     */
    abstract fun calcDesiredMass(volume: Double, molarMass: Double?): Double

    /**
     * Calculate component volume using calculated desired mass
     *
     * @param mass - mass[g] required in final solution
     * @return - volume[ml] of compound to be taken
     */
    abstract fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Concentration) return false

        if (concentration != other.concentration) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = concentration.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }


}
