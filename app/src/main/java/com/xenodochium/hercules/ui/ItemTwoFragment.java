package com.xenodochium.hercules.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.StandardHomeListItemAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.Routine;
import com.xenodochium.hercules.model.RoutineEntry;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ItemTwoFragment extends Fragment implements View.OnClickListener, StandardHomeListItemAdapter.OnItemClickListener, HerculesHomeFragment {
    private ListView listViewRoutine;
    private View fragmentView;
    private FloatingActionButton buttonAddRoutine;
    public ImageButton buttonHelpRoutine;

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
        if(listViewRoutine!=null) {
            listViewRoutine.setAdapter(dataAdapter);
        }
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
        buttonHelpRoutine = fragmentView.findViewById(R.id.button_help_routine);
        buttonAddRoutine.setOnClickListener(this);
        buttonHelpRoutine.setOnClickListener(this);
        populateRoutineListView();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_routine:
                startActivity(new Intent(getActivity(), RoutineActivity.class));
                break;
            case R.id.button_help_routine:
                new ShowcaseHelper.RoutineHelperBuilder(getActivity(), listViewRoutine,
                        buttonAddRoutine, buttonHelpRoutine).startShowcase(String.valueOf(System.currentTimeMillis()) + "_add");
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
        intent.putExtra("label", routine.getName());
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
                        List<RoutineEntry> routineEntryList = (((Routine) selectedItem)).getLinkedRoutineEntries();
                        Hercules.getInstance().getDaoSession().getRoutineEntryDao().deleteInTx(routineEntryList);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ShowcaseHelper.RoutineHelperBuilder(getActivity(), listViewRoutine,
                buttonAddRoutine, buttonHelpRoutine).startShowcase("first_help");
    }

    @Override
    public void onFragmentSelected() {
        populateRoutineListView();
    }
}
