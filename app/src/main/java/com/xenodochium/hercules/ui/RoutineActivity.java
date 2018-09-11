package com.xenodochium.hercules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropRoutineItemAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.Routine;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.model.Workout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
                Toast.makeText(getApplicationContext(), "Moving " + ((Workout) mDragListView.getAdapter().getItemList().get(position)).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(getApplicationContext(), "Moved  " + ((Workout) mDragListView.getAdapter().getItemList().get(toPosition)).getName()
                            + " From " + (fromPosition + 1) + " To " + (toPosition + 1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDragListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (getIntent().getExtras() != null) {
            Routine routine = Hercules.getInstance().getDaoSession().getRoutineDao().load((Long) getIntent().getExtras().get("routineId"));
            tilRoutineName.getEditText().setText(routine.getName());
            Iterator<RoutineEntry> routineEntryList = routine.getLinkedRoutineEntries().iterator();
            List<Workout> workoutList = new ArrayList<>();
            while (routineEntryList.hasNext()) {
                RoutineEntry routineEntry = routineEntryList.next();

                Workout workout = new Workout();
                workout.setWorkoutId(routineEntry.getRoutineEntryId());
                workout.setName(routineEntry.getName());
                workout.setBodyPartId(routineEntry.getBodyPartId());
                workout.setStandardNumberOfRepetitions(routineEntry.getStandardNumberOfRepetitions());
                workout.setStandardNumberOfSets(routineEntry.getStandardNumberOfSets());
                workout.setDuration(routineEntry.getDuration());
                workout.setCountDurationInReverse(routineEntry.getCountDurationInReverse());
                workout.setCountRepetitionsInReverse(routineEntry.getCountRepetitionsInReverse());
                workout.setTimeToGetInPosition(routineEntry.getTimeToGetInPosition());
                workout.setRestTimeAfterExercise(routineEntry.getRestTimeAfterExercise());
                workoutList.add(workout);
            }

            populateDragListView(workoutList);
            ((TextView) findViewById(R.id.text_view_create_routine)).setText(getString(R.string.edit_routine));

            //don't show keyboard
            getWindow().setSoftInputMode(WindowManager.
                    LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

    }

    /**
     * Populate drag and drop list
     *
     * @param workoutList
     */
    private void populateDragListView(List<Workout> workoutList) {
        DragAndDropRoutineItemAdapter listAdapter = new DragAndDropRoutineItemAdapter(RoutineActivity.this, workoutList, R.layout.drag_view_list_item, R.id.grab_handle, true);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(true);

        mDragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {

            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                // Swipe to delete on left
                //if (swipedDirection == ListSwipeItem.SwipeDirection.RIGHT) {
                Workout adapterItem = (Workout) item.getTag();
                int pos = mDragListView.getAdapter().getPositionForItem(adapterItem);
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
            Hercules.getInstance().getDaoSession().getRoutineEntryDao().deleteInTx(Hercules.getInstance().getDaoSession().getRoutineDao().load(routineId).getLinkedRoutineEntries());
        }

        Hercules.getInstance().getDaoSession().getRoutineDao().insertOrReplace(routine);

        Iterator<Workout> workoutList = mDragListView.getAdapter().getItemList().iterator();
        while (workoutList.hasNext()) {
            Workout selectedWorkout = workoutList.next();

            RoutineEntry routineEntry = new RoutineEntry();
            routineEntry.setRoutineEntryType(RoutineEntry.RoutineEntryType.WORKOUT);
            routineEntry.setName(selectedWorkout.getName());
            routineEntry.setBodyPartId(selectedWorkout.getBodyPartId());
            routineEntry.setStandardNumberOfRepetitions(selectedWorkout.getStandardNumberOfRepetitions());
            routineEntry.setStandardNumberOfSets(selectedWorkout.getStandardNumberOfSets());
            routineEntry.setDuration(selectedWorkout.getDuration());
            routineEntry.setCountDurationInReverse(selectedWorkout.getCountDurationInReverse());
            routineEntry.setCountRepetitionsInReverse(selectedWorkout.getCountRepetitionsInReverse());
            routineEntry.setTimeToGetInPosition(selectedWorkout.getTimeToGetInPosition());
            routineEntry.setRestTimeAfterExercise(selectedWorkout.getRestTimeAfterExercise());
            routineEntry.setRoutineId(routine.getRoutineId());

            Hercules.getInstance().getDaoSession().getRoutineEntryDao().insert(routineEntry);
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
            List<Workout> selectedWorkoutList = (List<Workout>) data.getSerializableExtra("selectedWorkouts");
            List<Workout> existingWorkoutList = new ArrayList<>();
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
