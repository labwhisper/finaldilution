package com.labessence.biotech.finaldilution.solution.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.component.view.ComponentsPanel
import com.labessence.biotech.finaldilution.component.view.CompoundActivity
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.compound.view.CompoundsPanel
import com.labessence.biotech.finaldilution.genericitem.putExtra
import com.labessence.biotech.finaldilution.peripherals.gestures.EditGestureListener
import com.labessence.biotech.finaldilution.solution.Solution

class EditActivity : AppCompatActivity() {
    private var volumePanel: VolumePanel? = null
    private var componentsPanel: ComponentsPanel? = null
    private var screenGestureDetector: GestureDetector? = null
    lateinit var solution: Solution

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solution = intent.getSerializableExtra("SOLUTION") as Solution
        setContentView(R.layout.content_edit)

        volumePanel = VolumePanel(this)
        volumePanel?.displayVolumeText()
        volumePanel?.displayBeakerImage()

        componentsPanel = ComponentsPanel(this)
        componentsPanel?.displayComponentList()

        displayAddCompoundFragment()
        screenGestureDetector = GestureDetector(this, EditGestureListener(this))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        screenGestureDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onPause() {
        super.onPause()
        val appState = applicationContext as ApplicationContext
        appState.saveCurrentWorkOnSolution(solution)
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.solution_toolbar_text).text = solution.name
    }

    fun refresh() {
        componentsPanel?.updateComponentList()
        volumePanel?.updateVolumeTextView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun displayAddCompoundFragment() {
        val compoundsPanel = CompoundsPanel(this)
        compoundsPanel.displayCompoundList()
    }

    fun startComponentEdition(compound: Compound) {
        val intent = Intent(this, CompoundActivity::class.java)
        intent.putExtra(compound)
        intent.putExtra(solution)
        startActivity(intent)
    }

    companion object {

        private val TAG = "Edit Activity"
    }

}
