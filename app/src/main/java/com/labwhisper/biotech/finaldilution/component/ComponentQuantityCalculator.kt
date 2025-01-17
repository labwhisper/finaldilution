package com.labwhisper.biotech.finaldilution.component

import com.labwhisper.biotech.finaldilution.component.concentration.CongruentConcentrationsInteractor
import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException
import java.text.DecimalFormat

class ComponentQuantityCalculator(
    private val congruentConcentrationsInteractor: CongruentConcentrationsInteractor
) {

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
                    val congruentConcentrations =
                        congruentConcentrationsInteractor.getCongruentConcentrations(
                            liquid = compound.liquid,
                            concentrationType = desiredConcentration.type
                        )
                    availableConcentration?.let { avConc ->
                        if (congruentConcentrations.contains(avConc.type)) {
                            return volume *
                                    desiredConcentration.concentration *
                                    avConc.multiplicationFactor /
                                    avConc.concentration /
                                    desiredConcentration.multiplicationFactor
                        }
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
                    resultInVolume(component) -> niceOutput.append(" l")
                    else -> niceOutput.append(" kg")
                }
            }
            amount == 0.0 || amount >= 1 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount))
                when {
                    resultInVolume(component) -> niceOutput.append(" ml")
                    else -> niceOutput.append(" g")
                }
            }
            amount >= 0.00001 -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000))
                when {
                    resultInVolume(component) -> niceOutput.append(" \u03bcl")
                    else -> niceOutput.append(" mg")
                }
            }
            else -> {
                niceOutput.append(DecimalFormat("0.###").format(amount * 1000000))
                when {
                    resultInVolume(component) -> niceOutput.append(" nl")
                    else -> niceOutput.append(" \u03bcg")
                }
            }
        }
        return niceOutput.toString()
    }

    fun resultInVolume(component: Component) = component.run {
        fromStock || (compound.liquid && !(molarNoDensity(component)))

    }

    fun noVolumeBecauseOfNoDensity(component: Component) = component.run {
        compound.liquid && molarNoDensity(component)
    }

    private fun molarNoDensity(component: Component) = component.run {
        val concentrationsAreCongruent = availableConcentration?.let {
            congruentConcentrationsInteractor.getCongruentConcentrations(
                compound.liquid,
                desiredConcentration.type
            ).contains(it.type)
        }
        compound.density == null && desiredConcentration.type.isMolarLike()
                && (concentrationsAreCongruent != true)
    }

}