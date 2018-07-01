package com.labessence.biotech.finaldilution.component

import com.labessence.biotech.finaldilution.component.concentration.Concentration
import com.labessence.biotech.finaldilution.compound.Compound
import java.io.Serializable
import java.util.*

class Component : Serializable {

    var fromStock: Boolean = false

    var desiredConcentration: Concentration? = null
    var availableConcentration: Concentration? = null
    var compound: Compound? = null
    private var solutionVolume = 0.0

    constructor(solutionVolume: Double, compound: Compound) {
        this.solutionVolume = solutionVolume
        this.compound = compound
    }

    internal constructor(compound: Compound, desired: Concentration, stock: Concentration) {
        this.compound = compound
        desiredConcentration = desired
        fromStock = true
        availableConcentration = stock
    }

    internal constructor(compound: Compound, desired: Concentration) {
        this.compound = compound
        desiredConcentration = desired
        fromStock = false
    }

    override fun toString(): String {
        return if (compound == null) {
            ""
        } else compound!!.shortName + " : " + getAmountString(solutionVolume)
    }

    fun getAmountString(volume: Double): String {

        val amount = getQuantity(volume)
        val niceOutput = StringBuilder(200)
        if (amount > 1) {
            niceOutput.append(String.format(Locale.ENGLISH, "%1$,.3f", amount))
            if (fromStock) {
                niceOutput.append(" ml")
            } else {
                niceOutput.append(" g")
            }
        } else {
            niceOutput.append(String.format(Locale.ENGLISH, "%1$,.1f", amount * 1000))
            if (fromStock) {
                niceOutput.append(" ul")
            } else {
                niceOutput.append(" mg")
            }
        }
        return niceOutput.toString()
    }

    fun getQuantity(volume: Double): Double {
        val M = compound!!.molarMass
        return if (fromStock) {
            availableConcentration!!.calcVolumeForDesiredMass(desiredConcentration!!
                    .calcDesiredMass(volume, M), M)
        } else {
            desiredConcentration!!.calcDesiredMass(volume, M)
        }
    }

    fun setSolutionVolume(solutionVolume: Double) {
        this.solutionVolume = solutionVolume
    }
}
