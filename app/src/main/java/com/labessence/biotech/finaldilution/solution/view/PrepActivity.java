package com.labessence.biotech.finaldilution.solution.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.Component;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.solution.Solution;

import java.util.List;

public class PrepActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_prep);

        displayComponentListView();
        displayFromPrepToEditButton();

        final ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        setTitle("Prepare " + appState.getCurrentSolution().getName());
    }


    private void displayComponentListView() {
        ApplicationContext appState = ((ApplicationContext) getApplicationContext());
        ChecklistAdapter adapter = new ChecklistAdapter(appState.getCurrentSolution().getComponents());
        ListView componentListView = (ListView) findViewById(R.id.checklist_listView);
        componentListView.setAdapter(adapter);
        componentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CheckBox)view.findViewById(R.id.checklist_checkBox1)).toggle();
            }
        });
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

    private class ChecklistAdapter extends BaseAdapter {

        private List<Component> componentList;

        public ChecklistAdapter(List<Component> componentList) {
            this.componentList = componentList;
        }

        @Override
        public int getCount() {
            return componentList.size();
        }

        @Override
        public Object getItem(int position) {
            return componentList.get(position);
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
//                        Compound compound = (Compound) cb.getTag();
//                        compound.setSelected(cb.isChecked());
//                    }
//                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            ApplicationContext appState = ((ApplicationContext) getApplicationContext());

            Solution solution = appState.getCurrentSolution();
            Component component = (Component) getItem(position);
            Compound compound = component.getCompound();
            holder.compoundTextView.setText(component.getCompound().getShortName());
            holder.unitTextView.setText(component.getAmountString(solution.getVolume()));
            if (component.getFromStock()) {
                holder.extraTextView.setText(component.getAvailableConcentration().toString());
            }
            holder.checkBox.setChecked(false);
            holder.checkBox.setTag(compound);

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
