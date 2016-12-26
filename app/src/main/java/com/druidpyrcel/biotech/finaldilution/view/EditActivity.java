package com.druidpyrcel.biotech.finaldilution.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
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

import com.druidpyrcel.biotech.finaldilution.ApplicationContext;
import com.druidpyrcel.biotech.finaldilution.R;
import com.druidpyrcel.biotech.finaldilution.model.Component;
import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.Solution;

import java.text.DecimalFormat;

import static com.druidpyrcel.biotech.finaldilution.ApplicationContext.SWIPE_MIN_DISTANCE;
import static com.druidpyrcel.biotech.finaldilution.ApplicationContext.SWIPE_MIN_VELOCITY;

public class EditActivity extends Activity {

    private static final String TAG = "Edit Activity";
    ViewSwitcher switcher = null;
    TextView volumeTextView = null;
    TextView volumeEditText = null;
    DecimalFormat volFormat = new DecimalFormat("0.##");
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit);

        displayVolumeText();
        displayComponentsList();
        displayCompoundList();
        displayBeakerImage();
        displayFromEditToPrepButton();
        displayNewCompoundButton();

        detector = new GestureDetector(this, new EditGestureListener());
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
        //TODO NPE IN THE LINE BELOW SOMEWHERE
        try {
            volumeTextView.setText(getResources().getString(R.string.volumeText) + volFormat.format(appState.getCurrentSolution().getVolume()) + "ml");
        } catch (NullPointerException e) {
            Log.d(TAG, "volumeTextView=" + volumeTextView, e);
            Log.d(TAG, " volFormat=" + volFormat, e);
            Log.d(TAG, " appState=" + appState, e);
            Log.d(TAG, " appState.getCurrentSolution()=" + appState.getCurrentSolution(), e);
            Log.d(TAG, " appState.getCurrentSolution().getVolume()=" + appState.getCurrentSolution().getVolume(), e);
            throw new NullPointerException();
        }
        if (volumeEditText == null) {
            return;
        }
        volumeEditText.setText(volFormat.format(appState.getCurrentSolution().getVolume()));
        volumeEditText.setSelectAllOnFocus(true);
        volumeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Switch to Edit Text
                if (!switcher.getCurrentView().equals(volumeTextView)) {
                    switcher.showNext();
                }
                try {
                    CharSequence s = v.getText();
                    if (s.length() != 0) {
                        appState.getCurrentSolution().setVolume(Double.parseDouble(s.toString().replace(',', '.')));
                        appState.getCurrentSolution().resetComponents();
                        appState.getDb().updateSolution(appState.getCurrentSolution());
                        displayComponentsList();
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
        ListView componentsListView = (ListView) findViewById(R.id.componentsTextView);
        for (Component component : appState.getCurrentSolution().getComponents()) {
            if (component.getCompound() == null) {
                appState.getDb().removeComponent(component);
                appState.getCurrentSolution().resetComponents();
            }
        }

        ArrayAdapter<Component> componentListAdapter = new ArrayAdapter<>(
                this, R.layout.tiny_list, appState.getCurrentSolution().getComponents());
        componentsListView.setAdapter(componentListAdapter);
        componentsListView.setOnItemClickListener(new ComponentListItemListener());
        componentsListView.setOnItemLongClickListener(new ComponentLongClickListener());

        if (appState.getCurrentSolution().isOverflown()) {
            componentsListView.setBackgroundColor(Color.WHITE);
            for (Component component : appState.getCurrentSolution().getComponents()) {
                if (component.getFromStock()) {
                    componentsListView.setBackgroundColor(Color.YELLOW);
                    break;
                }
            }
        }
    }

    private void displayCompoundList() {
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        ListView compoundsListView = (ListView) findViewById(R.id.compoundsListView);
        ArrayAdapter<Compound> compoundListAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, appState.getDb().getAllCompounds());
        compoundsListView.setAdapter(compoundListAdapter);
        compoundsListView.setOnItemClickListener(new CompoundChooseListener());
        compoundsListView.setOnItemLongClickListener(new CompoundLongClickListener());
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

    private void displayNewCompoundButton() {
        Button newCompoundButton = (Button) findViewById(R.id.newCompoundButton);
        newCompoundButton.setOnClickListener(new OnNewCompoundButtonClickListener());
    }

    class ComponentListItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Component component = (Component) (parent.getAdapter().getItem(position));
            if (component == null || component.getCompound() == null) {
                return;
            }
            Intent intent = new Intent(EditActivity.this, CompoundActivity.class);
            intent.putExtra("compound", component.getCompound());
            startActivity(intent);
        }
    }

    class ComponentLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ApplicationContext appState = ((ApplicationContext) getApplicationContext());
            final Component component = (Component) (parent.getAdapter().getItem(position));
            if (component == null || component.getCompound() == null) {
                return false;
            }
            appState.getDb().removeComponent(component);
            appState.getDb().updateSolution(appState.getCurrentSolution());
            appState.getCurrentSolution().resetComponents();
            displayComponentsList();
            return true;
        }
    }

    class CompoundChooseListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
            final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
            Compound compound = (Compound) (parent.getAdapter().getItem(position));

            if (appState.getDb().getComponentWithCompound(appState.getCurrentSolution(), compound) != null) {
                //TODO Change animation
                //TODO Check on all android versions simulators
                View currentView = view;
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
//                    currentView = parent.getChildAt(parent.getChildCount() - position - 1);
//                }
                new Anim().blinkWithRed(currentView);
                Log.d(TAG, "Compound (" + compound.getShortName() + ") already in solution (" + appState.getCurrentSolution().getName() + ")");
            } else {
                Intent intent = new Intent(EditActivity.this, CompoundActivity.class);
                intent.putExtra("compound", compound);
                startActivity(intent);
            }
        }
    }

    class CompoundLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ApplicationContext appState = ((ApplicationContext) getApplicationContext());
            final Compound compound = (Compound) (parent.getAdapter().getItem(position));
            for (Solution solution : appState.getDb().getAllSolutions()) {
                if (appState.getDb().getComponentWithCompound(solution, compound) != null) {
                    appState.getDb().removeComponent(appState.getDb().getComponentWithCompound(solution, compound));
                    appState.getDb().updateSolution(solution);
                    appState.getCurrentSolution().resetComponents();
                }
            }
            appState.getDb().removeCompound(compound);
            displayComponentsList();
            displayCompoundList();
            return true;
        }
    }

    private class OnNewCompoundButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            final EditText compoundNamePicker = new EditText(EditActivity.this);
            final ApplicationContext appState = ((ApplicationContext) getApplicationContext());

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditActivity.this);
            alertDialogBuilder.setView(compoundNamePicker)
                    .setMessage("Enter new compound parameters: ")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (compoundNamePicker.getText().length() != 0) {
                                Compound compound = new Compound();
                                compound.setShortName(compoundNamePicker.getText().toString());
                                //TODO Add nice dialog with molar mass
                                compound.setMolarMass(40.0);
                                appState.getDb().addCompound(compound);
                                //TODO refresh compound list properly
                                EditActivity.this.displayCompoundList();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            compoundNamePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
            }

            return true;
        }
    }

}
