package com.druidpyrcel.biotech.finaldilution;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.druidpyrcel.biotech.finaldilution.sqlite.DataProvider;

import java.util.List;

public class StartupActivity extends AppCompatActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        appState.setDb(new DataProvider(appState));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        appState.setCurrentSolution(appState.getDb().getSolution("Roztwor1"));
        List<Solution> solutionList = appState.getDb().getAllSolutions();
        ListView solutionListView = (ListView) findViewById(R.id.solutionListView);
        ArrayAdapter<Solution> solutionListAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, solutionList);
        solutionListView.setAdapter(solutionListAdapter);
        solutionListView.setOnItemClickListener(new SolutionChooseListener());

    }

    private class SolutionChooseListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
            appState.setCurrentSolution((Solution) parent.getAdapter().getItem(position));
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }
}
