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
import com.xenodochium.hercules.model.Routine;

import java.io.Serializable;
import java.util.List;

public class ItemTwoFragment extends Fragment implements View.OnClickListener, StandardHomeListItemAdapter.OnItemClickListener {
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

    /**
     * Populate routines
     */
    public void populateRoutineListView() {
        List<Routine> routineList = Hercules.getInstance().getDaoSession().getRoutineDao().loadAll();
        StandardHomeListItemAdapter<Routine> dataAdapter = new StandardHomeListItemAdapter<>(this, routineList);
        listViewRoutine.setAdapter(dataAdapter);
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
                startActivity(new Intent(getActivity(), RoutineActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(Object selectedItem) {
        Routine routine = ((Routine) selectedItem);
        Intent intent = new Intent(getActivity(), RoutineActivity.class);
        intent.putExtra("routineId", routine.getRoutineId());
        startActivity(intent);
    }

    @Override
    public void onItemPlay(Object selectedItem) {
        Routine routine = (Routine) selectedItem;
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra("workoutList", (Serializable) routine.getLinkedRoutineEntries());
        startActivity(intent);
    }

    @Override
    public void onItemDelete(final Object selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete) + " Routine ?")
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_item))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Hercules.getInstance().getDaoSession().getRoutineDao().delete((Routine) selectedItem);
                        populateRoutineListView();
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
