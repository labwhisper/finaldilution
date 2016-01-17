package com.druidpyrcel.biotech.finaldilution;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.druidpyrcel.biotech.finaldilution.model.Component;
import com.druidpyrcel.biotech.finaldilution.model.Compound;

import java.util.Map;

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
        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        ChecklistAdapter adapter = new ChecklistAdapter(appState.getCurrentSolution().getComponentList());
        ListView componentListView = (ListView) findViewById(R.id.checklist_listView);
        componentListView.setAdapter(adapter);
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

    private class ChecklistAdapter extends BaseAdapter {

        private Map<String, Component> componentList;
        private String[] keys;

        public ChecklistAdapter(Map<String, Component> componentList) {
            this.componentList = componentList;
            this.keys = this.componentList.keySet().toArray(new String[componentList.size()]);
        }

        @Override
        public int getCount() {
            return componentList.size();
        }

        @Override
        public Object getItem(int position) {
            return componentList.get(keys[position]);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.checklist, null);

                holder = new ViewHolder();
                holder.compoundTextView = (TextView) convertView.findViewById(R.id.checklist_mainTextView);
                holder.percentageTextView = (TextView) convertView.findViewById(R.id.checklist_percentageTextView);
                holder.unitTextView = (TextView) convertView.findViewById(R.id.checklist_unitextView);
                holder.extraTextView = (TextView) convertView.findViewById(R.id.checklist_extraTextView);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checklist_checkBox1);
                convertView.setTag(holder);

//                holder.name.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View view) {
//                        CheckBox cb = (CheckBox) view;
//                        Compound component = (Compound) cb.getTag();
//                        component.setSelected(cb.isChecked());
//                    }
//                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            ApplicationContext appState = ((ApplicationContext) getApplicationContext());

            Compound component = appState.getDb().getCompound(keys[position]);
            Double quantity = ((Component) getItem(position)).getQuantity();
            holder.compoundTextView.setText(component.getShortName());
            holder.percentageTextView.setText(Double.toString(quantity));
            holder.checkBox.setChecked(false);
            holder.checkBox.setTag(component);

            return convertView;

        }

        private class ViewHolder {
            TextView compoundTextView;
            TextView percentageTextView;
            TextView unitTextView;
            TextView extraTextView;
            CheckBox checkBox;
        }

    }

}
