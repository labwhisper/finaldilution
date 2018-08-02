package com.labessence.biotech.finaldilution.genericitem

import android.content.Intent
import android.util.Log

fun Intent.putExtra(item: Item) {
    putExtra(item.seriesName, item)
    Log.d("PASSING INTO INTENT", item.seriesName + ": " + item.name)
}