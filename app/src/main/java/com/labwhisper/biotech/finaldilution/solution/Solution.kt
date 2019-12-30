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
) : Item, Comparable<Solution> {

    override fun compareTo(other: Solution) =
        name.toLowerCase(Locale.ENGLISH).compareTo(other.name.toLowerCase(Locale.ENGLISH))

    var componentsAdded: MutableSet<Component> = hashSetOf()
    var isFilledInWithWater: Boolean = false

    override val seriesName: String
        get() = "SOLUTION"

    val isOverflown: Boolean
        get() = allLiquidComponentsVolume > volume

    private val allLiquidComponentsVolume: Double
        get() = components
            .asSequence()
            .filter { it.fromStock || it.compound.liquid }
            .sumByDouble {
                try {
                    it.getQuantity(volume)
                } catch (e: NoMolarMassException) {
                    0.0
                }
            }

    val done
        get() = isFilledInWithWater && componentsAdded.containsAll(components)

    fun recalculateVolume(volume: Double) {
        this.volume = volume
        components.forEach { component -> component.setSolutionVolume(volume) }
    }

    fun displayString() = components.toString()

    fun displayVolume(): String = "${volumeAmountForCurrentUnit()} ${volumeUnit()}"

    fun volumeUnit(): String = when {
        volume == 0.0 -> "ml"
        volume >= 1000 -> "l"
        volume < 1 -> "\u03bcl"
        else -> "ml"
    }

    fun volumeAmountForCurrentUnit(): String = when {
        volume == 0.0 -> DecimalFormat("0.###").format(volume)
        volume >= 1000 -> DecimalFormat("0.###").format(volume / 1000)
        volume < 1 -> DecimalFormat("0.###").format(volume * 1000)
        else -> DecimalFormat("0.###").format(volume)
    }

    fun removeComponent(component: Component) {
        components.remove(component)
    }

    fun getComponentWithCompound(compound: Compound): Component? {
        return components.firstOrNull { it.compound == compound }
    }

    fun addComponent(component: Component) {
        components.add(component)
    }

    override fun deepCopy(): Solution {
        val deepCopy = Solution(name, volume)
        deepCopy.components.apply { addAll(components) }
        deepCopy.componentsAdded = mutableSetOf<Component>().apply { addAll(componentsAdded) }
        deepCopy.isFilledInWithWater = isFilledInWithWater
        return deepCopy
    }

}
