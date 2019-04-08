package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.concentration.Concentration
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException
import java.io.Serializable
import java.text.DecimalFormat

class Component(var compound: Compound, desired: Concentration, stock: Concentration? = null) :
    Serializable {

    val fromStock get() = availableConcentration != null

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
        when {
            amount >= 1000 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount / 1000))
                when {
                    compound.liquid -> niceOutput.append(" l")
                    fromStock -> niceOutput.append(" l")
                    else -> niceOutput.append(" kg")
                }
            }
            amount >= 1 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount))
                when {
                    compound.liquid -> niceOutput.append(" ml")
                    fromStock -> niceOutput.append(" ml")
                    else -> niceOutput.append(" g")
                }
            }
            amount >= 0.00001 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000))
                when {
                    compound.liquid -> niceOutput.append(" \u03bcl")
                    fromStock -> niceOutput.append(" \u03bcl")
                    else -> niceOutput.append(" mg")
                }
            }
            else -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000000))
                when {
                    compound.liquid -> niceOutput.append(" nl")
                    fromStock -> niceOutput.append(" nl")
                    else -> niceOutput.append(" \u03bcg")
                }
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
