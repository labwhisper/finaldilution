package com.labwhisper.biotech.finaldilution.solution.view

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.view.ComponentsPanel
import com.labwhisper.biotech.finaldilution.component.view.EditComponentFragment
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.view.CompoundsPanel
import com.labwhisper.biotech.finaldilution.compound.view.NewCompoundFragment
import com.labwhisper.biotech.finaldilution.genericitem.putSerializableAnItem
import com.labwhisper.biotech.finaldilution.solution.CareTaker
import com.labwhisper.biotech.finaldilution.solution.RedoOnLastChangeException
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.UndoOnEmptyListException
import com.labwhisper.biotech.finaldilution.util.editText
import com.labwhisper.biotech.finaldilution.util.view


class EditActivity : AppCompatActivity() {
    private var volumePanel: VolumePanel? = null
    private var componentsPanel: ComponentsPanel? = null
    private var compoundsPanel: CompoundsPanel? = null
    private var screenGestureDetector: GestureDetector? = null
    lateinit var solution: Solution
    lateinit var careTaker: CareTaker<Solution>

    private var menu: Menu? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solution = intent.getSerializableExtra("SOLUTION") as Solution
        careTaker = intent.getSerializableExtra("CARE_TAKER") as CareTaker<Solution>
        setContentView(R.layout.solution_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        view(R.id.back).setOnClickListener { onBackPressed() }
        enableSolutionRenaming()

        volumePanel = VolumePanel(this)
        volumePanel?.displayVolumeText()
        volumePanel?.displayBeakerImage()

        componentsPanel = ComponentsPanel(this)
        componentsPanel?.displayComponentList()

        displayAddCompoundFragment()
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.fragments.filterIsInstance<EditComponentFragment>()
                .isNotEmpty() -> super.onBackPressed()
            compoundsPanel?.isSearchOpen() == true -> compoundsPanel?.exitSearch()
            supportFragmentManager.fragments.filterIsInstance<NewCompoundFragment>()
                .isNotEmpty() -> super.onBackPressed()
            compoundsPanel?.isExpanded() == true -> compoundsPanel?.collapse()
            else -> super.onBackPressed()
        }
    }

    private fun enableSolutionRenaming() {
        val title = findViewById<EditText>(R.id.solution_toolbar_text)
        title.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) return@OnFocusChangeListener
            val text = (view as EditText).text
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
                    val newSolution = careTaker.undo()
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
                    val newSolution = careTaker.redo()
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

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) = menuInflater.inflate(R.menu.menu_component, menu)


    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return super.onContextItemSelected(null)
        }
        return when (item.itemId) {
            R.id.action_delete_component -> {
                componentsPanel?.removeComponentSelectedInContextMenu()
                true
            }
            else -> false
        }
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
        careTaker.addMemento(solution)
        val title = findViewById<EditText>(R.id.solution_toolbar_text)
        title.setText(solution.name)
    }

    fun refresh() {
        componentsPanel?.updateSolution()
        volumePanel?.updateVolumeTextView()
        careTaker.addMemento(solution)
        val title = findViewById<EditText>(R.id.solution_toolbar_text)
        title.setText(solution.name)
        refreshMenu()
    }

    private fun refreshMenu() {
        menu?.findItem(R.id.action_undo)?.isEnabled = careTaker.canUndo
        menu?.findItem(R.id.action_redo)?.isEnabled = careTaker.canRedo
    }

    private fun displayAddCompoundFragment() {
        compoundsPanel = CompoundsPanel(this)
        compoundsPanel?.displayCompoundList()
    }

    fun startComponentEdition(compound: Compound) {
        val bundle = Bundle()
        bundle.putSerializableAnItem(compound)
        bundle.putSerializableAnItem(solution)
        bundle.putSerializable("CARE_TAKER", careTaker)
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = EditComponentFragment().apply { arguments = bundle }
        transaction.replace(android.R.id.content, fragment, EditComponentFragment.TAG)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus as? EditText ?: return super.dispatchTouchEvent(event)
            //Work around - to not exit search edit text before choosing item
            when (v) {
                editText(R.id.search_compound_button) -> return super.dispatchTouchEvent(event)
            }
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

}
