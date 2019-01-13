package com.labessence.biotech.finaldilution.solution.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
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
import com.labessence.biotech.finaldilution.solution.RedoOnLastChangeException
import com.labessence.biotech.finaldilution.solution.Solution
import com.labessence.biotech.finaldilution.solution.SolutionCareTaker
import com.labessence.biotech.finaldilution.solution.UndoOnEmptyListException

class EditActivity : AppCompatActivity() {
    private var volumePanel: VolumePanel? = null
    private var componentsPanel: ComponentsPanel? = null
    private var screenGestureDetector: GestureDetector? = null
    lateinit var solution: Solution
    lateinit var solutionCareTaker: SolutionCareTaker

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solution = intent.getSerializableExtra("SOLUTION") as Solution
        solutionCareTaker = intent.getSerializableExtra("CARE_TAKER") as SolutionCareTaker
        setContentView(R.layout.content_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        volumePanel = VolumePanel(this)
        volumePanel?.displayVolumeText()
        volumePanel?.displayBeakerImage()

        componentsPanel = ComponentsPanel(this)
        componentsPanel?.displayComponentList()

        displayAddCompoundFragment()
        screenGestureDetector = GestureDetector(this, EditGestureListener(this))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        refreshMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return super.onOptionsItemSelected(item)
        }
        val appState = applicationContext as ApplicationContext
        when (item.itemId) {
            R.id.action_save -> {
                appState.saveCurrentWorkOnSolution(solution)
                return true
            }
            R.id.action_undo -> {
                try {
                    solution = solutionCareTaker.undo()
                    refresh()
                    return true
                } catch (e: UndoOnEmptyListException) {
                    return false
                }
            }
            R.id.action_redo -> {
                try {
                    solution = solutionCareTaker.redo()
                    refresh()
                    return true
                } catch (e: RedoOnLastChangeException) {
                    return false
                }
            }
        }
        return false
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
        solutionCareTaker.addMemento(solution)
        findViewById<TextView>(R.id.solution_toolbar_text).text = solution.name
    }

    fun refresh() {
        componentsPanel?.updateComponentList()
        volumePanel?.updateVolumeTextView()
        solutionCareTaker.addMemento(solution)
        refreshMenu()
    }

    private fun refreshMenu() {
        menu?.findItem(R.id.action_undo)?.isEnabled = solutionCareTaker.canUndo
        menu?.findItem(R.id.action_redo)?.isEnabled = solutionCareTaker.canRedo
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
        intent.putExtra("CARE_TAKER", solutionCareTaker)
        startActivity(intent)
    }

    companion object {

        private val TAG = "Edit Activity"
    }

}
