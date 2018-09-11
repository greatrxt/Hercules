package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.Workout;

import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout tilExerciseName, tilNumberOfSets, tilNumberOfRepetitions, tilDuration, tilTimeToGetInPosition, tilRestTimeAfterExercise;
    CheckBox checkboxCountRepetitionsInReverse, checkboxCountDurationInReverse;
    Spinner spinnerBodyPart;
    Button buttonCancel, buttonOk;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        initializeViews();
        populateViews();
    }

    /**
     * Fill values if opening existing record
     */
    private void populateViews() {
        if (getIntent().getExtras() != null) {
            Workout workout = Hercules.getInstance().getDaoSession().getWorkoutDao().load((Long) getIntent().getExtras().get("workoutId"));
            tilExerciseName.getEditText().setText(workout.getName());
            tilNumberOfSets.getEditText().setText(String.valueOf(workout.getStandardNumberOfSets()));
            tilNumberOfRepetitions.getEditText().setText(String.valueOf(workout.getStandardNumberOfRepetitions()));
            tilDuration.getEditText().setText(String.valueOf(workout.getDuration()));
            tilTimeToGetInPosition.getEditText().setText(String.valueOf(workout.getTimeToGetInPosition()));
            tilRestTimeAfterExercise.getEditText().setText(String.valueOf(workout.getRestTimeAfterExercise()));
            checkboxCountRepetitionsInReverse.setChecked(workout.getCountDurationInReverse());
            checkboxCountDurationInReverse.setChecked(workout.getCountDurationInReverse());
            SpinnerAdapter spinnerAdapter = spinnerBodyPart.getAdapter();
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (((BodyPart) spinnerAdapter.getItem(i)).getBodyPartId() == workout.getBodyPartId()) {
                    spinnerBodyPart.setSelection(i);
                }
            }

            ((TextView) findViewById(R.id.text_view_label_workout)).setText(getString(R.string.edit_workout));

            //don't show keyboard
            getWindow().setSoftInputMode(WindowManager.
                    LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        tilExerciseName = findViewById(R.id.til_exercise_name);
        tilNumberOfSets = findViewById(R.id.til_number_of_sets);
        tilNumberOfRepetitions = findViewById(R.id.til_repetitions);
        tilDuration = findViewById(R.id.til_duration);
        tilTimeToGetInPosition = findViewById(R.id.til_time_to_get_in_position);
        tilRestTimeAfterExercise = findViewById(R.id.til_rest_time_after_exercise);

        checkboxCountDurationInReverse = findViewById(R.id.checkbox_count_duration_in_reverse);
        checkboxCountRepetitionsInReverse = findViewById(R.id.checkbox_count_repetitions_in_reverse);

        spinnerBodyPart = findViewById(R.id.spinner_body_part);

        buttonCancel = findViewById(R.id.button_cancel);
        buttonOk = findViewById(R.id.button_ok);

        buttonCancel.setOnClickListener(this);
        buttonOk.setOnClickListener(this);

        List<BodyPart> bodyPartsNameList = Hercules.getInstance().getDaoSession().getBodyPartDao().loadAll();
        ArrayAdapter<BodyPart> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bodyPartsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBodyPart.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_ok:
                if (formDataIsValid()) {
                    saveExerciseData();
                }
                break;
        }
    }

    /**
     * Check if data entered by user is valid
     *
     * @return
     */
    private boolean formDataIsValid() {
        boolean formDataIsValid = true;
        if (TextUtils.isEmpty(tilExerciseName.getEditText().getText())) {
            tilExerciseName.getEditText().setError(getResources().getString(R.string.cannot_be_empty));
            formDataIsValid = false;
        }

        if (TextUtils.isEmpty(tilDuration.getEditText().getText())) {
            tilDuration.getEditText().setError(getResources().getString(R.string.cannot_be_empty));
            formDataIsValid = false;
        }

        return formDataIsValid;
    }

    /**
     * Save exercise data
     */
    private void saveExerciseData() {
        Workout workout = new Workout();
        workout.setName(tilExerciseName.getEditText().getText().toString());
        workout.setStandardNumberOfSets(Integer.parseInt(tilNumberOfSets.getEditText().getText().toString()));
        workout.setStandardNumberOfRepetitions(Integer.parseInt(tilNumberOfRepetitions.getEditText().getText().toString()));
        workout.setCountRepetitionsInReverse(checkboxCountRepetitionsInReverse.isChecked());
        workout.setDuration(Integer.parseInt(tilDuration.getEditText().getText().toString()));
        workout.setCountDurationInReverse(checkboxCountDurationInReverse.isChecked());
        workout.setTimeToGetInPosition(Integer.parseInt(tilTimeToGetInPosition.getEditText().getText().toString()));
        workout.setRestTimeAfterExercise(Integer.parseInt(tilRestTimeAfterExercise.getEditText().getText().toString()));
        workout.setBodyPartId(((BodyPart) spinnerBodyPart.getSelectedItem()).getBodyPartId());

        if (getIntent().getExtras() != null) {
            workout.setWorkoutId((Long) getIntent().getExtras().get("workoutId"));
        }

        Hercules.getInstance().getDaoSession().getWorkoutDao().insertOrReplace(workout);
        Log.d(WorkoutActivity.class.getSimpleName(), "Saved " + workout.getWorkoutId());
        finish();
    }
}
