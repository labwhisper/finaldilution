package com.labwhisper.biotech.finaldilution.solution.view

import android.content.Context
import android.content.res.Configuration
import android.text.InputType
import android.util.Log
import android.view.GestureDetector
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.ViewSwitcher
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.peripherals.gestures.TapGestureController
import com.labwhisper.biotech.finaldilution.peripherals.gestures.TapGestureListener
import io.reactivex.disposables.CompositeDisposable


class VolumePanel internal constructor(private val activity: EditActivity) : TapGestureController {
    private val switcher by lazy<ViewSwitcher> { activity.findViewById(R.id.volumeViewSwitcher) }
    private val volumeTextView by lazy<TextView> { activity.findViewById(R.id.beakerVolumeTextView) }
    private val volumeEditText by lazy<TextView> { activity.findViewById(R.id.beakerVolumeEditText) }
    private val volumeEditTextUnit by lazy<TextView> { activity.findViewById(R.id.beakerVolumeEditTextUnit) }

    private val disposable = CompositeDisposable()

    internal fun displayVolumeText() {
        disposable.add(activity.appModel.solution.subscribe {
            volumeEditTextUnit.text = it.volumeUnit()
            volumeEditText.text = it.volumeAmountForCurrentUnit()
            updateVolumeTextView(it.displayVolume())
        })

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
                    activity.appModel.solution.value?.volumeUnit() == "\u03bcl" -> updateVolume(
                        volumeInCurrentUnit / 1000
                    )
                    activity.appModel.solution.value?.volumeUnit() == "l" -> updateVolume(
                        volumeInCurrentUnit * 1000
                    )
                    else -> updateVolume(volumeInCurrentUnit)
                }
            }
        } catch (e: NumberFormatException) {
            Log.d(TAG, "Cannot change volume", e)
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

    internal fun updateVolumeTextView(volume: CharSequence) {
        volumeTextView.text = volume
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(volumeEditText.windowToken, 0)
    }

    private fun updateVolume(volume: Double) {
        activity.appModel.solution.value?.deepCopy()?.also {
            val oldVolume = it.volume
            it.volume = volume
            if (oldVolume != volume) {
                it.resetPreparation()
            }
        }?.let { activity.appModel.updateSolution(it) }
    }

    override fun onTap() {
        startVolumeEdition()
    }

    private fun startVolumeEdition() {
        volumeEditText.text =
            activity.appModel.solution.value?.volumeAmountForCurrentUnit() ?: return
        if (switcher.currentView == volumeTextView) {
            switcher.showNext()
            volumeEditText.requestFocus()
        }
    }

    fun onStop() = disposable.clear()

    companion object {
        const val TAG = "Volume Panel"
    }


}
