package com.labessence.biotech.finaldilution.solution.view

import android.content.Context
import android.content.res.Configuration
import android.text.InputType
import android.view.GestureDetector
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.ViewSwitcher
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.peripherals.gestures.TapGestureController
import com.labessence.biotech.finaldilution.peripherals.gestures.TapGestureListener
import java.text.DecimalFormat

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

class VolumePanel internal constructor(private val activity: EditActivity) : TapGestureController {
    private val appState: ApplicationContext = activity.applicationContext as ApplicationContext
    private val switcher by lazy<ViewSwitcher> { activity.findViewById(R.id.volumeViewSwitcher) }
    private val volumeTextView by lazy<TextView> { activity.findViewById(R.id.beakerVolumeTextView) }
    private val volumeEditText by lazy<TextView> { activity.findViewById(R.id.beakerVolumeEditText) }
    private val volFormat = DecimalFormat("0.##")

    private//Switch to Edit Text
    // TODO NumberFormatException... get back old text
    val onVolumeEditorActionListener: TextView.OnEditorActionListener =
        TextView.OnEditorActionListener()
        { v, _, _ ->
            if (switcher.currentView != volumeTextView) {
                switcher.showNext()
            }
            try {
                val s = v.text
                if (s.isNotEmpty()) {
                    val volume = java.lang.Double.parseDouble(s.toString().replace(',', '.'))
                    updateVolume(volume)
                    activity.refresh()
                }
            } catch (e: NumberFormatException) {
            }

            true
        }

    internal fun displayVolumeText() {
        updateVolumeTextView()
        volumeEditText.text = volFormat.format(appState.currentSolution?.volume ?: 0.0)
        volumeEditText.setSelectAllOnFocus(true)
        volumeEditText.setOnEditorActionListener(onVolumeEditorActionListener)

        //Set keyboard to numerical and to show immediately
        volumeEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        volumeEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
        volumeEditText.setOnFocusChangeListener { v, hasFocus ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus)
                imm.showSoftInput(v, 0)
        }
        volumeTextView.setOnFocusChangeListener { v, hasFocus ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus) {
                imm.hideSoftInputFromWindow(volumeEditText.windowToken, 0)
            }
        }
        switcher.showNext()
    }

    internal fun displayBeakerImage() {
        val beakerImage = activity.findViewById<View>(R.id.beakerRectangle)
        val tapDetector = GestureDetector(
            activity,
            TapGestureListener(this)
        )
        beakerImage.setOnTouchListener { view, motionEvent ->
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
            volFormat.format(appState.currentSolution?.volume ?: 0.0)
        )
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(volumeEditText.windowToken, 0)
    }

    private fun updateVolume(volume: Double) {
        appState.currentSolution?.volume = volume
        appState.currentSolution?.recalculateVolume(volume)
    }

    override fun onTap() {
        startVolumeEdition()
    }

    companion object {

        private val TAG = "Volume panel "
    }

}
