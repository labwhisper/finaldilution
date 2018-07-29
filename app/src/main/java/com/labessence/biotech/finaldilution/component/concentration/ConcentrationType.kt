package com.labessence.biotech.finaldilution.component.concentration

enum class ConcentrationType(val value: Int) {
    PERCENTAGE(0),
    MOLAR(1),
    MILIMOLAR(2),
    MILIGRAM_PER_MILLILITER(3);

    fun hint(): String {
        return when (this.value) {
            0 -> "%"
            1 -> "M/l"
            2 -> "mM/l"
            3 -> "mg/ml"
            else -> ""
        }
    }

    override fun toString(): String {
        return when (this.value) {
            0 -> "%"
            1 -> "M"
            2 -> "mM"
            3 -> "mg/ml"
            else -> ""
        }
    }

    companion object {
        private val map = ConcentrationType.values().associateBy(ConcentrationType::value)
        fun fromInt(type: Int) = map[type]
    }

}
