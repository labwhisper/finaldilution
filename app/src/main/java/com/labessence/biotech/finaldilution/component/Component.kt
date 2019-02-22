package com.labessence.biotech.finaldilution.component

import com.labessence.biotech.finaldilution.component.concentration.Concentration
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.compound.NoMolarMassException
import java.io.Serializable
import java.text.DecimalFormat

class Component(var compound: Compound, desired: Concentration, stock: Concentration? = null) :
    Serializable {

    var fromStock
        get() = availableConcentration != null
        private set(value) {}

    var desiredConcentration: Concentration = desired
    var availableConcentration: Concentration? = stock
    private var solutionVolume = 0.0

    override fun toString(): String {
        return compound.trivialName + " : " + getAmountStringForVolume(solutionVolume)
    }

    fun getAmountStringForVolume(volume: Double): String {
        val amount = try {
            getQuantity(volume)
        } catch (e: NoMolarMassException) {
            return "Error"
        }
        return getAmountString(amount)
    }

    fun getAmountString(amount: Double): String {
        val niceOutput = StringBuilder(200)
        if (amount > 1) {
            niceOutput.append(DecimalFormat("0.###").format(amount))
            when {
                compound.liquid -> niceOutput.append(" ml")
                fromStock -> niceOutput.append(" ml")
                else -> niceOutput.append(" g")
            }
        } else {
            niceOutput.append(DecimalFormat("0.#").format(amount * 1000))
            when {
                compound.liquid -> niceOutput.append(" ul")
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
