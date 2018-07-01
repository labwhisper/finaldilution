package com.labessence.biotech.finaldilution.compound

import com.labessence.biotech.finaldilution.genericitem.Item

class Compound : Item {
    override val name: String

    var shortName: String? = null
    var molarMass: Double = 0.toDouble()
    private val longName: String? = null
    private val chemicalFormula: String? = null
    private val iupacName: String? = null

    constructor(shortName: String) {
        this.shortName = shortName
        this.name = shortName
    }

    constructor(shortName: String, molarMass: Double) {
        this.shortName = shortName
        this.molarMass = molarMass
        this.name = shortName
    }

    override fun toString(): String {
        return "$shortName [$molarMass]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val compound = other as Compound?

        if (shortName != compound!!.shortName) return false
        return if (chemicalFormula != null) chemicalFormula == compound.chemicalFormula else compound.chemicalFormula == null

    }

    override fun hashCode(): Int {
        var result = shortName!!.hashCode()
        result = 31 * result + (chemicalFormula?.hashCode() ?: 0)
        return result
    }
}
