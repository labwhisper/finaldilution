package com.labwhisper.biotech.finaldilution.solution.view

import android.app.AlertDialog
import android.content.Context
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.appmodel.StartupAppModel

class DeleteConfirmDialog(val context: Context, val appModel: StartupAppModel) {

    fun create(solution: Solution)
            : AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder
            .setMessage("Delete solution: ${solution.name}?")
            .setCancelable(true)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ -> onAcceptDelete(solution) }
        return alertDialogBuilder.create()
    }

    private fun onAcceptDelete(solution: Solution) {
        appModel.deleteSolution(solution)
    }

}