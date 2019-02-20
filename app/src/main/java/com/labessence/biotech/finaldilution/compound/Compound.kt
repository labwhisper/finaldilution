package com.labessence.biotech.finaldilution.compound

import com.labessence.biotech.finaldilution.genericitem.Item

data class Compound(
    var trivialName: String,
    var molarMass: Double,
    var iupacName: String? = null,
    var chemicalFormula: String? = null
) : Item {
    override val name: String = trivialName
    override val seriesName: String
        get() = "COMPOUND"

    val displayName: CharSequence?
        get() {
            return trivialName.ifBlank {
                iupacName?.takeUnless { it.isBlank() }
                    ?: chemicalFormula?.takeUnless { it.isBlank() } ?: "?"
            }
        }

    override fun toString(): String {
        return "$trivialName [$molarMass]"
    }

}
