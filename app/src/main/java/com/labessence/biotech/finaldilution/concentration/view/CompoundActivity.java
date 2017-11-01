package com.labessence.biotech.finaldilution.concentration.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.concentration.Component;
import com.labessence.biotech.finaldilution.concentration.Concentration;
import com.labessence.biotech.finaldilution.concentration.ConcentrationType;
import com.labessence.biotech.finaldilution.peripherals.view.Anim;
import com.labessence.biotech.finaldilution.solution.Solution;
import com.labessence.biotech.finaldilution.solution.view.EditActivity;

import java.util.ArrayList;
import java.util.List;

public class CompoundActivity extends Activity {

    Compound compound;
    ConcentrationType desiredConcType = ConcentrationType.MOLAR;
    ConcentrationType stockConcType;
    private List<View> desiredViewsList;
    private List<View> stockViewsList;
    private List<RadioButton> desiredButtonList;
    private List<RadioButton> stockButtonList;

    public CompoundActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_compound);

        desiredButtonList = new ArrayList<>();
        desiredButtonList.add((RadioButton) findViewById(R.id.desiredPercentageConcButton));
        desiredButtonList.add((RadioButton) findViewById(R.id.desiredMolarConcButton));
        desiredButtonList.add((RadioButton) findViewById(R.id.desiredMilimolarConcButton));
        desiredButtonList.add((RadioButton) findViewById(R.id.desiredMgMlConcButton));

        stockButtonList = new ArrayList<>();
        stockButtonList.add((RadioButton) findViewById(R.id.stockPercentageConcButton));
        stockButtonList.add((RadioButton) findViewById(R.id.stockMolarConcButton));
        stockButtonList.add((RadioButton) findViewById(R.id.stockMilimolarConcButton));
        stockButtonList.add((RadioButton) findViewById(R.id.stockMgMlConcButton));

        desiredViewsList = new ArrayList<>();
        desiredViewsList.add(findViewById(R.id.desiredConcEditText));
        desiredViewsList.add(findViewById(R.id.desiredConcButtonsBar));
        desiredViewsList.add(findViewById(R.id.desiredConcTextView));
        desiredViewsList.addAll(desiredButtonList);

        stockViewsList = new ArrayList<>();
        stockViewsList.add(findViewById(R.id.stockConcEditText));
        stockViewsList.add(findViewById(R.id.stockConcButtonsBar));
        stockViewsList.add(findViewById(R.id.stockConcTextView));
        stockViewsList.addAll(stockButtonList);

        compound = (Compound) getIntent().getSerializableExtra("compound");
        setTitle("Add " + compound.getShortName());

        findViewById(R.id.desiredConcButtonsBar).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                renderButtonsSquare();
            }
        });

        setKeyboardOnInputs();
        bindListeners();
        setConcentrationButtonsState(false);
        fillComponentFields();
        toggleSolutionFromStock();
    }

    private void bindListeners() {
        findViewById(R.id.desiredPercentageConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = ConcentrationType.PERCENTAGE;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("%");
            }
        });

        findViewById(R.id.desiredMolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = ConcentrationType.MOLAR;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("M/l");
            }
        });

        findViewById(R.id.desiredMilimolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = ConcentrationType.MILIMOLAR;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("mM/l");
            }
        });

        findViewById(R.id.desiredMgMlConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desiredConcType = ConcentrationType.MILIGRAM_PER_MILLILITER;
                ((EditText) findViewById(R.id.desiredConcEditText)).setHint("mg/ml");
            }
        });

        findViewById(R.id.stockPercentageConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockConcType = ConcentrationType.PERCENTAGE;
                ((EditText) findViewById(R.id.stockConcEditText)).setHint("%");
            }
        });

        findViewById(R.id.stockMolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockConcType = ConcentrationType.MOLAR;
                ((EditText) findViewById(R.id.stockConcEditText)).setHint("M/l");
            }
        });

        findViewById(R.id.stockMilimolarConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockConcType = ConcentrationType.MILIMOLAR;
                ((EditText) findViewById(R.id.stockConcEditText)).setHint("mM/l");
            }
        });

        findViewById(R.id.stockMgMlConcButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockConcType = ConcentrationType.MILIGRAM_PER_MILLILITER;
                ((EditText) findViewById(R.id.stockConcEditText)).setHint("mg/ml");
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
                onCancelComponent();
            }
        });

        findViewById(R.id.buttonAddCompoundDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteComponent();
            }
        });

    }

    private void fillComponentFields() {
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        Component component = appState.getCurrentSolution().getComponentWithCompound(compound);
        if (component != null) {
            desiredConcType = component.getDesiredConcentration().getType();
            EditText desiredConcEditText = (EditText) findViewById(R.id.desiredConcEditText);
            desiredConcEditText.setText(Double.toString(component.getDesiredConcentration().getAmount()));
            if (component.getFromStock()) {
                EditText stockConcEditText = (EditText) findViewById(R.id.stockConcEditText);
                stockConcType = component.getAvailableConcentration().getType();
                stockConcEditText.setText(Double.toString(component.getAvailableConcentration().getAmount()));

            }
            setConcentrationButtonsState(component.getFromStock());
        }
    }

    private void setConcentrationButtonsState(boolean fromStock) {
        ((ToggleButton) (findViewById(R.id.enableStockDilutionButton))).setChecked(fromStock);
        RadioGroup desiredRadioGroup = (RadioGroup) findViewById(R.id.desiredConcButtonsBar);
        switch (desiredConcType) {
            case PERCENTAGE:
                desiredRadioGroup.check(R.id.desiredPercentageConcButton);
                break;
            case MOLAR:
                desiredRadioGroup.check(R.id.desiredMolarConcButton);
                break;
            case MILIMOLAR:
                desiredRadioGroup.check(R.id.desiredMilimolarConcButton);
                break;
            case MILIGRAM_PER_MILLILITER:
                desiredRadioGroup.check(R.id.desiredMgMlConcButton);
                break;
        }

        if (fromStock) {
            RadioGroup stockRadioGroup = (RadioGroup) findViewById(R.id.stockConcButtonsBar);
            switch (stockConcType) {
                case PERCENTAGE:
                    stockRadioGroup.check(R.id.stockPercentageConcButton);
                    break;
                case MOLAR:
                    stockRadioGroup.check(R.id.stockMolarConcButton);
                    break;
                case MILIMOLAR:
                    stockRadioGroup.check(R.id.stockMilimolarConcButton);
                    break;
                case MILIGRAM_PER_MILLILITER:
                    stockRadioGroup.check(R.id.stockMgMlConcButton);
                    break;
            }
        }

    }

    private void onCancelComponent() {
        Intent intent = new Intent(CompoundActivity.this, EditActivity.class);
        startActivity(intent);
    }

    //TODO run this listener also on back and generally close?
    private void onAcceptComponent() {
        boolean fromStock = ((ToggleButton) findViewById(R.id.enableStockDilutionButton)).isChecked();
        //TODO Add all checks!
        EditText desiredConcEditText = (EditText) findViewById(R.id.desiredConcEditText);
        EditText stockConcEditText = (EditText) findViewById(R.id.stockConcEditText);
        if (desiredConcEditText.getText().toString().trim().length() == 0) {
            (new Anim()).blink(desiredConcEditText);
            return;
        }

        if (fromStock && stockConcEditText.getText().toString().trim().length() == 0) {
            (new Anim()).blink(stockConcEditText);
            return;
        }

        if (desiredConcType == null) {
            //TODO Add animation to buttons
            return;
        }

        if (fromStock && stockConcType == null) {
            //TODO Add animation to buttons
            return;
        }


        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        Solution currentSolution = appState.getCurrentSolution();

        Component component = currentSolution.getComponentWithCompound(compound);

        if (component != null) {
            updateComponent(component);

        } else {
            component = createComponent();
        }

        appState.getSolutionGateway().update(currentSolution);

        double allComponentsVolume = currentSolution.getAllLiquidComponentsVolume();
        double currentComponentVolume = 0.0;
        if (fromStock) {
            currentComponentVolume = component.getQuantity(currentSolution.getVolume());
        }
        if (allComponentsVolume + currentComponentVolume > currentSolution.getVolume()) {
            //TODO Color?? move this code
//            appState.getDb().removeComponent(component);
//            appState.getDb().update(appState.getCurrentSolution());
//            appState.getCurrentSolution().resetComponents();
        }

        Intent intent = new Intent(CompoundActivity.this, EditActivity.class);
        startActivity(intent);
        //TODO WHEN ITEM EXISTS SHOW STH? OR BEFORE EVEN OPENING COMP.ACTIVITY?

    }

    private void onDeleteComponent() {

        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        Component component = appState.getCurrentSolution().getComponentWithCompound(compound);
        if (component == null) {
            onCancelComponent();
        } else {
            appState.getCurrentSolution().removeComponent(component);
            appState.getSolutionGateway().update(appState.getCurrentSolution());
            Intent intent = new Intent(CompoundActivity.this, EditActivity.class);
            startActivity(intent);
        }
    }

    private Component createComponent() {
        //TODO remove local vars
        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        EditText desiredConcEditText = (EditText) findViewById(R.id.desiredConcEditText);
        EditText stockConcEditText = (EditText) findViewById(R.id.stockConcEditText);
        boolean fromStock = ((ToggleButton) findViewById(R.id.enableStockDilutionButton)).isChecked();
        Component component = new Component(fromStock, appState.getCurrentSolution().getVolume(), compound);

        Concentration concentration = new Concentration();
        concentration.setAmount(Double.parseDouble(desiredConcEditText.getText().toString().replace(',', '.')));
        concentration.setType(desiredConcType);
        component.setDesiredConcentration(concentration);

        if (fromStock) {
            concentration = new Concentration();
            concentration.setAmount(Double.parseDouble(stockConcEditText.getText().toString().replace(',', '.')));
            concentration.setType(stockConcType);
            component.setAvailableConcentration(concentration);
        }

        appState.getCurrentSolution().addComponent(component);
        appState.getSolutionGateway().update(appState.getCurrentSolution());
        return component;
    }

    private void updateComponent(Component component) {
        //TODO remove local vars
        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        EditText desiredConcEditText = (EditText) findViewById(R.id.desiredConcEditText);
        EditText stockConcEditText = (EditText) findViewById(R.id.stockConcEditText);
        boolean fromStock = ((ToggleButton) findViewById(R.id.enableStockDilutionButton)).isChecked();
        component.getDesiredConcentration().setType(desiredConcType);
        component.getDesiredConcentration().setAmount(Double.parseDouble(desiredConcEditText.getText().toString().replace(',', '.')));
        if (fromStock) {
            if (!component.getFromStock()) {
                //from stock was added, so need to create concentration object
                Concentration concentration = new Concentration();
                concentration.setType(stockConcType);
                concentration.setAmount(Double.parseDouble(stockConcEditText.getText().toString().replace(',', '.')));
                component.setAvailableConcentration(concentration);
            }
            component.getAvailableConcentration().setType(stockConcType);
            component.getAvailableConcentration().setAmount(Double.parseDouble(stockConcEditText.getText().toString().replace(',', '.')));
        }
        component.setFromStock(fromStock);
        appState.getSolutionGateway().update(appState.getCurrentSolution());
    }


    private void setKeyboardOnInputs() {
        List<EditText> concEditTexts = new ArrayList<>();
        concEditTexts.add((EditText) findViewById(R.id.desiredConcEditText));
        concEditTexts.add((EditText) findViewById(R.id.stockConcEditText));
        for (EditText editText : concEditTexts) {

            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setRawInputType(Configuration.KEYBOARD_12KEY);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                    //TODO: Check this code for different versions of Android
                    if (keyCode == EditorInfo.IME_ACTION_DONE || ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                        // hide virtual keyboard
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        return true;
                    }
                    return false;
                }
            });
        }
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