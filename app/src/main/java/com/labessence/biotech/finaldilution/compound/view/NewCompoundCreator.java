package com.labessence.biotech.finaldilution.compound.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.solution.view.EditActivity;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

class NewCompoundCreator {
    private final EditActivity activity;
    private final ApplicationContext appState;

    NewCompoundCreator(EditActivity activity) {
        this.activity = activity;
        appState = ((ApplicationContext) activity.getApplicationContext());
    }
//
//    void displayNewCompoundButton() {
//        Button newCompoundButton = (Button) activity.findViewById(R.id.newCompoundButton);
//        newCompoundButton.setOnClickListener(getNewCompoundClickListener());
//    }

    @NonNull
    private View.OnClickListener getNewCompoundClickListener() {
        return v -> {
            final EditText compoundNamePicker = new EditText(activity);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setView(compoundNamePicker)
                    .setMessage("Enter new compound parameters: ")
                    .setCancelable(false)
                    .setPositiveButton("OK", getOnAcceptNewCompoundListener(compoundNamePicker))
                    .setNegativeButton("Cancel", null);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            compoundNamePicker.setOnFocusChangeListener((v1, hasFocus) -> {
                if (hasFocus && alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            });
            alertDialog.show();
        };
    }

    @NonNull
    private DialogInterface.OnClickListener getOnAcceptNewCompoundListener(EditText compoundNamePicker) {
        return (dialog, which) -> {
            if (compoundNamePicker.getText().length() == 0) {
                return;
            }
            Compound compound = new Compound(compoundNamePicker.getText().toString());
            //TODO Add nice dialog with molar mass
            compound.setMolarMass(40.0);
            //TODO Save or not save here...
            appState.getCompoundGateway().save(compound);
            activity.refresh();
        };
    }
}
