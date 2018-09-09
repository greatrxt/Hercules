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

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.RoutineItem;

import java.util.List;

public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout tilExerciseName, tilNumberOfSets, tilNumberOfRepetitions, tilDuration, tilTimeToGetInPosition, tilRestTimeAfterExercise;
    CheckBox checkboxCountRepetitionsInReverse, checkboxCountDurationInReverse;
    Spinner spinnerBodyPart;
    Button buttonCancel, buttonOk;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().hide();
        initializeViews();
        populateViews();
    }

    /**
     * Fill values if opening existing record
     */
    private void populateViews() {
        if (getIntent().getExtras() != null) {
            RoutineItem routineItem = Hercules.getInstance().getDaoSession().getRoutineItemDao().load((Long) getIntent().getExtras().get("routineItemId"));
            tilExerciseName.getEditText().setText(routineItem.getName());
            tilNumberOfSets.getEditText().setText(String.valueOf(routineItem.getStandardNumberOfSets()));
            tilNumberOfRepetitions.getEditText().setText(String.valueOf(routineItem.getStandardNumberOfRepetitions()));
            tilDuration.getEditText().setText(String.valueOf(routineItem.getDuration()));
            tilTimeToGetInPosition.getEditText().setText(String.valueOf(routineItem.getTimeToGetInPosition()));
            tilRestTimeAfterExercise.getEditText().setText(String.valueOf(routineItem.getRestTimeAfterExercise()));
            checkboxCountRepetitionsInReverse.setChecked(routineItem.getCountDurationInReverse());
            checkboxCountDurationInReverse.setChecked(routineItem.getCountDurationInReverse());
            SpinnerAdapter spinnerAdapter = spinnerBodyPart.getAdapter();
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (((BodyPart) spinnerAdapter.getItem(i)).getBodyPartId() == routineItem.getBodyPartId()) {
                    spinnerBodyPart.setSelection(i);
                }
            }

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

        // Spinner Drop down elements
        List<BodyPart> bodyPartsNameList = Hercules.getInstance().getDaoSession().getBodyPartDao().loadAll();
        // Creating adapter for spinner
        ArrayAdapter<BodyPart> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bodyPartsNameList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
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
        RoutineItem routineItem = new RoutineItem();
        routineItem.setName(tilExerciseName.getEditText().getText().toString());
        routineItem.setStandardNumberOfSets(Integer.parseInt(tilNumberOfSets.getEditText().getText().toString()));
        routineItem.setStandardNumberOfRepetitions(Integer.parseInt(tilNumberOfRepetitions.getEditText().getText().toString()));
        routineItem.setCountRepetitionsInReverse(checkboxCountRepetitionsInReverse.isChecked());
        routineItem.setDuration(Integer.parseInt(tilDuration.getEditText().getText().toString()));
        routineItem.setCountDurationInReverse(checkboxCountDurationInReverse.isChecked());
        routineItem.setTimeToGetInPosition(Integer.parseInt(tilTimeToGetInPosition.getEditText().getText().toString()));
        routineItem.setRestTimeAfterExercise(Integer.parseInt(tilRestTimeAfterExercise.getEditText().getText().toString()));
        routineItem.setBodyPartId(((BodyPart) spinnerBodyPart.getSelectedItem()).getBodyPartId());

        if (getIntent().getExtras() != null) {
            routineItem.setRoutineItemId((Long) getIntent().getExtras().get("routineItemId"));
        }

        Hercules.getInstance().getDaoSession().getRoutineItemDao().insertOrReplace(routineItem);
        Log.d(ExerciseActivity.class.getSimpleName(), "Saved " + routineItem.getRoutineItemId());
        finish();
    }
}
