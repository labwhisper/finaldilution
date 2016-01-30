package com.druidpyrcel.biotech.finaldilution.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compound);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            }
        });

        findViewById(R.id.desiredMolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = Concentration.ConcentrationType.MOLAR;
            }
        });

        findViewById(R.id.desiredMilimolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = Concentration.ConcentrationType.MILIMOLAR;
            }
        });

        findViewById(R.id.desiredMgMlConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = Concentration.ConcentrationType.MILIGRAM_PER_MILLILITER;
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
        EditText desiredConcEditText = (EditText) findViewById(R.id.desiredConcEditText);
        desiredConcEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        desiredConcEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
    }

    private void renderButtonsSquare() {
        View buttonsBar = findViewById(R.id.desiredConcButtonsBar);
        RelativeLayout.LayoutParams params;
        params = (RelativeLayout.LayoutParams) buttonsBar.getLayoutParams();
        params.height = buttonsBar.getWidth() / 4;
        buttonsBar.setLayoutParams(params);
        buttonsBar.postInvalidate();
        View stockButtonsBar = findViewById(R.id.stockConcButtonsBar);
        params = (RelativeLayout.LayoutParams) stockButtonsBar.getLayoutParams();
        params.height = stockButtonsBar.getWidth() / 4;
        stockButtonsBar.setLayoutParams(params);
        stockButtonsBar.postInvalidate();
    }

    private void toggleSolutionFromStock() {
        List<View> stockViewsList = new ArrayList<>();
        stockViewsList.add(findViewById(R.id.stockConcEditText));
        stockViewsList.add(findViewById(R.id.stockConcButtonsBar));
        stockViewsList.add(findViewById(R.id.stockConcTextView));
        stockViewsList.add(findViewById(R.id.stockPercentageConcButton));
        stockViewsList.add(findViewById(R.id.stockMolarConcButton));
        stockViewsList.add(findViewById(R.id.stockMilimolarConcButton));
        stockViewsList.add(findViewById(R.id.stockMgMlConcButton));

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
