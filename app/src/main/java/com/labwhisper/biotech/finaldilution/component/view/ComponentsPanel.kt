package com.labwhisper.biotech.finaldilution.component.view

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import com.labwhisper.biotech.finaldilution.util.recyclerView


class ComponentsPanel(internal val activity: EditActivity) {

    private val componentListAdapter: ChecklistAdapter =
        ChecklistAdapter(activity.solution)

    fun displayComponentList() {
        componentListAdapter.onClickListener = ::editComponent
        componentListAdapter.onLongClickListener = ::removeComponent
        val componentsListView = activity.recyclerView(R.id.componentsList)
        componentsListView.layoutManager = LinearLayoutManager(activity)
        componentsListView.adapter = componentListAdapter
        val divider = DividerItemDecoration(activity, VERTICAL)
        ContextCompat.getDrawable(activity, R.drawable.components_divider)
            ?.let { divider.setDrawable(it) }
        componentsListView.addItemDecoration(divider)
        updateSolution()
    }

    private fun removeComponent(component: Component) {
        activity.solution.removeComponent(component)
        activity.refresh()
    }

    private fun editComponent(component: Component) {
        component.compound.let { activity.startComponentEdition(it) }
    }

    fun updateSolution() {
        componentListAdapter.updateComponents(activity.solution)
        updateOverflowState()
    }

    private fun updateOverflowState() {
        componentListAdapter.overflown = activity.solution.isOverflown
    }


}
