package com.xenodochium.hercules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.Workout;

import java.util.List;

public class ItemOneFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
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
        populateExerciseListView();
    }

    /**
     * Populate list
     */
    public void populateExerciseListView() {
        List<Workout> workoutList = Hercules.getInstance().getDaoSession().getWorkoutDao().loadAll();
        ArrayAdapter<Workout> dataAdapter = new ArrayAdapter<Workout>(getActivity(), android.R.layout.simple_list_item_1, workoutList);
        listViewExercise.setAdapter(dataAdapter);
        listViewExercise.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Long workoutId = ((Workout) adapterView.getItemAtPosition(position)).getWorkoutId();
        Intent intent = new Intent(getActivity(), WorkoutActivity.class);
        intent.putExtra("workoutId", workoutId);
        startActivity(intent);
    }
}
