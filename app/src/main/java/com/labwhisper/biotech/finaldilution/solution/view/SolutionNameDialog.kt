package com.labwhisper.biotech.finaldilution.solution.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.EditText
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.genericitem.putExtraAnItem
import com.labwhisper.biotech.finaldilution.solution.CareTaker
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.appmodel.StartupAppModel

class SolutionNameDialog(val context: Context, val appModel: StartupAppModel) {

    fun forCreate(): AlertDialog {
        return create(
            dialogTitle = context.getString(R.string.enter_new_solution_name),
            positiveButtonStringId = R.string.action_create,
            onAccept = ::onAcceptNew
        )
    }

    fun forClone(solution: Solution): AlertDialog {
        val oldName = solution.name
        return create(
            dialogTitle = context.getString(R.string.clone_solution, oldName),
            startText = oldName,
            positiveButtonStringId = R.string.action_clone
        ) { newName ->
            onAcceptClone(solution, newName)
        }
    }

    fun forRename(solution: Solution): AlertDialog {
        val oldName = solution.name
        return create(
            dialogTitle = context.getString(R.string.rename_solution, oldName),
            startText = oldName,
            positiveButtonStringId = R.string.action_rename
        ) { newName ->
            onAcceptRename(solution.apply { name = newName }, oldName)
        }
    }

    fun create(
        dialogTitle: String,
        startText: String = "",
        positiveButtonStringId: Int,
        onAccept: (String) -> Unit
    )
            : AlertDialog {
        val solutionNamePicker = EditText(context)
        solutionNamePicker.setText(startText)
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder
            .setView(solutionNamePicker)
            .setMessage(dialogTitle)
            .setCancelable(false)
            .setNegativeButton(context.getString(R.string.action_cancel), null)
            .setPositiveButton(context.getString(positiveButtonStringId)) { _, _ ->
                onAccept.invoke(solutionNamePicker.text.toString())
            }
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
        appModel.loadSolution(newName)?.let { enterSolution(it) }
    }

    private fun onAcceptClone(oldSolution: Solution, newName: String) {
        val newSolution = oldSolution.deepCopy().apply { name = newName }
        appModel.addNewSolutionFilled(newSolution)
    }

    private fun onAcceptRename(solution: Solution, oldName: String) {
        appModel.renameSolution(solution, oldName)
    }

    private fun enterSolution(solution: Solution) {
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtraAnItem(solution)
        intent.putExtra("CARE_TAKER", CareTaker<Solution>())
        context.startActivity(intent)
    }

}