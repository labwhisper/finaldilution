package com.labwhisper.biotech.finaldilution.solution

import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException
import com.labwhisper.biotech.finaldilution.genericitem.Item
import java.text.DecimalFormat
import java.util.*

data class Solution(
    override var name: String,
    var volume: Double = 0.0,
    val components: MutableList<Component> = ArrayList()
) : Item {

    override val seriesName: String
        get() = "SOLUTION"

    val isOverflown: Boolean
        get() = allLiquidComponentsVolume > volume

    val allLiquidComponentsVolume: Double
        get() {
            return components
                .asSequence()
                .filter { it.fromStock }
                .sumByDouble {
                    try {
                        it.getQuantity(volume)
                    } catch (e: NoMolarMassException) {
                        0.0
                    }
                }
        }

    fun recalculateVolume(volume: Double) {
        this.volume = volume
        components.forEach { component -> component.setSolutionVolume(volume) }
    }

    fun calculateQuantities(): String {
        val niceOutput = StringBuilder(800)
        for (component in components) {
            niceOutput.append(component.getAmountStringForVolume(volume))
            niceOutput.append(System.getProperty("line.separator"))
        }
        return niceOutput.toString()
    }


    fun displayString(): String {
        val volFormat = DecimalFormat("0.###")
        val format = String.format(
            "%s ml  %d components",
            volFormat.format(volume),
            components.size
        )
        return format
    }

    fun displayVolume(): String {
        val volFormat = DecimalFormat("0.###")
        return volFormat.format(volume) + " ml"
    }

    fun removeComponent(component: Component) {
        components.remove(component)
    }

    fun getComponentWithCompound(compound: Compound): Component? {
        //TODO Change to stream when ready on Android
        for (component in components) {
            if (component.compound == compound) {
                return component
            }
        }
        return null
    }

    fun addComponent(component: Component) {
        components.add(component)
    }

    fun deepCopy(): Solution {
        val deepCopy = Solution(name, volume)
        deepCopy.components.apply { addAll(components) }
        return deepCopy
    }

}
