package com.labessence.biotech.finaldilution.compound.view

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.compound.CompoundSearch
import com.labessence.biotech.finaldilution.util.editText
import com.labessence.biotech.finaldilution.util.imageButton

class SearchCompoundPanel(private val activity: Activity) {

    fun initSearchFunctionality(compoundListAdapter: CompoundListAdapter) {
        val searchEditText = activity.editText(R.id.search_compound_button)
        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                enterSearch()
            }
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterCompoundList(compoundListAdapter, text)
            }

        })
        val exitSearchButton = activity.imageButton(R.id.exit_search_button)
        exitSearchButton.setOnClickListener { exitSearch() }
    }

    private fun enterSearch() {
        val searchEditText = activity.editText(R.id.search_compound_button)
        val exitSearchButton = activity.imageButton(R.id.exit_search_button)
        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)
        searchEditText.layoutParams.width =
            (screenSize.x - 2 * activity.resources.getDimension(R.dimen.grid_margin_side)).toInt()
        exitSearchButton.visibility = View.VISIBLE
        searchEditText.setPadding(120, 0, 50, 0)
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, 0)
    }

    private fun exitSearch() {
        val searchEditText = activity.editText(R.id.search_compound_button)
        val exitSearchButton = activity.imageButton(R.id.exit_search_button)
        searchEditText.setText("")
        searchEditText.setPadding(50, 0, 50, 0)
        searchEditText.layoutParams.width =
            activity.resources.getDimension(R.dimen.button_size).toInt()
        searchEditText.clearFocus()
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        exitSearchButton.visibility = View.INVISIBLE
    }

    private fun filterCompoundList(compoundListAdapter: CompoundListAdapter, text: CharSequence?) {
        val appState: ApplicationContext = activity.applicationContext as ApplicationContext
        compoundListAdapter.compoundList =
            CompoundSearch.searchForCompound(appState.loadAllCompoundsSorted(), text.toString())
                .toMutableList()
        compoundListAdapter.notifyDataSetChanged()
    }
}