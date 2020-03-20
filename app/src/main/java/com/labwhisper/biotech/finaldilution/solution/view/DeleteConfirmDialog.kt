package com.labwhisper.biotech.finaldilution.solution.view

import android.app.AlertDialog
import android.content.Context
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.appmodel.StartupAppModel

class DeleteConfirmDialog(val context: Context, val appModel: StartupAppModel) {

    fun create(solution: Solution)
            : AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder
            .setMessage(context.getString(R.string.delete_solution, solution.name))
            .setCancelable(true)
            .setNegativeButton(context.getString(R.string.action_cancel), null)
            .setPositiveButton(context.getString(R.string.action_delete)) { _, _ ->
                onAcceptDelete(
                    solution
                )
            }
        return alertDialogBuilder.create()
    }

    private fun onAcceptDelete(solution: Solution) {
        appModel.deleteSolution(solution)
    }

}