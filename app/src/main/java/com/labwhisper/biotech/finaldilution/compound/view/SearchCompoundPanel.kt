package com.labwhisper.biotech.finaldilution.compound.view

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.compound.appmodel.CompoundsPanelAppModel
import com.labwhisper.biotech.finaldilution.util.editText
import com.labwhisper.biotech.finaldilution.util.imageButton

class SearchCompoundPanel(private val activity: Activity) {

    var searchOpen: Boolean = false

    fun initSearchFunctionality(compoundsPanelAppModel: CompoundsPanelAppModel) {
        val searchEditText = activity.editText(R.id.search_compound_button)
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
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
                compoundsPanelAppModel.filterCompoundList(text)
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
        searchEditText.setPadding(
            activity.resources.getDimensionPixelSize(R.dimen.search_button_offset_open), 0, 0, 0
        )
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, 0)
        searchOpen = true
    }

    fun exitSearch() {
        val searchEditText = activity.editText(R.id.search_compound_button)
        val exitSearchButton = activity.imageButton(R.id.exit_search_button)
        searchEditText.setText("")
        searchEditText.setPadding(
            activity.resources.getDimensionPixelSize(R.dimen.search_button_offset_closed), 0, 0, 0
        )
        searchEditText.layoutParams.width =
            activity.resources.getDimension(R.dimen.button_size).toInt()
        searchEditText.clearFocus()
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        exitSearchButton.visibility = View.INVISIBLE
        searchOpen = false
    }

}