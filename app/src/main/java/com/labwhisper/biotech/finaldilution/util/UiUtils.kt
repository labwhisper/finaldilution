package com.labwhisper.biotech.finaldilution.util

import android.app.Activity
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

fun View.textView(id: Int): TextView {
    return findViewById(id)
}

fun View.checkBox(id: Int): CheckBox {
    return findViewById(id)
}

fun Activity.textView(textViewId: Int): TextView? {
    val findViewById = findViewById<TextView>(textViewId)
    return findViewById
}

fun Activity.editText(editTextId: Int): EditText? {
    return findViewById(editTextId)
}

fun Activity.button(id: Int): Button {
    return findViewById(id)
}

fun Fragment.button(id: Int): Button {
    return requireActivity().findViewById(id)
}

fun Activity.view(id: Int): View {
    return findViewById(id)
}

fun Activity.imageButton(id: Int): ImageButton {
    return findViewById(id)
}

fun Fragment.imageButton(id: Int): ImageButton {
    return requireActivity().findViewById(id)
}

fun Activity.imageView(id: Int): ImageView {
    return findViewById(id)
}

fun Fragment.imageView(id: Int): ImageView {
    return requireActivity().findViewById(id)
}

fun View.imageView(id: Int): ImageView {
    return findViewById(id)
}

fun Activity.listView(id: Int): ListView {
    return findViewById(id)
}

fun Activity.recyclerView(id: Int): RecyclerView {
    return findViewById(id)
}

fun Fragment.textView(id: Int): TextView {
    return requireActivity().findViewById(id)
}

fun Fragment.editText(id: Int): EditText {
    return requireActivity().findViewById(id)
}

fun Fragment.editTextValue(id: Int): String {
    return editText(id).text.toString()
}

fun Activity.radioButton(id: Int): RadioButton {
    return findViewById(id)
}

fun Fragment.radioGroup(id: Int): RadioGroup {
    return requireActivity().findViewById(id)
}

fun Fragment.radioButton(id: Int): RadioButton {
    return requireActivity().findViewById(id)
}

fun View.editText(id: Int): EditText {
    return findViewById(id)
}

fun View.editTextValue(id: Int): String {
    return editText(id).text.toString()
}

fun Fragment.constraintLayout(id: Int): ConstraintLayout {
    return requireActivity().findViewById(id)
}

fun Fragment.editText(constraintLayoutId: Int, editTextId: Int): EditText {
    return constraintLayout(constraintLayoutId).editText(editTextId)
}

fun Fragment.textView(constraintLayoutId: Int, id: Int): TextView {
    return constraintLayout(constraintLayoutId).textView(id)
}

fun Fragment.editTextValue(constraintLayoutId: Int, editTextId: Int): String {
    return editText(constraintLayoutId, editTextId).text.toString()
}

fun Fragment.string(stringId: Int): String {
    return resources.getString(stringId)
}

fun Fragment.recyclerView(id: Int): RecyclerView {
    return requireActivity().findViewById(id)
}