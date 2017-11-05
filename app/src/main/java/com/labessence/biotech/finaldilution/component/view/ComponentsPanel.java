package com.labessence.biotech.finaldilution.component.view;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.Component;
import com.labessence.biotech.finaldilution.solution.view.EditActivity;

import java.util.List;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

public class ComponentsPanel {

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
    private ArrayAdapter<Component> getComponentListAdapter() {
        List<Component> components = appState.getCurrentSolution().getComponents();
        return new ArrayAdapter<>(activity, R.layout.tiny_list, components);
    }

}
