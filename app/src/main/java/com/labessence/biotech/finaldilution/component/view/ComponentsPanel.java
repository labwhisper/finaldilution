package com.labessence.biotech.finaldilution.component.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.Component;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.solution.Solution;
import com.labessence.biotech.finaldilution.solution.view.EditActivity;

import java.util.List;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

public class ComponentsPanel {

    private static final String TAG = "Component Panel";
    private final EditActivity activity;
    private final ApplicationContext appState;

    public ComponentsPanel(EditActivity activity) {
        this.activity = activity;
        appState = ((ApplicationContext) activity.getApplicationContext());
    }


    public void displayComponentList() {
        ListView componentsListView = (ListView) activity.findViewById(R.id.componentsTextView);
        componentsListView.setOnItemClickListener(getComponentClickListener());
        componentsListView.setOnItemLongClickListener(getComponentLongClickListener());
        updateComponentList();
    }

    public void updateComponentList() {
        ListView componentsListView = (ListView) activity.findViewById(R.id.componentsTextView);
        componentsListView.setAdapter(getComponentListAdapter());
        updateOverflowState(componentsListView);
    }

    private void updateOverflowState(ListView componentsListView) {
        componentsListView.setBackgroundColor(Color.WHITE);
        if (appState.getCurrentSolution().isOverflown())
            highlightAllLiquidComponents(componentsListView);
    }

    private void highlightAllLiquidComponents(ListView componentsListView) {
        for (Component component : appState.getCurrentSolution().getComponents())
            if (component.getFromStock()) {
                componentsListView.setBackgroundColor(Color.YELLOW);
            }
    }

    @NonNull
    private AdapterView.OnItemClickListener getComponentClickListener() {
        return (parent, view, position, id) -> {
            final Component component = (Component) (parent.getAdapter().getItem(position));
            if (component == null || component.getCompound() == null) {
                return;
            }
            activity.startComponentEdition(component.getCompound());
        };
    }

    @NonNull
    private AdapterView.OnItemLongClickListener getComponentLongClickListener() {
        return (parent, view, position, id) -> {
            final Component component = (Component) (parent.getAdapter().getItem(position));
            if (component == null) {
                return false;
            }
            appState.getCurrentSolution().removeComponent(component);
            activity.refresh();
            return true;
        };
    }

    @NonNull
    private ChecklistAdapter getComponentListAdapter() {
        List<Component> components = appState.getCurrentSolution().getComponents();
        Log.d(TAG, "Components: " + components);
        return new ChecklistAdapter(components);
        //return new ArrayAdapter<>(activity, R.layout.tiny_list, components);
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
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.checklist, null);

                holder = new ViewHolder();
                holder.compoundTextView = (TextView) convertView.findViewById(R.id
                        .checklist_mainTextView);
                holder.percentageTextView = (TextView) convertView.findViewById(R.id
                        .checklist_percentageTextView);
                holder.unitTextView = (TextView) convertView.findViewById(R.id
                        .checklist_unitextView);
                holder.extraTextView = (TextView) convertView.findViewById(R.id
                        .checklist_extraTextView);
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


            ApplicationContext appState = ((ApplicationContext) activity.getApplicationContext());

            Solution solution = appState.getCurrentSolution();
            Component component = (Component) getItem(position);
            Compound compound = component.getCompound();
            Log.d(TAG, "Solution: " + solution);
            Log.d(TAG, "Component: " + component);
            Log.d(TAG, "Component stock: " + component.getAvailableConcentration());
            Log.d(TAG, "Compound: " + compound);
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
