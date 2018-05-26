package com.labessence.biotech.finaldilution.compound.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.labessence.biotech.finaldilution.ApplicationContext;
import com.labessence.biotech.finaldilution.R;
import com.labessence.biotech.finaldilution.component.view.CompoundActivity;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.peripherals.gestures.CompoundListGestureListener;
import com.labessence.biotech.finaldilution.peripherals.view.Anim;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

public class CompoundsPanel extends Fragment {

    private static final String TAG = "Compound Panel";
    private ApplicationContext appState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        appState = ((ApplicationContext) getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.compound_list, container, false);
        displayCompoundList(view);
        // displayNewCompoundButton(view);


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.commit();
        return view;
    }

    public void displayCompoundList(View view) {
        ListView compoundsListView = (ListView) view.findViewById(R.id.compoundsListView);
        compoundsListView.setOnItemClickListener(getCompoundClickListener());
        compoundsListView.setOnItemLongClickListener(getCompoundLongClickListener());
        updateCompoundList(view);
        GestureDetector compoundListGestureDetector =
                new GestureDetector(getActivity(), new CompoundListGestureListener(getActivity(), compoundsListView));
        compoundsListView.setOnTouchListener((v, motionEvent) -> {
            if (compoundListGestureDetector.onTouchEvent(motionEvent)) {
                return true;
            }
            return false;
        });
    }

    public void updateCompoundList(View view) {
        ListView compoundsListView = (ListView) view.findViewById(R.id.compoundsListView);
        compoundsListView.setAdapter(getCompoundListAdapter());
    }

    @NonNull
    private ArrayAdapter<Compound> getCompoundListAdapter() {
        return new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, appState.getCompoundGateway().loadAll());
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

    public void startComponentEdition(Compound compound) {
        Intent intent = new Intent(getActivity(), CompoundActivity.class);
        intent.putExtra("compound", compound);
        startActivity(intent);
    }

//
//    public void displayNewCompoundButton() {
//        new NewCompoundCreator(activity).displayNewCompoundButton();
//    }
}
