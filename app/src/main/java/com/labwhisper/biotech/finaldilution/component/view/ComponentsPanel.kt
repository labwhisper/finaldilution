package com.labwhisper.biotech.finaldilution.component.view

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import com.labwhisper.biotech.finaldilution.util.recyclerView
import io.reactivex.disposables.CompositeDisposable


class ComponentsPanel(internal val activity: EditActivity) {


    var componentInContextMenu: Component? = null

    private val disposable = CompositeDisposable()

    fun displayComponentList() {
        val componentListAdapter = ChecklistAdapter()
        componentListAdapter.onClickListener = ::editComponent
        componentListAdapter.onLongClickListener = { componentInContextMenu = it; false }
        componentListAdapter.solutionUpdate = { activity.appModel.updateSolution(it) }
        val componentsListView = activity.recyclerView(R.id.componentsList)
        componentsListView.layoutManager =
            LinearLayoutManager(activity)
        componentsListView.adapter = componentListAdapter
        val divider = DividerItemDecoration(activity, VERTICAL)
        ContextCompat.getDrawable(activity, R.drawable.components_divider)
            ?.let { divider.setDrawable(it) }
        componentsListView.addItemDecoration(divider)
        activity.registerForContextMenu(componentsListView)

        disposable.add(activity.appModel.solution
            .distinctUntilChanged()
            .subscribe {
                onSolutionChange(it, componentListAdapter)
            })
    }

    private fun onSolutionChange(
        solution: Solution,
        componentListAdapter: ChecklistAdapter
    ) {
        componentListAdapter.solution = solution
        componentListAdapter.notifyDataSetChanged()
    }

    fun removeComponentSelectedInContextMenu() {
        componentInContextMenu?.let { component ->
            val updatedSolution = activity.appModel.solution.value?.deepCopy()
            updatedSolution?.removeComponent(component) ?: return
            activity.appModel.updateSolution(updatedSolution)
        }
    }

    private fun editComponent(component: Component) {
        component.compound.let { activity.startComponentEdition(it) }
    }

    fun onStop() = disposable.clear()

    companion object {
        const val TAG = "Components Panel"
    }
}
