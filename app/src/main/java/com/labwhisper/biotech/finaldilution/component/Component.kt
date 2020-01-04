package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.concentration.Concentration
import com.labwhisper.biotech.finaldilution.component.concentration.MolarConcentration
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException
import java.io.Serializable
import java.text.DecimalFormat

data class Component(val compound: Compound) :
    Serializable, Comparable<Component> {

    override fun compareTo(other: Component) = compound.compareTo(other.compound)

    constructor(
        compound: Compound,
        desiredConcentration: Concentration,
        availableConcentration: Concentration? = null
    ) : this(compound) {
        this.desiredConcentration = desiredConcentration
        this.availableConcentration = availableConcentration
    }

    val fromStock get() = availableConcentration != null

    val resultInVolume
        //FIXME for non-molar - all, for molar only with density
        get() = fromStock ||
                (compound.liquid && compound.density != null)

    var desiredConcentration: Concentration = MolarConcentration(0.0)
    var availableConcentration: Concentration? = null
    private var solutionVolume = 0.0


    override fun toString(): String {
        return "$desiredConcentration ${compound.displayName}"
    }

    fun getAmountStringForVolume(volume: Double): String {
        val amount = try {
            getQuantity(volume)
        } catch (e: NoMolarMassException) {
            return "NoMolar!"
        }
        return getAmountString(amount)
    }

    fun getAmountString(amount: Double): String {
        val niceOutput = StringBuilder(200)
        when {
            amount >= 1000 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount / 1000))
                when {
                    resultInVolume -> niceOutput.append(" l")
                    else -> niceOutput.append(" kg")
                }
            }
            amount == 0.0 || amount >= 1 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount))
                when {
                    resultInVolume -> niceOutput.append(" ml")
                    else -> niceOutput.append(" g")
                }
            }
            amount >= 0.00001 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000))
                when {
                    resultInVolume -> niceOutput.append(" \u03bcl")
                    else -> niceOutput.append(" mg")
                }
            }
            else -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000000))
                when {
                    resultInVolume -> niceOutput.append(" nl")
                    else -> niceOutput.append(" \u03bcg")
                }
            }
        }
        return niceOutput.toString()
    }

    fun getQuantity(volume: Double): Double {
        val M = compound.molarMass
        return when {
            fromStock -> {
                if (availableConcentration?.type == desiredConcentration.type) {
                    return desiredConcentration.concentration * volume /
                            (availableConcentration?.concentration ?: 0.0)
                }
                availableConcentration?.calcVolumeForDesiredMass(
                    desiredConcentration.calcDesiredMass(volume, M), M
                ) ?: 0.0
            }
            else -> desiredConcentration.calcDesiredMass(volume, M)
        }
    }

    fun setSolutionVolume(solutionVolume: Double) {
        this.solutionVolume = solutionVolume
    }
}
