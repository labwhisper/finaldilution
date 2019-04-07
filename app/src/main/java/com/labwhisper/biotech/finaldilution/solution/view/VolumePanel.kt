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


class VolumePanel internal constructor(private val activity: EditActivity) : TapGestureController {
    private val switcher by lazy<ViewSwitcher> { activity.findViewById(R.id.volumeViewSwitcher) }
    private val volumeTextView by lazy<TextView> { activity.findViewById(R.id.beakerVolumeTextView) }
    private val volumeEditText by lazy<TextView> { activity.findViewById(R.id.beakerVolumeEditText) }
    private val volumeEditTextUnit by lazy<TextView> { activity.findViewById(R.id.beakerVolumeEditTextUnit) }

    internal fun displayVolumeText() {
        updateVolumeTextView()
        volumeEditTextUnit.text = activity.solution.volumeUnit()
        volumeEditText.text = activity.solution.volumeAmountForCurrentUnit()
        volumeEditText.setSelectAllOnFocus(true)

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

    private fun updateVolumeAndStopEdition(text: CharSequence) {
        volumeEditText.setSelectAllOnFocus(true)
        if (switcher.currentView != volumeTextView) {
            switcher.showNext()
        }
        try {
            if (text.isNotEmpty()) {
                val volumeInCurrentUnit =
                    java.lang.Double.parseDouble(text.toString().replace(',', '.'))
                when {
                    activity.solution.volumeUnit() == "\u03bcl" -> updateVolume(volumeInCurrentUnit / 1000)
                    activity.solution.volumeUnit() == "l" -> updateVolume(volumeInCurrentUnit * 1000)
                    else -> updateVolume(volumeInCurrentUnit)
                }
                activity.refresh()
            }
        } catch (e: NumberFormatException) {
            // TODO NumberFormatException... get back old text
        }
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

    internal fun updateVolumeTextView() {
        volumeTextView.text = activity.solution.displayVolume()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(volumeEditText.windowToken, 0)
    }

    private fun updateVolume(volume: Double) {
        activity.solution.volume = volume
        activity.solution.recalculateVolume(volume)
        volumeEditTextUnit.text = activity.solution.volumeUnit()
    }

    override fun onTap() {
        startVolumeEdition()
    }

    private fun startVolumeEdition() {
        volumeEditText.text = activity.solution.volumeAmountForCurrentUnit()
        if (switcher.currentView == volumeTextView) {
            switcher.showNext()
            volumeEditText.requestFocus()
        }
    }

}
