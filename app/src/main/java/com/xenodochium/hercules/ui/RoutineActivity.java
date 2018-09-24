package com.xenodochium.hercules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropRoutineEntryItemAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.Routine;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.model.Workout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RoutineActivity extends AppCompatActivity implements View.OnClickListener {

    private DragListView mDragListView;
    public final static int REQUEST_WORKOUT = 9382;
    private com.github.clans.fab.FloatingActionButton buttonAddExercise, buttonAddRest;
    private TextInputLayout tilRoutineName;
    private Button buttonOk, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        getSupportActionBar().hide();
        initializeViews();
    }

    /**
     * Initialize views
     */
    private void initializeViews() {
        tilRoutineName = findViewById(R.id.til_routine_name);
        buttonAddExercise = findViewById(R.id.menu_item_add_exercise);
        buttonAddRest = findViewById(R.id.menu_item_add_rest);

        buttonOk = findViewById(R.id.button_ok);
        buttonCancel = findViewById(R.id.button_cancel);

        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonAddRest.setOnClickListener(this);
        buttonAddExercise.setOnClickListener(this);

        mDragListView = findViewById(R.id.drag_list_view_routine);
        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(getApplicationContext(), "Moving " + ((RoutineEntry) mDragListView.getAdapter().getItemList().get(position)).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(getApplicationContext(), "Moved  " + ((RoutineEntry) mDragListView.getAdapter().getItemList().get(toPosition)).getName()
                            + " From " + (fromPosition + 1) + " To " + (toPosition + 1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDragListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<RoutineEntry> routineEntryList = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            Hercules.getInstance().getDaoSession().clear();
            Routine routine = Hercules.getInstance().getDaoSession().getRoutineDao().load((Long) getIntent().getExtras().get("routineId"));
            tilRoutineName.getEditText().setText(routine.getName());
            routineEntryList = routine.getLinkedRoutineEntries();
            ((TextView) findViewById(R.id.text_view_create_routine)).setText(getString(R.string.edit_routine));
            //don't show keyboard
            getWindow().setSoftInputMode(WindowManager.
                    LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        populateDragListView(routineEntryList);
    }

    /**
     * Populate drag and drop list
     *
     * @param routineEntryList
     */
    private void populateDragListView(List<RoutineEntry> routineEntryList) {
        DragAndDropRoutineEntryItemAdapter listAdapter = new DragAndDropRoutineEntryItemAdapter(RoutineActivity.this, routineEntryList, R.layout.drag_view_list_item, R.id.grab_handle, true);
        mDragListView.setAdapter(listAdapter, true);
        //mDragListView.setCanDragHorizontally(true);

        mDragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {

            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                // Swipe to delete on left
                //if (swipedDirection == ListSwipeItem.SwipeDirection.RIGHT) {
                RoutineEntry routineEntry = (RoutineEntry) item.getTag();
                int pos = mDragListView.getAdapter().getPositionForItem(routineEntry);
                mDragListView.getAdapter().removeItem(pos);
                mDragListView.getAdapter().notifyDataSetChanged();
                //}
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_item_add_exercise:
                ((FloatingActionMenu) findViewById(R.id.fam_routine_menu)).close(true);
                Intent intent = new Intent(RoutineActivity.this, SelectWorkoutActivity.class);
                startActivityForResult(intent, REQUEST_WORKOUT);
                break;
            case R.id.menu_item_add_rest:
                ((FloatingActionMenu) findViewById(R.id.fam_routine_menu)).close(true);
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.alert_box_add_rest, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(RoutineActivity.this, R.style.AppThemeDialog);
                alertDialogBuilderUserInput.setView(mView);

                alertDialogBuilderUserInput.setCancelable(false);
                final AlertDialog alertDialogAddRest = alertDialogBuilderUserInput.create();
                Button buttonAlertCancel = mView.findViewById(R.id.button_alert_cancel);
                Button buttonAlertOk = mView.findViewById(R.id.button_alert_ok);
                final EditText editTextBreakDuration = mView.findViewById(R.id.edit_text_add_break_duration);
                buttonAlertOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editTextBreakDuration.getText().toString().isEmpty()) {
                            editTextBreakDuration.setError(getResources().getString(R.string.cannot_be_empty));
                        } else {
                            RoutineEntry routineEntry = new RoutineEntry();
                            routineEntry.setRoutineEntryId(new Random().nextLong());
                            routineEntry.setName(RoutineEntry.RoutineEntryType.REST.toString());
                            routineEntry.setRoutineEntryType(RoutineEntry.RoutineEntryType.REST);
                            routineEntry.setDuration(Integer.valueOf(editTextBreakDuration.getText().toString()));
                            mDragListView.getAdapter().getItemList().add(routineEntry);
                            populateDragListView(mDragListView.getAdapter().getItemList());
                            alertDialogAddRest.dismiss();
                        }
                    }
                });
                buttonAlertCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogAddRest.dismiss();
                    }
                });


                alertDialogAddRest.show();
                break;
            case R.id.button_ok:
                if (routineDataIsValid()) {
                    saveRoutine();
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    /**
     * Save routine
     */
    private void saveRoutine() {

        Hercules.getInstance().getDaoSession().getDatabase().beginTransaction();

        Routine routine = new Routine();
        routine.setName(tilRoutineName.getEditText().getText().toString());

        if (getIntent().getExtras() != null) {
            long routineId = (Long) getIntent().getExtras().get("routineId");
            routine.setRoutineId(routineId);

            //delete old RoutineEntries
            routine.deleteAllLinkedEntries();
        }

        Hercules.getInstance().getDaoSession().getRoutineDao().insertOrReplace(routine);

        if (mDragListView.getAdapter() != null) {
            List<RoutineEntry> routineEntryList = mDragListView.getAdapter().getItemList();
            if (routineEntryList.size() > 0) {
                Iterator<RoutineEntry> routineEntryIterator = routineEntryList.iterator();
                while (routineEntryIterator.hasNext()) {
                    RoutineEntry routineEntry = routineEntryIterator.next();
                    routineEntry.setRoutineEntryId(null); //remove any previously assigned ID
                    routineEntry.setRoutineId(routine.getRoutineId());
                    Hercules.getInstance().getDaoSession().getRoutineEntryDao().insert(routineEntry);
                }
            }
        }
        Hercules.getInstance().getDaoSession().getDatabase().setTransactionSuccessful();
        Hercules.getInstance().getDaoSession().getDatabase().endTransaction();

        finish();
    }

    /**
     * Check if data entered by user is valid
     *
     * @return
     */
    private boolean routineDataIsValid() {
        boolean routineDataIsValid = true;
        if (tilRoutineName.getEditText().getText().toString().isEmpty()) {
            tilRoutineName.getEditText().setError(getString(R.string.cannot_be_empty));
            routineDataIsValid = false;
        }
        return routineDataIsValid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_WORKOUT && resultCode == RESULT_OK) {
            List<RoutineEntry> selectedWorkoutList = RoutineEntry.convertWorkoutListToRoutineEntryList(null, (List<Workout>) data.getSerializableExtra("selectedWorkouts"));
            List<RoutineEntry> existingWorkoutList = new ArrayList<>();
            if (mDragListView.getAdapter() != null)
                existingWorkoutList = mDragListView.getAdapter().getItemList();
            existingWorkoutList.addAll(selectedWorkoutList);
            if (selectedWorkoutList != null) {
                populateDragListView(existingWorkoutList);
                Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.routine_activity_instructions), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
