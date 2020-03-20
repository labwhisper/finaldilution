package com.labwhisper.biotech.finaldilution.compound.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.view.EditComponentFragment
import com.labwhisper.biotech.finaldilution.component.view.EditCompoundFragmentCreator
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.appmodel.CompoundsPanelAppModel
import com.labwhisper.biotech.finaldilution.genericitem.putSerializableAnItem
import com.labwhisper.biotech.finaldilution.peripherals.view.Anim
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import com.labwhisper.biotech.finaldilution.util.imageButton
import com.labwhisper.biotech.finaldilution.util.recyclerView
import io.reactivex.disposables.CompositeDisposable


class CompoundsPanel(
    private val activity: EditActivity,
    var appModel: CompoundsPanelAppModel,
    val editCompoundFragmentCreator: EditCompoundFragmentCreator
) {

    private val adapter = CompoundListAdapter()
    private val searchCompoundPanel = SearchCompoundPanel(activity)
    private val disposable = CompositeDisposable()

    var compoundInContextMenu: Compound? = null

    fun displayCompoundList() {

        disposable.add(appModel.filteredCompoundList.subscribe {
            adapter.compoundList = it
            adapter.notifyDataSetChanged()
        })

        disposable.add(appModel.solution.subscribe {
            adapter.compoundsInSolution = it.components.map { it.compound }.toMutableList()
        })

        adapter.onClickListener = ::onCompoundClick
        adapter.onLongClickListener = { compoundInContextMenu = it; false }
        val compoundsListView = activity.recyclerView(R.id.compoundsListView)
        val layoutManager = LinearLayoutManager(activity)
        compoundsListView.layoutManager = layoutManager
        compoundsListView.itemAnimator = DefaultItemAnimator()
        compoundsListView.adapter = adapter
        activity.imageButton(R.id.new_compound_button).setOnClickListener {
            editCompoundFragmentCreator.startCompoundEdition(
                activity, onClose =
                ::handleCompoundEditionClose
            )
        }
        searchCompoundPanel.initSearchFunctionality(appModel)
        activity.findViewById<AddComponentButtonView>(R.id.compoundsListHeader).setOnClickListener {
            if (!isExpanded()) expand() else collapse()
        }
        activity.registerForContextMenu(compoundsListView)
    }

    fun isExpanded(): Boolean {
        val compoundsListView = activity.findViewById<ConstraintLayout>(R.id.compounds_fragment)
        val state = BottomSheetBehavior.from(compoundsListView).state
        return state != BottomSheetBehavior.STATE_COLLAPSED
    }

    fun expand() {
        val compoundsListView = activity.findViewById<ConstraintLayout>(R.id.compounds_fragment)
        BottomSheetBehavior.from(compoundsListView).state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun collapse() {
        val compoundsListView = activity.findViewById<ConstraintLayout>(R.id.compounds_fragment)
        BottomSheetBehavior.from(compoundsListView).state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun isSearchOpen() = searchCompoundPanel.searchOpen

    fun exitSearch() = searchCompoundPanel.exitSearch()

    private fun onCompoundClick(view: View, compound: Compound) {
        if (appModel.solution.value?.getComponentWithCompound(compound) != null) {
            informAboutCompoundAlreadyAdded(view, compound)
            return
        }
        startComponentEdition(compound)
    }


    private fun handleCompoundEditionClose(
        newCompound: Compound?
    ) {
        activity.imageButton(R.id.new_compound_button).visibility = View.VISIBLE
        val compoundsListView = activity.findViewById<RecyclerView>(R.id.compoundsListView)
        val newCompoundPosition = adapter.compoundList.indexOf(newCompound)
        val offset =
            2 * activity.resources.getDimension(R.dimen.compound_list_item_height).toInt()
        (compoundsListView.layoutManager as LinearLayoutManager?)?.scrollToPositionWithOffset(
            newCompoundPosition,
            offset
        )
    }

    fun deleteCompoundSelectedInContextMenu() {
        compoundInContextMenu?.let { deleteCompound(it) }
    }

    fun editCompoundSelectedInContextMenu() {
        compoundInContextMenu?.let {
            editCompoundFragmentCreator.startCompoundEdition(
                activity,
                it,
                ::handleCompoundEditionClose
            )
        }
    }

    private fun deleteCompound(compound: Compound) {
        appModel.deleteCompound(compound)
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
            "Compound (${compound.trivialName}) already in solution (${appModel.solution.value?.name
                ?: ""})"
        )
    }

    private fun startComponentEdition(compound: Compound) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val editComponentFragment = EditComponentFragment()
        val bundle = Bundle()
        bundle.putSerializableAnItem(compound)
        appModel.solution.value?.let { bundle.putSerializableAnItem(it) }
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

    fun onStop() = disposable.clear()

    companion object {
        const val TAG = "Compound Panel"
    }

}


