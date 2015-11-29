package com.druidpyrcel.biotech.finaldilution;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.ItemExistsException;

import java.text.DecimalFormat;

public class EditActivity extends AppCompatActivity {

    final Context context = this;
    ViewSwitcher switcher = null;
    TextView volumeTextView = null;
    TextView volumeEditText = null;
    TextView componentsTextView;
    DecimalFormat volFormat = new DecimalFormat("0.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        displayVolumeText();
        displayComponentsList();
        displayCompoundList();
        displayBeakerImage();
        displayFromEditToPrepButton();
        displayTitleToolbar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        appState.getDb().updateSolution(appState.getCurrentSolution());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void displayVolumeText() {
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        switcher = (ViewSwitcher) findViewById(R.id.volumeViewSwitcher);
        volumeTextView = (TextView) findViewById(R.id.beakerVolumeTextView);
        volumeEditText = (EditText) findViewById(R.id.beakerVolumeEditText);
        volumeTextView.setText(getResources().getString(R.string.volumeText) + volFormat.format(appState.getCurrentSolution().getVolumeMili()) + "ml");
        volumeEditText.setText(volFormat.format(appState.getCurrentSolution().getVolumeMili()));
        volumeEditText.setSelectAllOnFocus(true);
        volumeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Switch to Edit Text
                if (switcher.getCurrentView().equals(volumeEditText)) {
                    switcher.showNext();
                }
                try {
                    CharSequence s = v.getText();
                    if (s.length() != 0) {
                        appState.getCurrentSolution().setVolumeMili(Double.parseDouble(s.toString()));
                        componentsTextView.setText(appState.getCurrentSolution().calculateQuantities());
                        volumeTextView.setText(getResources().getString(R.string.volumeText) + volFormat.format(appState.getCurrentSolution().getVolumeMili()) + "ml");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(volumeEditText.getWindowToken(), 0);
                    }
                } catch (NumberFormatException e) {
//                    // NumberFormatException... get back old text
                }
                return true;
            }
        });

        //Set keyboard to numerical and to show immediately
        volumeEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
        volumeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    imm.showSoftInput(v, 0);
                }
            }
        });
        volumeTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    imm.hideSoftInputFromWindow(volumeEditText.getWindowToken(), 0);
                }
            }
        });
        switcher.showNext();
    }

    private void displayComponentsList() {
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        componentsTextView = (TextView) findViewById(R.id.componentsTextView);
        componentsTextView.setText(appState.getCurrentSolution().calculateQuantities());
    }

    private void displayCompoundList() {
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        ListView compoundsListView = (ListView) findViewById(R.id.compoundsListView);
        ArrayAdapter<Compound> compoundListAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, appState.getDb().getAllCompounds());
        compoundsListView.setAdapter(compoundListAdapter);
        compoundsListView.setOnItemClickListener(new CompoundChooseListener());
    }

    private void displayBeakerImage() {
        //TODO : Merge listeners (common code)
        class BeakerClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                //Switch to Edit Text
                if (switcher.getCurrentView().equals(volumeTextView)) {
                    switcher.showNext();
                    volumeEditText.requestFocus();
                }
            }
        }
        View beakerImage = findViewById(R.id.beakerRectangle);
        beakerImage.setOnClickListener(new BeakerClickListener());
    }

    private void displayFromEditToPrepButton() {
        Button fromEditToPrepareButton = (Button) findViewById(R.id.fromEditToPreparebutton);
        fromEditToPrepareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, PrepActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayTitleToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    class CompoundChooseListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
            final EditText amountInput = new EditText(EditActivity.this);
            final ApplicationContext appState = ((ApplicationContext) getApplicationContext());

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(amountInput)
                    .setMessage("Pick amount: ")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (amountInput.getText().length() != 0) {
                                Compound currentCompound = (Compound) (parent.getAdapter().getItem(position));
                                try {
                                    appState.getCurrentSolution().addComponent(currentCompound, Double.parseDouble(amountInput.getText().toString()));
                                } catch (ItemExistsException e) {
                                    final AlertDialog itemExistsDialog = new AlertDialog.Builder(EditActivity.this).create();
                                    itemExistsDialog.setTitle("Information");
                                    itemExistsDialog.setMessage("Compound [" + currentCompound.getShortName() + "] already present");
                                    itemExistsDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    itemExistsDialog.show();
                                }
                                componentsTextView.setText(appState.getCurrentSolution().calculateQuantities());
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            //Set keyboard to numerical and to show immediately
            amountInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            amountInput.setRawInputType(Configuration.KEYBOARD_12KEY);
            amountInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            });
            alertDialog.show();
        }
    }


}
