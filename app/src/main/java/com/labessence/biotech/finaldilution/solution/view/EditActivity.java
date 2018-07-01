package com.labessence.biotech.finaldilution.solution.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.view.ComponentsPanel;
import com.labessence.biotech.finaldilution.component.view.CompoundActivity;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.compound.view.CompoundsPanel;
import com.labessence.biotech.finaldilution.peripherals.gestures.EditGestureListener;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "Edit Activity";
    private VolumePanel volumePanel;
    private ComponentsPanel componentsPanel;
    private GestureDetector screenGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit);

        volumePanel = new VolumePanel(this);
        volumePanel.displayVolumeText();
        volumePanel.displayBeakerImage();

        componentsPanel = new ComponentsPanel(this);
        componentsPanel.displayComponentList();

        displayAddCompoundFragment();
        screenGestureDetector = new GestureDetector(this, new EditGestureListener(this));
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        screenGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        appState.saveCurrentWorkOnSolution();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        componentsPanel.updateComponentList();
        volumePanel.updateVolumeTextView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void displayAddCompoundFragment() {
        CompoundsPanel compoundsPanel = new CompoundsPanel(this);
        compoundsPanel.displayCompoundList();
    }

    public void startComponentEdition(Compound compound) {
        Intent intent = new Intent(this, CompoundActivity.class);
        intent.putExtra("compound", compound);
        startActivity(intent);
    }

}
