package com.labwhisper.biotech.finaldilution.compound.view

import android.app.AlertDialog
import android.content.Context
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.solution.appmodel.EditSolutionAppModel

class DeleteCompoundConfirmDialog(val context: Context, val appModel: EditSolutionAppModel) {


    fun create(compound: Compound)
            : AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val deleteCompoundString = context.getString(
            R.string.delete_compound, compound.displayName
        )
        val affectedSolutions = appModel.findSolutionsWithCompound(compound)
        val message = if (affectedSolutions.isEmpty()) {
            deleteCompoundString
        } else {
            val affectedSolutionsString = context.getString(
                R.string.delete_compound_affected_solutions, affectedSolutions.map { it.name }
            )
            "$deleteCompoundString\n$affectedSolutionsString"
        }
        alertDialogBuilder
            .setMessage(message)
            .setCancelable(true)
            .setNegativeButton(context.getString(R.string.action_cancel), null)
            .setPositiveButton(context.getString(R.string.action_delete)) { _, _ ->
                onAcceptDelete(compound)
            }
        return alertDialogBuilder.create()
    }

    private fun onAcceptDelete(compound: Compound) {
        appModel.removeCompoundFromEverywhere(compound)
    }
}