package com.labwhisper.biotech.finaldilution.genericitem

import java.io.Serializable

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 9/11/2017.
 */

interface Item : Serializable {
    val name: String
    val seriesName: String
    fun deepCopy(): Item
}
