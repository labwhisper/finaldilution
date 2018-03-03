package com.labessence.biotech.finaldilution.solution.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.view.ComponentsPanel;
import com.labessence.biotech.finaldilution.component.view.CompoundActivity;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.peripherals.view.EditGestureListener;

public class EditActivity extends Activity {

    private VolumePanel volumePanel;
    //private CompoundsPanel compoundsPanel;
    private ComponentsPanel componentsPanel;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit);

        volumePanel = new VolumePanel(this);
        volumePanel.displayVolumeText();
        volumePanel.displayBeakerImage();

//        compoundsPanel = new CompoundsPanel(this);
//        compoundsPanel.displayCompoundList();
//        compoundsPanel.displayNewCompoundButton();

        componentsPanel = new ComponentsPanel(this);
        componentsPanel.displayComponentList();
        displayToPrepActivityButton();

        detector = new GestureDetector(this, new EditGestureListener(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        appState.saveCurrentWorkOnSolution();
    }

    public void refresh() {
        componentsPanel.updateComponentList();
        //compoundsPanel.updateCompoundList();
        volumePanel.updateVolumeTextView();
    }

    private void displayToPrepActivityButton() {
        Button fromEditToPrepareButton = (Button) findViewById(R.id.fromEditToPreparebutton);
        fromEditToPrepareButton.setOnClickListener(v -> {
            Intent intent = new Intent(EditActivity.this, PrepActivity.class);
            startActivity(intent);
        });
    }

    public void startComponentEdition(Compound compound) {
        Intent intent = new Intent(this, CompoundActivity.class);
        intent.putExtra("compound", compound);
        startActivity(intent);
    }

}
