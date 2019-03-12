package com.labwhisper.biotech.finaldilution.solution.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.EditText
import com.labwhisper.biotech.finaldilution.genericitem.putExtraAnItem
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.SolutionCareTaker
import com.labwhisper.biotech.finaldilution.solution.appmodel.StartupAppModel

class SolutionNameDialog(val context: Context, val appModel: StartupAppModel) {

    fun create(): AlertDialog {
        val solutionNamePicker = EditText(context)

        val dialogText = "Enter new solution name: "

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder
            .setView(solutionNamePicker)
            .setMessage(dialogText)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> onAcceptNew(solutionNamePicker.text.toString()) }
            .setNegativeButton("Cancel", null)
        val alertDialog = alertDialogBuilder.create()

        solutionNamePicker.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
        return alertDialog
    }

    private fun onAcceptNew(newName: String) {
        appModel.addNewSolution(newName)
        // TODO observer should refreshSolutionList()
        appModel.loadSolution(newName)?.let { enterSolution(it) }
    }

    private fun enterSolution(solution: Solution) {
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtraAnItem(solution)
        intent.putExtra("CARE_TAKER", SolutionCareTaker())
        context.startActivity(intent)
    }

}