package com.druidpyrcel.biotech.finaldilution.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.druidpyrcel.biotech.finaldilution.ApplicationContext;
import com.druidpyrcel.biotech.finaldilution.model.Component;
import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.Concentration;
import com.druidpyrcel.biotech.finaldilution.model.ItemExistsException;


public class CompoundChooseDialog extends AlertDialog {

    protected CompoundChooseDialog(final Context context, final TextView textViewToUpdate, final Compound compound) {
        super(context);

        final EditText amountInput = new EditText(context);

        this.setView(amountInput);
        this.setMessage("Choose desired concentration");
        this.setCancelable(false);
        this.setButton(BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ApplicationContext appState = ((ApplicationContext) context.getApplicationContext());
                if (amountInput.getText().length() != 0) {
                    try {
                        Concentration concentration = new Concentration(Double.parseDouble(amountInput.getText().toString()), Concentration.ConcentrationType.MOLAR);
                        Component component = new Component(compound, concentration);
                        appState.getCurrentSolution().addComponent(component);
                    } catch (ItemExistsException e) {
                        final AlertDialog itemExistsDialog = new AlertDialog.Builder(context).create();
                        itemExistsDialog.setTitle("Information");
                        itemExistsDialog.setMessage("Compound [" + compound.getShortName() + "] already present");
                        itemExistsDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        itemExistsDialog.show();
                    }
                    textViewToUpdate.setText(appState.getCurrentSolution().calculateQuantities());
                }
            }
        });
        this.setButton(BUTTON_NEGATIVE, "Cancel", (DialogInterface.OnClickListener) null);

        amountInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amountInput.setRawInputType(Configuration.KEYBOARD_12KEY);
        amountInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
    }
}
