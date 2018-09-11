package com.xenodochium.hercules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.MultiSelectArrayAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.Workout;

import java.io.Serializable;
import java.util.List;

public class SelectWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listViewWorkouts;
    Button buttonOk, buttonCancel;
    MultiSelectArrayAdapter multiSelectArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_workout);
        getSupportActionBar().hide();

        initializeViews();
        populateListViewWithWorkouts();
    }

    private void populateListViewWithWorkouts() {
        List<Workout> workouts = Hercules.getInstance().getDaoSession().getWorkoutDao().loadAll();
        multiSelectArrayAdapter = new MultiSelectArrayAdapter(getApplicationContext(), workouts);
        listViewWorkouts.setAdapter(multiSelectArrayAdapter);
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        listViewWorkouts = findViewById(R.id.list_view_workouts);
        buttonCancel = findViewById(R.id.button_cancel);
        buttonOk = findViewById(R.id.button_ok);
        buttonCancel.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent result = new Intent();
        switch (view.getId()) {
            case R.id.button_cancel:
                setResult(RESULT_CANCELED);
                break;
            case R.id.button_ok:
                List<Workout> selectedWorkouts = multiSelectArrayAdapter.getSelectedItems();
                result.putExtra("selectedWorkouts", (Serializable) selectedWorkouts);
                setResult(RESULT_OK, result);
                break;
        }

        finish();
    }
}
