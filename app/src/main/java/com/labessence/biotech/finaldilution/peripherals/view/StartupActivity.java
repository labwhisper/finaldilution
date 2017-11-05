package com.labessence.biotech.finaldilution.peripherals.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.peripherals.datastores.SharedPreferencesStore;
import com.labessence.biotech.finaldilution.solution.Solution;
import com.labessence.biotech.finaldilution.solution.view.EditActivity;

import java.text.DecimalFormat;
import java.util.List;

public class StartupActivity extends Activity {

    DecimalFormat volFormat = new DecimalFormat("0.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_startup);

        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        appState.setSolutionGateway(new SharedPreferencesStore<>(
                getSharedPreferences("solutions", MODE_PRIVATE), new TypeToken<List<Solution>>() {
        }));
        appState.setCompoundGateway(new SharedPreferencesStore<>(
                getSharedPreferences("compounds", MODE_PRIVATE), new TypeToken<List<Compound>>() {
        }));

        Button newSolutionButton = (Button) findViewById(R.id.addNewSolutionButton);
        newSolutionButton.setOnClickListener(new OnNewSolutionButtonClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSolutionList();
    }

    private void refreshSolutionList() {

        //TODO Extract setting current solution from file elswhere
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        final List<Solution> solutionList = appState.getSolutionGateway().loadAll();
        if (!solutionList.isEmpty()) {
            //TODO Save and set last solution
            appState.setCurrentSolution(appState.getSolutionGateway().loadAll().get(0));
        }
        ListView solutionListView = (ListView) findViewById(R.id.solutionListView);
        ArrayAdapter<Solution> solutionListAdapter = new ArrayAdapter<Solution>(
                this, R.layout.solution_list_item, R.id.solution_list_text1, solutionList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.solution_list_text1);
                TextView text2 = (TextView) view.findViewById(R.id.solution_list_text2);
                Solution solution = solutionList.get(position);
                text1.setText(solution.getName());
                text2.setText(String.format(getString(R.string.solutionListPrepFormat), volFormat.format(solution.getVolume()), solution.getComponents().size()));
                return view;
            }
        };
        solutionListView.setAdapter(solutionListAdapter);
        solutionListView.setOnItemClickListener(new SolutionChooseListener());
        solutionListView.setOnItemLongClickListener(new SolutionLongClickListener());
    }

    private class OnNewSolutionButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            final EditText solutionNamePicker = new EditText(StartupActivity.this);
            final ApplicationContext appState = ((ApplicationContext) getApplicationContext());

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartupActivity.this);
            alertDialogBuilder.setView(solutionNamePicker)
                    .setMessage("Enter new solution name: ")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        if (solutionNamePicker.getText().length() != 0) {
                            //TODO Extract those 3 lines into app entities or no?
                            Solution solution = new Solution();
                            solution.setName(solutionNamePicker.getText().toString());
                            appState.getSolutionGateway().save(solution);
                            refreshSolutionList();
                            appState.setCurrentSolution(appState.getSolutionGateway().load(solutionNamePicker.getText().toString()));
                            Intent intent = new Intent(StartupActivity.this, EditActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            solutionNamePicker.setOnFocusChangeListener((v1, hasFocus) -> {
                if (hasFocus && alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            });
            alertDialog.show();
        }
    }

    private class SolutionChooseListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
            appState.setCurrentSolution((Solution) parent.getAdapter().getItem(position));
            Intent intent = new Intent(StartupActivity.this, EditActivity.class);
            startActivity(intent);
        }
    }

    class SolutionLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ApplicationContext appState = ((ApplicationContext) getApplicationContext());
            //TODO Extract deleting solution into view logic layer. // app entity
            final Solution solution = (Solution) (parent.getAdapter().getItem(position));
            if (solution == null) {
                return false;
            }
            appState.getSolutionGateway().remove(solution);
            refreshSolutionList();
            return true;
        }
    }

}
