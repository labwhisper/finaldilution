package com.druidpyrcel.biotech.finaldilution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class PrepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep);

        displayComponentListView();
        displayFromPrepToEditButton();

        displayTitleToolbar();
    }


    private void displayComponentListView() {

    }

    private void displayFromPrepToEditButton() {
        Button button = (Button) findViewById(R.id.fromPrepToEditButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrepActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayTitleToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
