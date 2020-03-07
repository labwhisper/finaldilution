package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException
import java.text.DecimalFormat

class ComponentQuantityCalculator {

    fun getAmountStringForVolume(component: Component, volume: Double): String {
        val amount = try {
            getQuantity(component, volume)
        } catch (e: NoMolarMassException) {
            return "NoMolar!"
        }
        return getAmountString(component, amount)
    }


    fun getQuantity(component: Component, volume: Double): Double {
        component.run {
            val M = compound.molarMass
            when {
                fromStock -> {
                    if (availableConcentration?.type == desiredConcentration.type) {
                        return desiredConcentration.concentration * volume /
                                (availableConcentration?.concentration ?: 0.0)
                    }
                    return availableConcentration?.calcVolumeForDesiredMass(
                        desiredConcentration.calcDesiredMass(volume, M), M
                    ) ?: 0.0
                }
                else -> {
                    if (desiredConcentration.type.isMolarLike()
                        && compound.liquid
                        && compound.density != null
                    ) {
                        return desiredConcentration.calcDesiredMass(volume, M) / compound.density
                    }
                    return desiredConcentration.calcDesiredMass(volume, M)
                }
            }
        }
    }


    fun getAmountString(component: Component, amount: Double): String {
        val niceOutput = StringBuilder(200)
        when {
            amount >= 1000 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount / 1000))
                when {
                    component.resultInVolume -> niceOutput.append(" l")
                    else -> niceOutput.append(" kg")
                }
            }
            amount == 0.0 || amount >= 1 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount))
                when {
                    component.resultInVolume -> niceOutput.append(" ml")
                    else -> niceOutput.append(" g")
                }
            }
            amount >= 0.00001 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000))
                when {
                    component.resultInVolume -> niceOutput.append(" \u03bcl")
                    else -> niceOutput.append(" mg")
                }
            }
            else -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000000))
                when {
                    component.resultInVolume -> niceOutput.append(" nl")
                    else -> niceOutput.append(" \u03bcg")
                }
            }
        }
        return niceOutput.toString()
    }

}