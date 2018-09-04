package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xenodochium.hercules.R;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().hide();
    }
}
