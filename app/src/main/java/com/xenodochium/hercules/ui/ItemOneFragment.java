package com.xenodochium.hercules.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.StandardHomeListItemAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.model.Workout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemOneFragment extends Fragment implements View.OnClickListener, StandardHomeListItemAdapter.OnItemClickListener {
    private ListView listViewExercise;
    private View fragmentView;
    private FloatingActionButton buttonAddExercise;

    public static ItemOneFragment newInstance() {
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Initialize Views
     */
    private void initializeViews() {
        listViewExercise = fragmentView.findViewById(R.id.list_view_exercise);
        buttonAddExercise = fragmentView.findViewById(R.id.button_add_exercise);
        buttonAddExercise.setOnClickListener(this);
        populateWorkoutListView();
    }

    /**
     * Populate list
     */
    public void populateWorkoutListView() {
        List<Workout> workoutList = Hercules.getInstance().getDaoSession().getWorkoutDao().loadAll();
        StandardHomeListItemAdapter<Workout> dataAdapter = new StandardHomeListItemAdapter<Workout>(this, workoutList);
        listViewExercise.setAdapter(dataAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_item_one, container, false);
        initializeViews();
        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_exercise:
                startActivity(new Intent(getActivity(), WorkoutActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(Object selectedItem) {
        Long workoutId = ((Workout) selectedItem).getWorkoutId();
        Intent intent = new Intent(getActivity(), WorkoutActivity.class);
        intent.putExtra("workoutId", workoutId);
        startActivity(intent);
    }

    @Override
    public void onItemPlay(Object selectedItem) {
        Workout workout = (Workout) selectedItem;
        List<Workout> workoutList = new ArrayList<>();
        workoutList.add(workout);

        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra("workoutList", (Serializable) RoutineEntry.convertWorkoutListToRoutineEntryList((long) 0, workoutList));
        startActivity(intent);
    }

    @Override
    public void onItemDelete(final Object selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete) + " Workout ?")
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_item))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Hercules.getInstance().getDaoSession().getWorkoutDao().delete((Workout) selectedItem);
                        populateWorkoutListView();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
