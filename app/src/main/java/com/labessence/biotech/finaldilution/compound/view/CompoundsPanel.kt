package com.labessence.biotech.finaldilution.compound.view

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.component.view.CompoundActivity
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

    private val compoundListAdapter: ArrayAdapter<Compound>
        get() = ArrayAdapter(
            activity, android.R.layout.simple_list_item_1, appState.compoundGateway.loadAll()
        )

    private// TODO Refresh activity if needed.
    // activity.refresh();
    val compoundLongClickListener: (AdapterView<*>, View, Int, Long) -> Boolean =
        { parent, view, position, id ->
            val compound = parent.adapter.getItem(position) as Compound
            appState.saveCurrentWorkOnSolution(activity.solution)
            appState.removeCompoundFromEverywhere(compound)
            activity.solution = appState.reloadSolution(activity.solution)!!
            true
        }

    private val compoundClickListener: (AdapterView<*>, View, Int, Long) -> Unit =
        OnClick@{ parent, view, position, id ->
            val compound = parent.adapter.getItem(position) as Compound
            if (activity.solution.getComponentWithCompound(compound) != null) {
                informAboutCompoundAlreadyAdded(view, compound)
                return@OnClick
            }
            startComponentEdition(compound)
        }

    fun displayCompoundList() {
        val compoundsListView = activity.findViewById<ListView>(R.id.compoundsListView)
        compoundsListView.setOnItemClickListener(compoundClickListener)
        compoundsListView.setOnItemLongClickListener(compoundLongClickListener)
        updateCompoundList()
    }

    private fun updateCompoundList() {
        val compoundsListView = activity.findViewById<ListView>(R.id.compoundsListView)
        compoundsListView.adapter = compoundListAdapter
    }

    private fun informAboutCompoundAlreadyAdded(view: View, compound: Compound) {
        Anim().blinkWithRed(view)
        Log.d(
            TAG,
            "Compound (" + compound.shortName + ") already in solution (" + activity.solution.name + ")"
        )
    }

    private fun startComponentEdition(compound: Compound) {
        val intent = Intent(activity, CompoundActivity::class.java)
        intent.putExtra(compound)
        intent.putExtra(activity.solution)
        intent.putExtra("CARE_TAKER", activity.solutionCareTaker)
        activity.startActivity(intent)
    }

    companion object {

        private val TAG = "Compound Panel"
    }

}


