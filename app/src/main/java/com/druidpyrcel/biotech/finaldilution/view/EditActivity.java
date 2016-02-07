package com.druidpyrcel.biotech.finaldilution.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.druidpyrcel.biotech.finaldilution.ApplicationContext;
import com.druidpyrcel.biotech.finaldilution.R;
import com.druidpyrcel.biotech.finaldilution.model.Compound;

import java.text.DecimalFormat;

import static com.druidpyrcel.biotech.finaldilution.ApplicationContext.SWIPE_MIN_DISTANCE;
import static com.druidpyrcel.biotech.finaldilution.ApplicationContext.SWIPE_MIN_VELOCITY;

public class EditActivity extends AppCompatActivity {

    ViewSwitcher switcher = null;
    TextView volumeTextView = null;
    TextView volumeEditText = null;
    TextView componentsTextView;
    DecimalFormat volFormat = new DecimalFormat("0.##");
    private GestureDetectorCompat detector;

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

        detector = new GestureDetectorCompat(this, new EditGestureListener());
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void displayVolumeText() {
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        switcher = (ViewSwitcher) findViewById(R.id.volumeViewSwitcher);
        volumeTextView = (TextView) findViewById(R.id.beakerVolumeTextView);
        volumeEditText = (EditText) findViewById(R.id.beakerVolumeEditText);
        volumeTextView.setText(getResources().getString(R.string.volumeText) + volFormat.format(appState.getCurrentSolution().getVolume()) + "ml");
        volumeEditText.setText(volFormat.format(appState.getCurrentSolution().getVolume()));
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
                        appState.getCurrentSolution().setVolume(Double.parseDouble(s.toString()));
                        componentsTextView.setText(appState.getCurrentSolution().calculateQuantities());
                        volumeTextView.setText(getResources().getString(R.string.volumeText) + volFormat.format(appState.getCurrentSolution().getVolume()) + "ml");
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
            Intent intent = new Intent(EditActivity.this, CompoundActivity.class);
            intent.putExtra("compound", (Compound) (parent.getAdapter().getItem(position)));
            startActivity(intent);
        }
    }

    class EditGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float ev1X = e1.getX();
            float ev2X = e2.getX();
            final float xdistance = Math.abs(ev1X - ev2X);
            final float xvelocity = Math.abs(velocityX);

            if ((xvelocity < SWIPE_MIN_VELOCITY) || (xdistance < SWIPE_MIN_DISTANCE)) {
                return false;
            }
            if (ev1X > ev2X) {
                Intent intent = new Intent(EditActivity.this, PrepActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(EditActivity.this, StartupActivity.class);
                startActivity(intent);
            }

            return true;
        }
    }

}
