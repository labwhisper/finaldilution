package com.labessence.biotech.finaldilution.util

import android.support.v4.app.Fragment
import android.widget.EditText

fun Fragment.editText(editTextId: Int): EditText {
    return requireActivity().findViewById(editTextId)
}

fun Fragment.editTextValue(editTextId: Int): String {
    return editText(editTextId).text.toString()
}