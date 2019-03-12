package com.labwhisper.biotech.finaldilution.util

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.View
import android.widget.*

fun View.textView(textViewId: Int): TextView? {
    val findViewById = findViewById<TextView>(textViewId)
    return findViewById
}

fun Activity.textView(textViewId: Int): TextView? {
    val findViewById = findViewById<TextView>(textViewId)
    return findViewById
}

fun Activity.editText(editTextId: Int): EditText {
    return findViewById(editTextId)
}

fun Activity.button(id: Int): Button {
    return findViewById(id)
}

fun Activity.imageButton(id: Int): ImageButton {
    return findViewById(id)
}

fun Activity.listView(id: Int): ListView {
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