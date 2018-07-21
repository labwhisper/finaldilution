package com.labessence.biotech.finaldilution.compound.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.view.WindowManager
import android.widget.EditText

import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.solution.view.EditActivity

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

internal class NewCompoundCreator(private val activity: EditActivity) {
    private val appState: ApplicationContext
    //
    //    void displayNewCompoundButton() {
    //        Button newCompoundButton = (Button) activity.findViewById(R.id.newCompoundButton);
    //        newCompoundButton.setOnClickListener(getNewCompoundClickListener());
    //    }

    private val newCompoundClickListener: (View) -> Unit = { v ->
        val compoundNamePicker = EditText(activity)

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setView(compoundNamePicker)
            .setMessage("Enter new compound parameters: ")
            .setCancelable(false)
            .setPositiveButton("OK", getOnAcceptNewCompoundListener(compoundNamePicker))
            .setNegativeButton("Cancel", null)
        val alertDialog = alertDialogBuilder.create()

        compoundNamePicker.setOnFocusChangeListener { v1, hasFocus ->
            if (hasFocus) {
                alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
        alertDialog.show()
    }

    init {
        appState = activity.applicationContext as ApplicationContext
    }

    private fun getOnAcceptNewCompoundListener(compoundNamePicker: EditText): (DialogInterface, Int) -> Unit =
        label@{ dialog, which ->
            if (compoundNamePicker.text.length == 0) {
                return@label
            }
            val compound = Compound(compoundNamePicker.text.toString())
            //TODO Add nice dialog with molar mass
            compound.molarMass = 40.0
            //TODO Save or not save here...
            appState.compoundGateway.save(compound)
            activity.refresh()
        }
}
