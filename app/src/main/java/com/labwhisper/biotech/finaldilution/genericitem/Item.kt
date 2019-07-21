package com.labwhisper.biotech.finaldilution.genericitem

import java.io.Serializable

interface Item : Serializable {
    val name: String
    val seriesName: String
    fun deepCopy(): Item
}
