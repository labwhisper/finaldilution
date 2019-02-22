package com.labessence.biotech.finaldilution.init.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.genericitem.putExtra
import com.labessence.biotech.finaldilution.solution.Solution
import com.labessence.biotech.finaldilution.solution.SolutionCareTaker
import com.labessence.biotech.finaldilution.solution.view.EditActivity
import java.text.DecimalFormat

class StartupActivity : Activity() {

    private var solution: Solution? = null

    internal var volFormat = DecimalFormat("0.##")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "On create Startup Activity")
        setContentView(R.layout.startup)
        val newSolutionButton = findViewById<View>(R.id.addNewSolutionButton) as Button
        newSolutionButton.setOnClickListener(OnNewSolutionButtonClickListener())
    }

    override fun onResume() {
        super.onResume()
        refreshSolutionList()
    }

    private fun refreshSolutionList() {

        //TODO Extract setting current solution from file elswhere
        val appState = applicationContext as ApplicationContext
        val solutionList = appState.solutionGateway.loadAll()
        if (!solutionList.isEmpty()) {
            //TODO Save and set last solution
            solution = solutionList[0]
        }
        val solutionListView = findViewById<View>(R.id.solutionListView) as ListView
        val solutionListAdapter = object : ArrayAdapter<Solution>(
            this, R.layout.solution_list_item, R.id.solution_list_text1, solutionList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val text1 = view.findViewById<View>(R.id.solution_list_text1) as TextView
                val text2 = view.findViewById<View>(R.id.solution_list_text2) as TextView
                val solution = solutionList[position]
                text1.text = solution.name
                text2.text = String.format(
                    getString(R.string.solutionListPrepFormat),
                    volFormat.format(solution.volume),
                    solution.components.size
                )
                return view
            }
        }
        solutionListView.adapter = solutionListAdapter
        solutionListView.onItemClickListener = SolutionChooseListener()
        solutionListView.onItemLongClickListener = SolutionLongClickListener()
    }

    private inner class OnNewSolutionButtonClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            val solutionNamePicker = EditText(this@StartupActivity)
            val appState = applicationContext as ApplicationContext

            val alertDialogBuilder = AlertDialog.Builder(this@StartupActivity)
            alertDialogBuilder.setView(solutionNamePicker)
                .setMessage("Enter new solution name: ")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ ->
                    if (!solutionNamePicker.text.isNotEmpty()) {
                        return@setPositiveButton
                    }
                    //TODO Extract those 3 lines into app entities or no?
                    val newName = solutionNamePicker.text.toString()
                    if (appState.solutionGateway.load(newName) != null) {
                        return@setPositiveButton
                    }
                    val solution = Solution(newName)
                    appState.solutionGateway.save(solution)
                    refreshSolutionList()
                    this@StartupActivity.solution =
                            appState.solutionGateway.load(newName)
                    this@StartupActivity.solution?.let {
                        val intent = Intent(this@StartupActivity, EditActivity::class.java)
                        intent.putExtra(it)
                        intent.putExtra("CARE_TAKER", SolutionCareTaker())
                        startActivity(intent)
                    }
                }
                .setNegativeButton("Cancel", null)
            val alertDialog = alertDialogBuilder.create()

            solutionNamePicker.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
            }
            alertDialog.show()
        }
    }

    private inner class SolutionChooseListener : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            solution = parent.adapter.getItem(position) as Solution
            solution?.let {
                val intent = Intent(this@StartupActivity, EditActivity::class.java)
                intent.putExtra(it)
                intent.putExtra("CARE_TAKER", SolutionCareTaker())
                startActivity(intent)
            }
        }
    }

    internal inner class SolutionLongClickListener : AdapterView.OnItemLongClickListener {

        override fun onItemLongClick(
            parent: AdapterView<*>,
            view: View,
            position: Int,
            id: Long
        ): Boolean {
            val appState = applicationContext as ApplicationContext
            //TODO Extract deleting solution into view logic layer. // app entity
            val solution = parent.adapter.getItem(position) as Solution
            appState.solutionGateway.remove(solution)
            refreshSolutionList()
            return true
        }
    }

    companion object {
        private val TAG = "Startup Activity"
    }

}
