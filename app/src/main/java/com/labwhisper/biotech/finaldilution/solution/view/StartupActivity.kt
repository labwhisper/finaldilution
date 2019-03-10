package com.labwhisper.biotech.finaldilution.solution.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.genericitem.putExtra
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.SolutionCareTaker
import java.text.DecimalFormat

class StartupActivity : Activity() {

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
        solutionListView.setOnItemClickListener { parent, _, position, _ ->
            enterSolution(parent.adapter.getItem(position) as Solution)
        }
        solutionListView.setOnItemLongClickListener { parent, _, position, _ ->
            deleteSolution(parent.adapter.getItem(position) as Solution); true
        }
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
                    val newName = solutionNamePicker.text.toString()
                    if (appState.solutionGateway.load(newName) != null) {
                        return@setPositiveButton
                    }
                    val solution = Solution(newName)
                    appState.solutionGateway.save(solution)
                    refreshSolutionList()
                    enterSolution(appState.solutionGateway.load(newName))
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

    private fun enterSolution(solution: Solution?) {
        solution?.let {
            val intent = Intent(this@StartupActivity, EditActivity::class.java)
            intent.putExtra(it)
            intent.putExtra("CARE_TAKER", SolutionCareTaker())
            startActivity(intent)
        }
    }

    private fun deleteSolution(solution: Solution) {
        (applicationContext as ApplicationContext).solutionGateway.remove(solution)
        refreshSolutionList()
    }

    companion object {
        private val TAG = "Startup Activity"
    }

}
