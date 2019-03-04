package com.labwhisper.biotech.finaldilution.solution.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.view.ComponentsPanel
import com.labwhisper.biotech.finaldilution.component.view.EditComponentActivity
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.view.CompoundsPanel
import com.labwhisper.biotech.finaldilution.genericitem.putExtra
import com.labwhisper.biotech.finaldilution.peripherals.gestures.EditGestureListener
import com.labwhisper.biotech.finaldilution.solution.RedoOnLastChangeException
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.SolutionCareTaker
import com.labwhisper.biotech.finaldilution.solution.UndoOnEmptyListException
import com.labwhisper.biotech.finaldilution.util.editText


class EditActivity : AppCompatActivity() {
    private var volumePanel: VolumePanel? = null
    private var componentsPanel: ComponentsPanel? = null
    private var compoundsPanel: CompoundsPanel? = null
    private var screenGestureDetector: GestureDetector? = null
    lateinit var solution: Solution
    lateinit var solutionCareTaker: SolutionCareTaker

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solution = intent.getSerializableExtra("SOLUTION") as Solution
        solutionCareTaker = intent.getSerializableExtra("CARE_TAKER") as SolutionCareTaker
        setContentView(R.layout.solution_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        enableSolutionRenaming()

        volumePanel = VolumePanel(this)
        volumePanel?.displayVolumeText()
        volumePanel?.displayBeakerImage()

        componentsPanel = ComponentsPanel(this)
        componentsPanel?.displayComponentList()

        displayAddCompoundFragment()
        screenGestureDetector = GestureDetector(this, EditGestureListener(this))
    }

    override fun onBackPressed() {
        when {
            compoundsPanel?.isSearchOpen() == true -> compoundsPanel?.exitSearch()
            compoundsPanel?.isExpanded() == true -> compoundsPanel?.collapse()
            else -> super.onBackPressed()
        }
    }

    private fun enableSolutionRenaming() {
        val title = findViewById<EditText>(R.id.solution_toolbar_text)
        title.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            if (p1 == true) return@OnFocusChangeListener
            val text = (p0 as EditText).text
            if (text.isNullOrBlank()) return@OnFocusChangeListener
            if (text.toString() == solution.name) return@OnFocusChangeListener
            val appState = applicationContext as ApplicationContext
            val oldName = solution.name
            solution.name = text.toString()
            appState.renameSolution(solution, oldName)
            refresh()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        refreshMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return super.onOptionsItemSelected(null)
        }
        val appState = applicationContext as ApplicationContext
        when (item.itemId) {
            R.id.action_save -> {
                appState.saveCurrentWorkOnSolution(solution)
                return true
            }
            R.id.action_undo -> {
                return try {
                    val newSolution = solutionCareTaker.undo()
                    if (newSolution.name != solution.name) {
                        appState.renameSolution(newSolution, solution.name)
                    }
                    solution = newSolution
                    refresh()
                    true
                } catch (e: UndoOnEmptyListException) {
                    false
                }
            }
            R.id.action_redo -> {
                return try {
                    val newSolution = solutionCareTaker.redo()
                    if (newSolution.name != solution.name) {
                        appState.renameSolution(newSolution, solution.name)
                    }
                    solution = newSolution
                    refresh()
                    true
                } catch (e: RedoOnLastChangeException) {
                    false
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
        val title = findViewById<EditText>(R.id.solution_toolbar_text)
        title.setText(solution.name)
    }

    fun refresh() {
        componentsPanel?.updateComponentList()
        volumePanel?.updateVolumeTextView()
        solutionCareTaker.addMemento(solution)
        val title = findViewById<EditText>(R.id.solution_toolbar_text)
        title.setText(solution.name)
        refreshMenu()
    }

    private fun refreshMenu() {
        menu?.findItem(R.id.action_undo)?.isEnabled = solutionCareTaker.canUndo
        menu?.findItem(R.id.action_redo)?.isEnabled = solutionCareTaker.canRedo
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun displayAddCompoundFragment() {
        compoundsPanel = CompoundsPanel(this)
        compoundsPanel?.displayCompoundList()
    }

    fun startComponentEdition(compound: Compound) {
        val intent = Intent(this, EditComponentActivity::class.java)
        intent.putExtra(compound)
        intent.putExtra(solution)
        intent.putExtra("CARE_TAKER", solutionCareTaker)
        startActivity(intent)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus as? EditText ?: return super.dispatchTouchEvent(event)
            //Work around - to not exit search edit text before choosing item
            if (v == editText(R.id.search_compound_button)) return super.dispatchTouchEvent(event)
            val outRect = Rect()
            v.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                v.clearFocus()
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    companion object {
        private val TAG = "Edit Activity"
    }

}
