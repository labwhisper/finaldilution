package com.labwhisper.biotech.finaldilution.compound

import com.labwhisper.biotech.finaldilution.genericitem.Item
import java.text.DecimalFormat
import java.util.*

data class Compound(
    val iupacName: String,
    val liquid: Boolean,
    val molarMass: Double? = null,
    val trivialName: String? = null,
    val chemicalFormula: String? = null,
    val density: Double? = null
) : Item, Comparable<Compound> {

    override fun compareTo(other: Compound) = displayName.toLowerCase(Locale.ENGLISH)
        .compareTo(other.displayName.toLowerCase(Locale.ENGLISH))

    override val name: String = iupacName
    override val seriesName: String
        get() = "COMPOUND"

    val displayName: String
        get() {
            return trivialName?.takeUnless { it.isBlank() }
                ?: iupacName.takeUnless { it.isBlank() }
                ?: chemicalFormula?.takeUnless { it.isBlank() } ?: "?"
        }

    val displayMass: String
        get() {
            return molarMass?.let {
                "[${DecimalFormat("0.##").format(molarMass)}]"
            } ?: "[liquid]".takeIf { liquid } ?: "[undefined]"
        }

    val molarMassGiven: Boolean
        get() = molarMass != null

    val densityGiven: Boolean
        get() = density != null

    override fun toString(): String {
        return "$displayName [$displayMass]"
    }

    override fun deepCopy(): Compound {
        return this.copy()
    }

}
