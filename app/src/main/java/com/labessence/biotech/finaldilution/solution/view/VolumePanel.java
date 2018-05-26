package com.labessence.biotech.finaldilution.solution.view;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.peripherals.gestures.TapGestureController;
import com.labessence.biotech.finaldilution.peripherals.gestures.TapGestureListener;

import java.text.DecimalFormat;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

public class VolumePanel implements TapGestureController {

    private static final String TAG = "Volume panel ";
    private final EditActivity activity;
    private final ApplicationContext appState;
    private ViewSwitcher switcher = null;
    private TextView volumeTextView = null;
    private TextView volumeEditText = null;
    private DecimalFormat volFormat = new DecimalFormat("0.##");

    VolumePanel(EditActivity activity) {
        this.activity = activity;
        appState = ((ApplicationContext) activity.getApplicationContext());
    }

    void displayVolumeText() {
        switcher = (ViewSwitcher) activity.findViewById(R.id.volumeViewSwitcher);
        volumeTextView = (TextView) activity.findViewById(R.id.beakerVolumeTextView);
        volumeEditText = (EditText) activity.findViewById(R.id.beakerVolumeEditText);
        updateVolumeTextView();
        volumeEditText.setText(volFormat.format(appState.getCurrentSolution().getVolume()));
        volumeEditText.setSelectAllOnFocus(true);
        volumeEditText.setOnEditorActionListener(getOnVolumeEditorActionListener());

        //Set keyboard to numerical and to show immediately
        volumeEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
        volumeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (hasFocus && imm != null)
                imm.showSoftInput(v, 0);
        });
        volumeTextView.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (hasFocus && imm != null) {
                imm.hideSoftInputFromWindow(volumeEditText.getWindowToken(), 0);
            }
        });
        switcher.showNext();
    }

    void displayBeakerImage() {
        View beakerImage = activity.findViewById(R.id.beakerRectangle);
        GestureDetector tapDetector = new GestureDetector(activity,
                new TapGestureListener(this));
        beakerImage.setOnTouchListener((view, motionEvent) -> {
            //Log.d(TAG, motionEvent.toString());
            tapDetector.onTouchEvent(motionEvent);
            return true;
        });
    }

    public void startVolumeEdition() {
        if (switcher.getCurrentView().equals(volumeTextView)) {
            switcher.showNext();
            volumeEditText.requestFocus();
        }
    }

    void updateVolumeTextView() {
        volumeTextView.setText(String.format("%sml",
                volFormat.format(appState.getCurrentSolution().getVolume())));
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(volumeEditText.getWindowToken(), 0);
        }
    }

    private void updateVolume(double volume) {
        appState.getCurrentSolution().setVolume(volume);
    }

    @NonNull
    private TextView.OnEditorActionListener getOnVolumeEditorActionListener() {
        return (v, actionId, event) -> {
            //Switch to Edit Text
            if (!switcher.getCurrentView().equals(volumeTextView)) {
                switcher.showNext();
            }
            try {
                CharSequence s = v.getText();
                if (s.length() != 0) {
                    double volume = Double.parseDouble(s.toString().replace(',', '.'));
                    updateVolume(volume);
                    activity.refresh();
                }
            } catch (NumberFormatException e) {
                // TODO NumberFormatException... get back old text
            }
            return true;
        };
    }

    @Override
    public void onTap() {
        startVolumeEdition();
    }
}
