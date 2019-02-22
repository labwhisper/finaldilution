package com.labessence.biotech.finaldilution.compound

import com.labessence.biotech.finaldilution.genericitem.Item

data class Compound(
    var iupacName: String,
    var molarMass: Double?,
    var trivialName: String? = null,
    var chemicalFormula: String? = null
) : Item {
    override val name: String = iupacName
    override val seriesName: String
        get() = "COMPOUND"

    val displayName: CharSequence?
        get() {
            return trivialName?.takeUnless { it.isBlank() }
                ?: iupacName.takeUnless { it.isBlank() }
                ?: chemicalFormula?.takeUnless { it.isBlank() } ?: "?"
        }

    override fun toString(): String {
        return "$trivialName [$molarMass]"
    }

}
