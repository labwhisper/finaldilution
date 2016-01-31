package com.druidpyrcel.biotech.finaldilution.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.druidpyrcel.biotech.finaldilution.ApplicationContext;
import com.druidpyrcel.biotech.finaldilution.R;
import com.druidpyrcel.biotech.finaldilution.model.Component;
import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.Concentration;
import com.druidpyrcel.biotech.finaldilution.model.ItemExistsException;

import java.util.ArrayList;
import java.util.List;

public class CompoundActivity extends AppCompatActivity {

    Compound compound;
    Concentration.ConcentrationType desiredConcType;
    Concentration.ConcentrationType stockConcType;
    private List<View> desiredViewsList;
    private List<View> stockViewsList;

    public CompoundActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compound);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stockViewsList = new ArrayList<>();
        stockViewsList.add(findViewById(R.id.stockConcEditText));
        stockViewsList.add(findViewById(R.id.stockConcButtonsBar));
        stockViewsList.add(findViewById(R.id.stockConcTextView));
        stockViewsList.add(findViewById(R.id.stockPercentageConcButton));
        stockViewsList.add(findViewById(R.id.stockMolarConcButton));
        stockViewsList.add(findViewById(R.id.stockMilimolarConcButton));
        stockViewsList.add(findViewById(R.id.stockMgMlConcButton));

        desiredViewsList = new ArrayList<>();
        desiredViewsList.add(findViewById(R.id.desiredConcEditText));
        desiredViewsList.add(findViewById(R.id.desiredConcButtonsBar));
        desiredViewsList.add(findViewById(R.id.desiredConcTextView));
        desiredViewsList.add(findViewById(R.id.desiredPercentageConcButton));
        desiredViewsList.add(findViewById(R.id.desiredMolarConcButton));
        desiredViewsList.add(findViewById(R.id.desiredMilimolarConcButton));
        desiredViewsList.add(findViewById(R.id.desiredMgMlConcButton));
        compound = (Compound) getIntent().getSerializableExtra("compound");

        findViewById(R.id.desiredConcButtonsBar).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                renderButtonsSquare();
            }
        });

        setKeyboardOnInputs();


        findViewById(R.id.desiredPercentageConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = Concentration.ConcentrationType.PERCENTAGE;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("%");
            }
        });

        findViewById(R.id.desiredMolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = Concentration.ConcentrationType.MOLAR;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("M/l");
            }
        });

        findViewById(R.id.desiredMilimolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = Concentration.ConcentrationType.MILIMOLAR;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("mM/l");
            }
        });

        findViewById(R.id.desiredMgMlConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = Concentration.ConcentrationType.MILIGRAM_PER_MILLILITER;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("mg/ml");
            }
        });

        findViewById(R.id.enableStockDilutionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSolutionFromStock();
            }
        });

        findViewById(R.id.buttonAddCompoundDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAcceptComponent();
            }
        });

        findViewById(R.id.buttonAddCompoundCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnCancelComponent();
            }
        });
    }

    private void OnCancelComponent() {
        Intent intent = new Intent(CompoundActivity.this, EditActivity.class);
        startActivity(intent);
    }

    //TODO run this listener also on back and generaly close?
    private void onAcceptComponent() {
        //TODO Add all checks!
        EditText desiredConcEditText = (EditText) findViewById(R.id.desiredConcEditText);
        if (desiredConcEditText.getText().toString().trim().length() == 0) {
            (new Anim()).blink(desiredConcEditText);
            return;
        }
        Concentration concentration = new Concentration(Double.parseDouble(desiredConcEditText.getText().toString()), desiredConcType);
        Component component = new Component(compound, concentration);

        try {
            ApplicationContext appState = ((ApplicationContext) getApplicationContext());
            appState.getCurrentSolution().addComponent(component);

            Intent intent = new Intent(CompoundActivity.this, EditActivity.class);
            startActivity(intent);
        } catch (ItemExistsException e) {
            //TODO WHEN ITEM EXISTS SHOW STH? OR BEFORE EVEN OPENING COMP.ACTIVITY?
            e.printStackTrace();

        }
    }

    private void setKeyboardOnInputs() {
        final EditText desiredConcEditText = (EditText) findViewById(R.id.desiredConcEditText);
        desiredConcEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        desiredConcEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
        desiredConcEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // hide virtual keyboard
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(desiredConcEditText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void renderButtonsSquare() {
        RadioGroup buttonsBar = (RadioGroup) findViewById(R.id.desiredConcButtonsBar);
        buttonsBar.getLayoutParams().height = buttonsBar.getWidth() / 4;

        RadioGroup stockButtonsBar = (RadioGroup) findViewById(R.id.stockConcButtonsBar);
        stockButtonsBar.getLayoutParams().height = buttonsBar.getWidth() / 4;
    }

    private void toggleSolutionFromStock() {
        boolean fromStock = ((ToggleButton) findViewById(R.id.enableStockDilutionButton)).isChecked();
        for (View stockView : stockViewsList) {
            if (fromStock) {
                stockView.setVisibility(View.VISIBLE);
            } else {
                stockView.setVisibility(View.INVISIBLE);
            }
        }
    }


}
