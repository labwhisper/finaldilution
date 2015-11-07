package com.druidpyrcel.biotech.finaldilution;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.druidpyrcel.biotech.finaldilution.sqlite.MySQLiteHelper;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    Solution currentSolution = null;
    ViewSwitcher switcher = null;
    TextView volumeTextView = null;
    TextView volumeEditText = null;
    TextView compoundsTextView;
    DecimalFormat volFormat = new DecimalFormat("0.##");
    MySQLiteHelper db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MySQLiteHelper(this);

        List<Compound> allCompounds = db.getAllCompounds();

        Solution tempSolution = new Solution("Roztwor1", 145);
        Solution previousSolution = db.getSolution("Roztwor1");
        if (previousSolution == null) {
            db.addSolution(tempSolution);
        }
        currentSolution = db.getSolution("Roztwor1");
        compoundsTextView = (TextView) findViewById(R.id.compoundsTextView);
        compoundsTextView.setText(currentSolution.calculateQuantities());
        switcher = (ViewSwitcher) findViewById(R.id.volumeViewSwitcher);
        volumeTextView = (TextView) findViewById(R.id.beakerVolumeTextView);
        volumeEditText = (EditText) findViewById(R.id.beakerVolumeEditText);
        volumeTextView.setText(getResources().getString(R.string.volumeText) + volFormat.format(currentSolution.getVolumeMili()) + "ml");
        volumeEditText.setText(volFormat.format(currentSolution.getVolumeMili()));
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
                        currentSolution.setVolumeMili(Double.parseDouble(s.toString()));
                        compoundsTextView.setText(currentSolution.calculateQuantities());
                        volumeTextView.setText(getResources().getString(R.string.volumeText) + volFormat.format(currentSolution.getVolumeMili()) + "ml");
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

        ListView compoundsListView = (ListView) findViewById(R.id.compoundsListView);
        ArrayAdapter<Compound> compoundListAdapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1, allCompounds);
        compoundsListView.setAdapter(compoundListAdapter);
        compoundsListView.setOnItemClickListener(new CompoundChooseListener());

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        View beakerImage = findViewById(R.id.beakerRectangle);
        beakerImage.setOnClickListener(new BeakerClickListener());

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.updateSolution(currentSolution);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentSolution = db.getSolution("Roztwor1");
    }

    //
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    class CompoundChooseListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
            final EditText amountInput = new EditText(MainActivity.this);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(amountInput)
                    .setMessage("Pick amount: ")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (amountInput.getText().length() != 0) {
                                currentSolution.addComponent((Compound) parent.getAdapter().getItem(position), Double.parseDouble(amountInput.getText().toString()));
                                compoundsTextView.setText(currentSolution.calculateQuantities());
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
}
