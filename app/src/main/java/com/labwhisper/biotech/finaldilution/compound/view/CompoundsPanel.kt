package com.labwhisper.biotech.finaldilution.compound.view

import android.content.Context
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
import com.labwhisper.biotech.finaldilution.component.view.EditComponentFragment
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.appmodel.CompoundsPanelAppModel
import com.labwhisper.biotech.finaldilution.genericitem.putSerializableAnItem
import com.labwhisper.biotech.finaldilution.peripherals.view.Anim
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity


class CompoundsPanel(private val activity: EditActivity) {

    private lateinit var appModel: CompoundsPanelAppModel

    private val searchCompoundPanel = SearchCompoundPanel(activity)

    fun displayCompoundList() {
        appModel = CompoundsPanelAppModel(
            activity.applicationContext as ApplicationContext,
            CompoundListAdapter(),
            activity.solution,
            activity.solutionCareTaker
        )
        appModel.compoundListAdapter.compoundList = appModel.compoundList
        val compoundsListView = activity.findViewById<RecyclerView>(R.id.compoundsListView)
        val layoutManager = LinearLayoutManager(activity)
        compoundsListView.layoutManager = layoutManager
        compoundsListView.itemAnimator = DefaultItemAnimator()
        compoundsListView.adapter = appModel.compoundListAdapter
        compoundsListView.addOnItemTouchListener(
            CompoundListTouchListener(
                activity, compoundsListView, compoundTouchListener()
            )
        )
        activity.findViewById<ImageButton>(R.id.new_compound_button).setOnClickListener {
            startCompoundEdition()
        }
        searchCompoundPanel.initSearchFunctionality(appModel)
    }

    fun isExpanded(): Boolean {
        val compoundsListView = activity.findViewById<ConstraintLayout>(R.id.compounds_fragment)
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
        val compoundsListView = activity.findViewById<ConstraintLayout>(R.id.compounds_fragment)
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
                if (position > appModel.compoundListAdapter.compoundList.size - 1) {
                    return
                }
                val compound = appModel.compoundListAdapter.compoundList[position]
                if (activity.solution.getComponentWithCompound(compound) != null) {
                    informAboutCompoundAlreadyAdded(view, compound)
                    return
                }
                startComponentEdition(compound)
            }

            override fun onLongTouch(view: View, position: Int) {
                val compound = appModel.compoundListAdapter.compoundList[position]
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
        newCompoundFragment.setOnFragmentCloseListener { newCompound ->
            activity.findViewById<ImageButton>(R.id.new_compound_button).visibility = View.VISIBLE
            appModel.compoundListAdapter.compoundList = appModel.compoundList
            appModel.compoundListAdapter.notifyDataSetChanged()
            val compoundsListView = activity.findViewById<RecyclerView>(R.id.compoundsListView)
            val newCompoundPosition = appModel.compoundListAdapter.compoundList.indexOf(newCompound)
            val offset =
                2 * activity.resources.getDimension(R.dimen.compound_list_item_height).toInt()
            (compoundsListView.layoutManager as LinearLayoutManager?)?.scrollToPositionWithOffset(
                newCompoundPosition,
                offset
            )

        }
        fragmentTransaction.replace(R.id.compounds_fragment, newCompoundFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        activity.findViewById<ImageButton>(R.id.new_compound_button).visibility = View.GONE
    }

    private fun deleteCompound(position: Int) {
        val compound = appModel.compoundListAdapter.compoundList[position]
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
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val editComponentFragment = EditComponentFragment()
        val bundle = Bundle()
        bundle.putSerializableAnItem(compound)
        bundle.putSerializableAnItem(appModel.solution)
        bundle.putSerializable("CARE_TAKER", appModel.careTaker)
        editComponentFragment.arguments = bundle
        fragmentTransaction.replace(
            android.R.id.content,
            editComponentFragment,
            EditComponentFragment.TAG
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {

        const val TAG = "Compound Panel"
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


