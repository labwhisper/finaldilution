package com.labessence.biotech.finaldilution.compound

import com.labessence.biotech.finaldilution.genericitem.Item
import java.text.DecimalFormat

data class Compound(
    val iupacName: String,
    val liquid: Boolean,
    val molarMass: Double? = null,
    val trivialName: String? = null,
    val chemicalFormula: String? = null
) : Item {
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
            } ?: "[liquid]".takeIf { liquid } ?: "[variable]"
        }

    override fun toString(): String {
        return "$displayName [$displayMass]"
    }

}
