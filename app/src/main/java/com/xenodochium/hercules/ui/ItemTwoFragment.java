package com.xenodochium.hercules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.Routine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemTwoFragment extends Fragment implements View.OnClickListener {
    private ListView listViewRoutine;
    private View fragmentView;
    private FloatingActionButton buttonAddRoutine;

    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeViews() {
        listViewRoutine = fragmentView.findViewById(R.id.list_view_routine);
        buttonAddRoutine = fragmentView.findViewById(R.id.button_add_routine);
        buttonAddRoutine.setOnClickListener(this);
        populateRoutineListView();
    }

    public void populateRoutineListView() {
        Iterator<Routine> routineItemList = Hercules.getInstance().getDaoSession().getRoutineDao().loadAll().iterator();
        List<String> routineList = new ArrayList<>();
        while (routineItemList.hasNext()) {
            routineList.add(routineItemList.next().getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, routineList);
        listViewRoutine.setAdapter(dataAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_item_two, container, false);
        initializeViews();
        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_routine:
                startActivity(new Intent(getActivity(), RoutineActivity.class));
                break;
        }
    }
}
