package com.labessence.biotech.finaldilution.compound

import com.labessence.biotech.finaldilution.genericitem.Item

class Compound(var shortName: String, var molarMass: Double) : Item {
    override val name: String = shortName
    override val seriesName: String
        get() = "COMPOUND"

    var chemicalFormula: String? = null
    var iupacName: String? = null
    val displayName: CharSequence?
        get() {
            var displayedName = shortName
            iupacName?.let { iupac ->
                if (!iupac.isEmpty() && iupac.length < displayedName.length) {
                    displayedName = iupac
                }
            }
            return displayedName
        }

    override fun toString(): String {
        return "$shortName [$molarMass]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val compound = other as Compound

        if (shortName != compound.shortName) return false
        return if (chemicalFormula != null) chemicalFormula == compound.chemicalFormula else compound.chemicalFormula == null

    }

    override fun hashCode(): Int {
        var result = shortName.hashCode()
        result = 31 * result + (chemicalFormula?.hashCode() ?: 0)
        return result
    }
}
