package com.labessence.biotech.finaldilution.solution

import com.labessence.biotech.finaldilution.component.Component
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.genericitem.Item
import java.util.*

class Solution : Item {

    override var name: String = ""
    var volume: Double = 0.toDouble()

    val components = ArrayList<Component>()

    val isOverflown: Boolean
        get() = allLiquidComponentsVolume > volume

    val allLiquidComponentsVolume: Double
        get() {
            var allLiquidComponentsVolume = 0.0
            for (component in components) {
                if (component.fromStock) {
                    allLiquidComponentsVolume += component.getQuantity(volume)
                }
            }
            return allLiquidComponentsVolume
        }

    constructor() {}

    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, volume: Double) {
        this.name = name
        this.volume = volume
    }

    fun recalculateVolume(volume: Double) {
        this.volume = volume
        for (component in components) {
            component.setSolutionVolume(volume)
        }
    }

    fun calculateQuantities(): String {
        val niceOutput = StringBuilder(800)
        for (component in components) {
            niceOutput.append(component.getAmountString(volume))
            niceOutput.append(System.getProperty("line.separator"))
        }
        return niceOutput.toString()
    }


    fun getComponents(): List<Component> {
        return components
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
}
