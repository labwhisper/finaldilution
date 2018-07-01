package com.labessence.biotech.finaldilution.component.concentration

enum class ConcentrationType private constructor(val value: Int) {
    PERCENTAGE(0),
    MOLAR(1),
    MILIMOLAR(2),
    MILIGRAM_PER_MILLILITER(3);

    override fun toString(): String {
        return when (this.value) {
            0 -> "%"
            1 -> "M"
            2 -> "mM"
            3 -> "mg/ml"
            else -> ""
        }
    }
}
