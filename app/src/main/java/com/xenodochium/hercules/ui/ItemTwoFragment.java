package com.xenodochium.hercules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.Voice;
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
import com.xenodochium.hercules.model.Routine;
import com.xenodochium.hercules.speech.HerculesSpeechEngine;

import java.util.Iterator;
import java.util.List;

public class ItemTwoFragment extends Fragment implements View.OnClickListener {
    private ListView listViewRoutine;
    private View fragmentView;
    private FloatingActionButton buttonAddRoutine;

    int i = 0;

    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Iterator<Voice> voices;

    /**
     * Populate routines
     */
    public void populateRoutineListView() {
        List<Routine> routineList = Hercules.getInstance().getDaoSession().getRoutineDao().loadAll();
        ArrayAdapter<Routine> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, routineList);
        listViewRoutine.setAdapter(dataAdapter);
        listViewRoutine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Routine routine = ((Routine) adapterView.getItemAtPosition(position));
                Intent intent = new Intent(getActivity(), RoutineActivity.class);
                intent.putExtra("routineId", routine.getRoutineId());
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_item_two, container, false);
        initializeViews();
        return fragmentView;
    }

    private void initializeViews() {
        listViewRoutine = fragmentView.findViewById(R.id.list_view_routine);
        buttonAddRoutine = fragmentView.findViewById(R.id.button_add_routine);
        buttonAddRoutine.setOnClickListener(this);
        populateRoutineListView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_routine:
                //startActivity(new Intent(getActivity(), RoutineActivity.class));
                HerculesSpeechEngine.speak("Hi there... I am Hercules, your virtual gym trainer.");
                HerculesSpeechEngine.speak("I am ready");
                break;
        }
    }
}
