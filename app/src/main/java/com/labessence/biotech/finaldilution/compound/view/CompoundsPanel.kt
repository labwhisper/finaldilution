package com.labessence.biotech.finaldilution.compound.view

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.component.view.EditComponentActivity
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.genericitem.putExtra
import com.labessence.biotech.finaldilution.peripherals.view.Anim
import com.labessence.biotech.finaldilution.solution.view.EditActivity

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

class CompoundsPanel(private val activity: EditActivity) {
    private val appState: ApplicationContext = activity.applicationContext as ApplicationContext

    private val compoundListAdapter: CompoundListAdapter
        get() = CompoundListAdapter(appState.compoundGateway.loadAll())

    fun displayCompoundList() {
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
        val newCompoundButton = activity.findViewById<Button>(R.id.new_compound_button)
        newCompoundButton.setOnClickListener {
            val newLayout = activity.findViewById<ViewGroup>(R.id.new_compound_layout)
            val listLayout = activity.findViewById<ViewGroup>(R.id.compound_list_layout)
            newLayout.visibility = View.VISIBLE
            listLayout.visibility = View.GONE
//            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.compoundsList, NewCompoundFragment())
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()

        }
    }

    private fun compoundTouchListener(): CompoundListTouchListener.TouchListener {
        return object : CompoundListTouchListener.TouchListener {
            override fun onTouch(view: View, position: Int) {
                val compound = compoundListAdapter.compoundList[position]
                if (activity.solution.getComponentWithCompound(compound) != null) {
                    informAboutCompoundAlreadyAdded(view, compound)
                    return
                }
                startComponentEdition(compound)
            }

            override fun onLongTouch(view: View, position: Int) {
                val compound = compoundListAdapter.compoundList[position]
                appState.saveCurrentWorkOnSolution(activity.solution)
                appState.removeCompoundFromEverywhere(compound)
                activity.solution = appState.reloadSolution(activity.solution)!!
            }

        }
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
            "Compound (" + compound.shortName + ") already in solution (" + activity.solution.name + ")"
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
            //TODO WARN: rv.findChild... doesn't find the proper child
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


