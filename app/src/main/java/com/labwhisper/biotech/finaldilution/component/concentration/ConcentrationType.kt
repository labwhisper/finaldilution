package com.labwhisper.biotech.finaldilution.component.concentration

enum class ConcentrationType(val value: Int) {
    PERCENTAGE(0),
    MOLAR(1),
    MILIMOLAR(2),
    MILIGRAM_PER_MILLILITER(3),
    NX(4);

    fun hint(): String {
        return when (this.value) {
            0 -> "%"
            1 -> "mol/l"
            2 -> "mmol/l"
            3 -> "mg/ml"
            4 -> "n times concentrated"
            else -> ""
        }
    }

    override fun toString(): String {
        return when (this.value) {
            0 -> "%"
            1 -> "M"
            2 -> "mM"
            3 -> "mg/ml"
            4 -> "nX"
            else -> ""
        }
    }

    companion object {
        private val map = values().associateBy(ConcentrationType::value)
        fun fromInt(type: Int) = map[type]
    }

}
