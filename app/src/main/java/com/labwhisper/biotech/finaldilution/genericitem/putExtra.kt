package com.labwhisper.biotech.finaldilution.genericitem

import android.content.Intent
import android.util.Log

fun Intent.putExtraAnItem(item: Item) {
    putExtra(item.seriesName, item)
    Log.d("PASSING INTO INTENT", item.seriesName + ": " + item.name)
}