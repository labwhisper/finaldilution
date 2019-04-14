package com.labwhisper.biotech.finaldilution.component.view

import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity


class ComponentsPanel(internal val activity: EditActivity) {

    private val componentListAdapter: ChecklistAdapter =
        ChecklistAdapter(
            activity.solution.components,
            this
        )

    fun displayComponentList() {
        val componentsListView = activity.findViewById<View>(R.id.componentsTextView) as ListView
        componentsListView.setOnItemClickListener(componentClick)
        componentsListView.setOnItemLongClickListener(componentLongClick)
        componentsListView.adapter = componentListAdapter
        updateComponentList()
    }

    private val componentClick: (AdapterView<*>, View, Int, Long) -> Unit =
        onClick@{ parent, _, position, _ ->
            val component = parent.adapter.getItem(position) as? Component ?: return@onClick
            component.compound.let { activity.startComponentEdition(it) }
        }

    private val componentLongClick: (AdapterView<*>, View, Int, Long) -> Boolean =
        { parent, _, position, _ ->
            val component = parent.adapter.getItem(position) as Component
            activity.solution.removeComponent(component)
            activity.refresh()
            true
        }

    fun updateComponentList() {
        componentListAdapter.updateComponents(activity.solution.components)
        updateOverflowState()
    }

    private fun updateOverflowState() {
        componentListAdapter.overflown = activity.solution.isOverflown
    }


}
