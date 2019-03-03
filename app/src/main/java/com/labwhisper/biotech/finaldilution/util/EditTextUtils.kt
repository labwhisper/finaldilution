package com.labwhisper.biotech.finaldilution.util

import android.app.Activity
import android.support.v4.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

fun Activity.editText(editTextId: Int): EditText {
    return findViewById(editTextId)
}

fun Activity.button(id: Int): Button {
    return findViewById(id)
}

fun Activity.imageButton(id: Int): ImageButton {
    return findViewById(id)
}

fun Fragment.textView(textViewId: Int): TextView {
    return requireActivity().findViewById(textViewId)
}

fun Fragment.editText(editTextId: Int): EditText {
    return requireActivity().findViewById(editTextId)
}

fun Fragment.editTextValue(editTextId: Int): String {
    return editText(editTextId).text.toString()
}