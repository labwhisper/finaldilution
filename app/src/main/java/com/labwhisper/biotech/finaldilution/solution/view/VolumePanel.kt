package com.labwhisper.biotech.finaldilution.solution.view

import android.content.Context
import android.content.res.Configuration
import android.text.InputType
import android.view.GestureDetector
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.ViewSwitcher
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.peripherals.gestures.TapGestureController
import com.labwhisper.biotech.finaldilution.peripherals.gestures.TapGestureListener
import java.text.DecimalFormat

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

class VolumePanel internal constructor(private val activity: EditActivity) : TapGestureController {
    private val switcher by lazy<ViewSwitcher> { activity.findViewById(R.id.volumeViewSwitcher) }
    private val volumeTextView by lazy<TextView> { activity.findViewById(R.id.beakerVolumeTextView) }
    private val volumeEditText by lazy<TextView> { activity.findViewById(R.id.beakerVolumeEditText) }
    private val volFormat = DecimalFormat("0.##")

    private val onVolumeEditorActionListener =
        TextView.OnEditorActionListener { textView, _, _ ->
            updateVolumeAndStopEdition(textView.text)
            true
        }

    private fun updateVolumeAndStopEdition(text: CharSequence) {
        if (switcher.currentView != volumeTextView) {
            switcher.showNext()
        }
        try {
            if (text.isNotEmpty()) {
                val volume = java.lang.Double.parseDouble(text.toString().replace(',', '.'))
                updateVolume(volume)
                activity.refresh()
            }
        } catch (e: NumberFormatException) {
            // TODO NumberFormatException... get back old text
        }
    }

    internal fun displayVolumeText() {
        updateVolumeTextView()
        volumeEditText.text = volFormat.format(activity.solution.volume)
        volumeEditText.setSelectAllOnFocus(true)
        volumeEditText.setOnEditorActionListener(onVolumeEditorActionListener)

        //Set keyboard to numerical and to show immediately
        volumeEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        volumeEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
        volumeEditText.setOnFocusChangeListener { v, hasFocus ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus) imm.showSoftInput(v, 0)
            else updateVolumeAndStopEdition((v as EditText).text)
        }
        volumeTextView.setOnFocusChangeListener { _, hasFocus ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus) {
                imm.hideSoftInputFromWindow(volumeEditText.windowToken, 0)
            }
        }
        switcher.showNext()
    }

    internal fun displayBeakerImage() {
        val beakerImage = activity.findViewById<View>(R.id.beaker)
        val tapDetector = GestureDetector(
            activity,
            TapGestureListener(this)
        )
        beakerImage.setOnTouchListener { _, motionEvent ->
            tapDetector.onTouchEvent(motionEvent)
            true
        }
    }

    fun startVolumeEdition() {
        if (switcher.currentView == volumeTextView) {
            switcher.showNext()
            volumeEditText.requestFocus()
        }
    }

    internal fun updateVolumeTextView() {
        volumeTextView.text = String.format(
            "%sml",
            volFormat.format(activity.solution.volume)
        )
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(volumeEditText.windowToken, 0)
    }

    private fun updateVolume(volume: Double) {
        activity.solution.volume = volume
        activity.solution.recalculateVolume(volume)
    }

    override fun onTap() {
        startVolumeEdition()
    }

}