package com.labessence.biotech.finaldilution.component.concentration

abstract class Concentration(var concentration: Double, var type: ConcentrationType?) {

    override fun toString(): String {
        return concentration.toString() + " [" + type!!.toString() + "]"
    }

    /**
     * Calculate desired mass depending on desired concentration
     *
     * @param volume - volume[ml] of the final solution
     * @return - desired mass[g]
     */
    abstract fun calcDesiredMass(volume: Double, molarMass: Double): Double

    /**
     * Calculate component volume using calculated desired mass
     *
     * @param mass - mass[g] required in final solution
     * @return - volume[ml] of compound to be taken
     */
    abstract fun calcVolumeForDesiredMass(mass: Double, molarMass: Double): Double
}
