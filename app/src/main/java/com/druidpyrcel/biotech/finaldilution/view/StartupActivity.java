package com.druidpyrcel.biotech.finaldilution.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.druidpyrcel.biotech.finaldilution.ApplicationContext;
import com.druidpyrcel.biotech.finaldilution.R;
import com.druidpyrcel.biotech.finaldilution.model.Solution;

import java.text.DecimalFormat;
import java.util.List;

public class StartupActivity extends Activity {

    DecimalFormat volFormat = new DecimalFormat("0.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_startup);
        Button newSolutionButton = (Button) findViewById(R.id.addNewSolutionButton);
        newSolutionButton.setOnClickListener(new OnNewSolutionButtonClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSolutionList();
    }

    private void refreshSolutionList() {

        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        final List<Solution> solutionList = appState.getDb().getAllSolutions();
        if (!solutionList.isEmpty()) {
            //TODO Save and set last solution
            appState.setCurrentSolution(appState.getDb().getAllSolutions().get(0));
        }
        ListView solutionListView = (ListView) findViewById(R.id.solutionListView);
        ArrayAdapter<Solution> solutionListAdapter = new ArrayAdapter<Solution>(
                this, R.layout.solution_list_item, R.id.solution_list_text1, solutionList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.solution_list_text1);
                TextView text2 = (TextView) view.findViewById(R.id.solution_list_text2);
                Solution solution = solutionList.get(position);
                text1.setText(solution.getName());
                text2.setText(volFormat.format(solution.getVolume()) + " ml  "
                        + solution.getComponents().size() + " components");
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
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (solutionNamePicker.getText().length() != 0) {
                                Solution solution = new Solution();
                                solution.setName(solutionNamePicker.getText().toString());
                                appState.getDb().addSolution(solution);
                                refreshSolutionList();
                                appState.setCurrentSolution(appState.getDb().getSolution(solutionNamePicker.getText().toString()));
                                Intent intent = new Intent(StartupActivity.this, EditActivity.class);
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            solutionNamePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
            final Solution solution = (Solution) (parent.getAdapter().getItem(position));
            if (solution == null) {
                return false;
            }
            appState.getDb().removeSolution(solution);
            refreshSolutionList();
            return true;
        }
    }

}
