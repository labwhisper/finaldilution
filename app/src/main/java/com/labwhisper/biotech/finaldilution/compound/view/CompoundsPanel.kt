package com.labwhisper.biotech.finaldilution.compound.view

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.view.EditComponentActivity
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.genericitem.putExtra
import com.labwhisper.biotech.finaldilution.peripherals.view.Anim
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

class CompoundsPanel(private val activity: EditActivity) {

    private val compoundListAdapter = CompoundListAdapter()
    private val searchCompoundPanel = SearchCompoundPanel(activity)

    fun displayCompoundList() {
        val appState: ApplicationContext = activity.applicationContext as ApplicationContext
        compoundListAdapter.compoundList = appState.loadAllCompoundsSorted().toMutableList()
        val compoundsListView = activity.findViewById<RecyclerView>(R.id.compoundsListView)
        val layoutManager = LinearLayoutManager(activity)
        compoundsListView.layoutManager = layoutManager
        compoundsListView.itemAnimator = DefaultItemAnimator()
        compoundsListView.adapter = compoundListAdapter
        compoundsListView.addOnItemTouchListener(
            CompoundListTouchListener(
                activity, compoundsListView, compoundTouchListener()
            )
        )
        activity.findViewById<ImageButton>(R.id.new_compound_button).setOnClickListener {
            startCompoundEdition()
        }
        searchCompoundPanel.initSearchFunctionality(compoundListAdapter)
    }

    fun isExpanded(): Boolean {
        val compoundsListView = activity.findViewById<ConstraintLayout>(R.id.compoundsList)
        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)
        val locationOnScreen = IntArray(2)
        compoundsListView.getLocationOnScreen(locationOnScreen)
        val currentHeight = screenSize.y - locationOnScreen[1] - 1
        val expandedHeight =
            activity.resources.getDimension(R.dimen.bottom_sheet_expanded_height).toInt()
        return currentHeight == expandedHeight
    }

    fun collapse() {
        val compoundsListView = activity.findViewById<ConstraintLayout>(R.id.compoundsList)
        val collapsedHeight =
            activity.resources.getDimension(R.dimen.bottom_sheet_collapsed_height).toInt()
        val expandedHeight =
            activity.resources.getDimension(R.dimen.bottom_sheet_expanded_height).toInt()
        ViewCompat.offsetTopAndBottom(
            compoundsListView,
            expandedHeight - collapsedHeight
        )
    }

    fun isSearchOpen() = searchCompoundPanel.searchOpen

    fun exitSearch() = searchCompoundPanel.exitSearch()

    private fun compoundTouchListener(): CompoundListTouchListener.TouchListener {
        return object : CompoundListTouchListener.TouchListener {
            override fun onTouch(view: View, position: Int) {
                if (position > compoundListAdapter.compoundList.size - 1) {
                    return
                }
                val compound = compoundListAdapter.compoundList[position]
                if (activity.solution.getComponentWithCompound(compound) != null) {
                    informAboutCompoundAlreadyAdded(view, compound)
                    return
                }
                startComponentEdition(compound)
            }

            override fun onLongTouch(view: View, position: Int) {
                val compound = compoundListAdapter.compoundList[position]
                startCompoundEdition(compound)
            }

        }
    }

    private fun startCompoundEdition(compound: Compound? = null) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val newCompoundFragment = NewCompoundFragment()
        compound?.let {
            val bundle = Bundle()
            bundle.putSerializable("COMPOUND", compound)
            newCompoundFragment.arguments = bundle
        }
        newCompoundFragment.setOnFragmentCloseListener {
            activity.findViewById<ImageButton>(R.id.new_compound_button).visibility = View.VISIBLE
            val appState: ApplicationContext = activity.applicationContext as ApplicationContext
            compoundListAdapter.compoundList = appState.loadAllCompoundsSorted().toMutableList()
            compoundListAdapter.notifyDataSetChanged()
        }
        fragmentTransaction.replace(R.id.compoundsList, newCompoundFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        activity.findViewById<ImageButton>(R.id.new_compound_button).visibility = View.GONE
    }

    private fun deleteCompound(position: Int) {
        val compound = compoundListAdapter.compoundList[position]
        val appState: ApplicationContext = activity.applicationContext as ApplicationContext
        appState.saveCurrentWorkOnSolution(activity.solution)
        appState.removeCompoundFromEverywhere(compound)
        activity.solution = appState.reloadSolution(activity.solution)!!
    }

    private fun informAboutCompoundAlreadyAdded(view: View, compound: Compound) {
        val layout = view as ViewGroup
        (0 until layout.childCount).forEach { i ->
            val child = layout.getChildAt(i)
            if (child is TextView) {
                Anim().blinkWithRed(child)
            }
        }
        Log.d(
            TAG,
            "Compound (" + compound.trivialName + ") already in solution (" + activity.solution.name + ")"
        )
    }

    private fun startComponentEdition(compound: Compound) {
        val intent = Intent(activity, EditComponentActivity::class.java)
        intent.putExtra(compound)
        intent.putExtra(activity.solution)
        intent.putExtra("CARE_TAKER", activity.solutionCareTaker)
        activity.startActivity(intent)
    }

    companion object {

        private const val TAG = "Compound Panel"
    }

    class CompoundListTouchListener(
        val context: Context,
        val recyclerView: RecyclerView,
        val touchListener: TouchListener
    ) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent?): Boolean {
                        return true
                    }

                    override fun onLongPress(e: MotionEvent?) {
                        if (e == null) return
                        val compound = recyclerView.findChildViewUnder(e.x, e.y) ?: return
                        touchListener.onLongTouch(
                            compound,
                            recyclerView.getChildAdapterPosition(compound)
                        )
                    }
                })
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) = Unit

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val compound = rv.findChildViewUnder(e.x, e.y)
            if (compound != null && gestureDetector.onTouchEvent(e)) {
                touchListener.onTouch(compound, rv.getChildAdapterPosition(compound))
            }
            return false
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit

        interface TouchListener {
            fun onTouch(view: View, position: Int)
            fun onLongTouch(view: View, position: Int)
        }

    }

}

