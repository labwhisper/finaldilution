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
import com.labwhisper.biotech.finaldilution.component.view.EditCompoundFragmentCreator
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.appmodel.CompoundsPanelAppModel
import com.labwhisper.biotech.finaldilution.compound.view.CompoundsPanel
import com.labwhisper.biotech.finaldilution.compound.view.NewCompoundFragment
import com.labwhisper.biotech.finaldilution.genericitem.putSerializableAnItem
import com.labwhisper.biotech.finaldilution.solution.CareTaker
import com.labwhisper.biotech.finaldilution.solution.RedoOnLastChangeException
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.UndoOnEmptyListException
import com.labwhisper.biotech.finaldilution.solution.appmodel.EditSolutionAppModel
import com.labwhisper.biotech.finaldilution.util.editText
import com.labwhisper.biotech.finaldilution.util.view
import io.reactivex.disposables.CompositeDisposable


class EditActivity : AppCompatActivity() {
    private var volumePanel: VolumePanel? = null
    private var componentsPanel: ComponentsPanel? = null
    private var compoundsPanel: CompoundsPanel? = null
    private var screenGestureDetector: GestureDetector? = null
    private lateinit var careTaker: CareTaker<Solution>

    val appModel by lazy {
        EditSolutionAppModel(
            (applicationContext as ApplicationContext).compoundGateway,
            (applicationContext as ApplicationContext).solutionGateway
        )
    }

    private var menu: Menu? = null

    private val disposable = CompositeDisposable()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appModel.solution.onNext(intent.getSerializableExtra("SOLUTION") as Solution)
        careTaker = intent.getSerializableExtra("CARE_TAKER") as CareTaker<Solution>
        setContentView(R.layout.solution_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        view(R.id.back).setOnClickListener { onBackPressed() }
        disposable.add(appModel.solution.subscribe {
            editText(R.id.solution_toolbar_text)?.setText(it.name)
            careTaker.addMemento(it)
            refreshMenu()
        })
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
            appModel.solution.value?.let {
                val oldName = it.name
                if (text.toString() == oldName) return@OnFocusChangeListener
//                appModel.solution.onNext(it.apply { name = text.toString() })
                appModel.renameSolution(it.apply { name = text.toString() }, oldName)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        refreshMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_undo -> {
                return try {
                    val newSolution = careTaker.undo()
                    appModel.solution.value?.let {
                        if (newSolution.name != it.name) {
                            appModel.renameSolution(newSolution, it.name)
                        }
                        appModel.updateSolution(newSolution)
                        true
                    } ?: false
                } catch (e: UndoOnEmptyListException) {
                    false
                }
            }
            R.id.action_redo -> {
                return try {
                    val newSolution = careTaker.redo()
                    appModel.solution.value?.let {
                        if (newSolution.name != it.name) {
                            appModel.renameSolution(newSolution, it.name)
                        }
                        appModel.updateSolution(newSolution)
                        true
                    } ?: false
                } catch (e: RedoOnLastChangeException) {
                    false
                }
            }
        }
        return false
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        when (v?.id) {
            R.id.componentsList -> {
                menuInflater.inflate(R.menu.menu_component, menu)
            }
            R.id.compoundsListView -> {
                menuInflater.inflate(R.menu.menu_compound, menu)
            }
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove_component -> {
                componentsPanel?.removeComponentSelectedInContextMenu()
                true
            }
            R.id.action_delete_compound -> {
                compoundsPanel?.deleteCompoundSelectedInContextMenu()
                true
            }
            R.id.action_edit_compound -> {
                compoundsPanel?.editCompoundSelectedInContextMenu()
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
        appModel.solution.value?.deepCopy()?.let { appModel.updateSolution(it) }
    }

    override fun onResume() {
        super.onResume()
        appModel.solution.value?.let { careTaker.addMemento(it) }
    }

    override fun onStop() {
        volumePanel?.onStop()
        componentsPanel?.onStop()
        compoundsPanel?.onStop()
        super.onStop()
        disposable.clear()
    }

    private fun refreshMenu() {
        menu?.findItem(R.id.action_undo)?.isEnabled = careTaker.canUndo
        menu?.findItem(R.id.action_redo)?.isEnabled = careTaker.canRedo
    }

    private fun displayAddCompoundFragment() {
        compoundsPanel = CompoundsPanel(
            activity = this,
            appModel = CompoundsPanelAppModel(
                appModel,
                appModel.compoundList,
                appModel.solution,
                careTaker
            ),
            editCompoundFragmentCreator = EditCompoundFragmentCreator()
        )
        compoundsPanel?.displayCompoundList()
    }

    fun startComponentEdition(compound: Compound) {
        val bundle = Bundle()
        bundle.putSerializableAnItem(compound)
        bundle.putSerializableAnItem(appModel.solution.value ?: return)
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

    companion object {
        const val TAG = "Edit Activity"
    }

}
