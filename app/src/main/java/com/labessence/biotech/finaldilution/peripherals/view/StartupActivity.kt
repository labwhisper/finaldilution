package com.labessence.biotech.finaldilution.peripherals.view

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
import com.labessence.biotech.finaldilution.solution.Solution
import com.labessence.biotech.finaldilution.solution.view.EditActivity
import java.text.DecimalFormat

class StartupActivity : Activity() {

    internal var volFormat = DecimalFormat("0.##")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "On create Startup Activity")
        setContentView(R.layout.content_startup)
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
            appState.currentSolution = appState.solutionGateway.loadAll()[0]
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
                text1.setText(solution.name)
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
                .setPositiveButton("OK") { dialog, which ->
                    if (solutionNamePicker.text.length != 0) {
                        //TODO Extract those 3 lines into app entities or no?
                        val solution = Solution()
                        solution.name = solutionNamePicker.text.toString()
                        appState.solutionGateway.save(solution)
                        refreshSolutionList()
                        appState.currentSolution =
                                appState.solutionGateway.load(solutionNamePicker.text.toString())
                        val intent = Intent(this@StartupActivity, EditActivity::class.java)
                        startActivity(intent)
                    }
                }
                .setNegativeButton("Cancel", null)
            val alertDialog = alertDialogBuilder.create()

            solutionNamePicker.setOnFocusChangeListener { v1, hasFocus ->
                if (hasFocus) {
                    alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
            }
            alertDialog.show()
        }
    }

    private inner class SolutionChooseListener : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val appState = applicationContext as ApplicationContext
            appState.currentSolution = parent.adapter.getItem(position) as Solution
            val intent = Intent(this@StartupActivity, EditActivity::class.java)
            startActivity(intent)
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
