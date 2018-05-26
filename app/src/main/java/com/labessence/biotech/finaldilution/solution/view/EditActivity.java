package com.labessence.biotech.finaldilution.solution.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.view.ComponentsPanel;
import com.labessence.biotech.finaldilution.component.view.CompoundActivity;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.peripherals.gestures.CompoundListGestureListener;
import com.labessence.biotech.finaldilution.peripherals.gestures.EditGestureListener;

public class EditActivity extends Activity {

    private static final String TAG = "Edit Activity";
    private VolumePanel volumePanel;
    private ComponentsPanel componentsPanel;
    private GestureDetector screenGestureDetector;
    private GestureDetector compoundListGestureDetector;

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
        View addCompoundButton = findViewById(R.id.addCompoundButton);
        compoundListGestureDetector = new GestureDetector(this, new CompoundListGestureListener(this, addCompoundButton));
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
        View addCompoundButton = findViewById(R.id.addCompoundButton);
        addCompoundButton.setOnTouchListener((view, motionEvent) -> {
            compoundListGestureDetector.onTouchEvent(motionEvent);
            return true;
        });
    }

    public void startComponentEdition(Compound compound) {
        Intent intent = new Intent(this, CompoundActivity.class);
        intent.putExtra("compound", compound);
        startActivity(intent);
    }

}
