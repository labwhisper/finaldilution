package com.labessence.biotech.finaldilution.solution.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.component.view.ComponentsPanel
import com.labessence.biotech.finaldilution.component.view.CompoundActivity
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.compound.view.CompoundsPanel
import com.labessence.biotech.finaldilution.peripherals.gestures.EditGestureListener

class EditActivity : AppCompatActivity() {
    private var volumePanel: VolumePanel? = null
    private var componentsPanel: ComponentsPanel? = null
    private var screenGestureDetector: GestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_edit)

        volumePanel = VolumePanel(this)
        volumePanel!!.displayVolumeText()
        volumePanel!!.displayBeakerImage()

        componentsPanel = ComponentsPanel(this)
        componentsPanel!!.displayComponentList()

        displayAddCompoundFragment()
        screenGestureDetector = GestureDetector(this, EditGestureListener(this))
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        screenGestureDetector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onPause() {
        super.onPause()
        val appState = applicationContext as ApplicationContext
        appState.saveCurrentWorkOnSolution()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    fun refresh() {
        componentsPanel!!.updateComponentList()
        volumePanel!!.updateVolumeTextView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun displayAddCompoundFragment() {
        val compoundsPanel = CompoundsPanel(this)
        compoundsPanel.displayCompoundList()
    }

    fun startComponentEdition(compound: Compound) {
        val intent = Intent(this, CompoundActivity::class.java)
        intent.putExtra("compound", compound)
        startActivity(intent)
    }

    companion object {

        private val TAG = "Edit Activity"
    }

}
