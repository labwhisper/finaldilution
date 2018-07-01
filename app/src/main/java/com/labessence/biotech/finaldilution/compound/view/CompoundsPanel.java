package com.labessence.biotech.finaldilution.compound.view;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.view.CompoundActivity;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.peripherals.view.Anim;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

public class CompoundsPanel {
    private final Activity activity;// extends BottomSheetDialogFragment {

    public CompoundsPanel(Activity activity) {
        this.activity = activity;
        appState = ((ApplicationContext) activity.getApplicationContext());
    }

    private static final String TAG = "Compound Panel";
    private ApplicationContext appState;

    public void displayCompoundList() {
        ListView compoundsListView = activity.findViewById(R.id.compoundsListView);
        compoundsListView.setOnItemClickListener(getCompoundClickListener());
        compoundsListView.setOnItemLongClickListener(getCompoundLongClickListener());
        updateCompoundList();
    }

    private void updateCompoundList() {
        ListView compoundsListView = activity.findViewById(R.id.compoundsListView);
        compoundsListView.setAdapter(getCompoundListAdapter());
    }

    @NonNull
    private ArrayAdapter<Compound> getCompoundListAdapter() {
        return new ArrayAdapter<>(
                activity, android.R.layout.simple_list_item_1, appState.getCompoundGateway().loadAll());
    }

    private void informAboutCompoundAlreadyAdded(View view, Compound compound) {
        new Anim().blinkWithRed(view);
        Log.d(TAG, "Compound (" + compound.getShortName() + ") already in solution (" + appState.getCurrentSolution().getName() + ")");
    }

    @NonNull
    private AdapterView.OnItemLongClickListener getCompoundLongClickListener() {
        return (parent, view, position, id) -> {
            final Compound compound = (Compound) (parent.getAdapter().getItem(position));
            appState.removeCompoundFromEverywhere(compound);
            // TODO Refresh activity if needed.
            // activity.refresh();
            return true;
        };
    }

    @NonNull
    private AdapterView.OnItemClickListener getCompoundClickListener() {
        return (parent, view, position, id) -> {
            Compound compound = (Compound) (parent.getAdapter().getItem(position));

            if (appState.getCurrentSolution().getComponentWithCompound(compound) != null) {
                informAboutCompoundAlreadyAdded(view, compound);
                return;
            }

            startComponentEdition(compound);
        };
    }

    private void startComponentEdition(Compound compound) {
        Intent intent = new Intent(activity, CompoundActivity.class);
        intent.putExtra("compound", compound);
        activity.startActivity(intent);
    }

}
