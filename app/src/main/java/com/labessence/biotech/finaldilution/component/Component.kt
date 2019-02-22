package com.labessence.biotech.finaldilution.component

import com.labessence.biotech.finaldilution.component.concentration.Concentration
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.compound.NoMolarMassException
import java.io.Serializable
import java.util.*

class Component(var compound: Compound, desired: Concentration, stock: Concentration? = null) :
    Serializable {

    var fromStock
        get() = availableConcentration != null
        private set(value) {}

    var desiredConcentration: Concentration = desired
    var availableConcentration: Concentration? = stock
    private var solutionVolume = 0.0

    override fun toString(): String {
        return compound.trivialName + " : " + getAmountString(solutionVolume)
    }

    fun getAmountString(volume: Double): String {

        val amount = try {
            getQuantity(volume)
        } catch (e: NoMolarMassException) {
            return "Error"
        }
        val niceOutput = StringBuilder(200)
        if (amount > 1) {
            niceOutput.append(String.format(Locale.ENGLISH, "%1$,.3f", amount))
            when {
                fromStock -> niceOutput.append(" ml")
                else -> niceOutput.append(" g")
            }
        } else {
            niceOutput.append(String.format(Locale.ENGLISH, "%1$,.1f", amount * 1000))
            when {
                fromStock -> niceOutput.append(" ul")
                else -> niceOutput.append(" mg")
            }
        }
        return niceOutput.toString()
    }

    fun getQuantity(volume: Double): Double {
        val M = compound.molarMass
        return when {
            fromStock -> availableConcentration!!.calcVolumeForDesiredMass(
                desiredConcentration.calcDesiredMass(volume, M), M
            )
            else -> desiredConcentration.calcDesiredMass(volume, M)
        }
    }

    fun setSolutionVolume(solutionVolume: Double) {
        this.solutionVolume = solutionVolume
    }
}
