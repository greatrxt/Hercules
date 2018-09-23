package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.engine.RoutineOrchestratorImpl;
import com.xenodochium.hercules.model.RoutineEntry;

import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButtonPlay, imageButtonForward, imageButtonRewind;
    TextView textViewRoutineName, textViewRoutineEntryName, textViewTimerText, textViewRepetitionsText, textViewRoutineEntrySetNumber,
            textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel,
            textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player);
        initializeViews();
    }


    @Override
    protected void onPause() {
        super.onPause();
        RoutineOrchestratorImpl.getInstance().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoutineOrchestratorImpl.getInstance().pause();
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        getSupportActionBar().hide();
        findViewById(R.id.image_button_slider_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playListFragmentUp();
            }
        });

        textViewRoutineName = findViewById(R.id.text_view_routine_name);
        textViewRoutineEntrySetNumber = findViewById(R.id.text_view_routine_entry_set_number);
        textViewRoutineEntryName = findViewById(R.id.text_view_routine_entry_name);
        textViewTimerText = findViewById(R.id.text_view_timer_text);
        textViewRepetitionsText = findViewById(R.id.text_view_repetitions_text);

        textViewRoutineEntryTtgpLabel = findViewById(R.id.text_view_routine_entry_ttgp_label);
        textViewRoutineEntrySetLabel = findViewById(R.id.text_view_routine_entry_set_label);
        textViewRoutineEntryRestLabel = findViewById(R.id.text_view_routine_entry_rest_label);

        textViewRoutineEntryTtgp = findViewById(R.id.text_view_routine_entry_ttgp);
        textViewRoutineEntrySet = findViewById(R.id.text_view_routine_entry_set);
        textViewRoutineEntryRest = findViewById(R.id.text_view_routine_entry_rest);

        textViewRoutineName.setText(getIntent().getExtras().getString("label"));
        imageButtonPlay = findViewById(R.id.image_button_play);
        imageButtonForward = findViewById(R.id.image_button_forward);
        imageButtonRewind = findViewById(R.id.image_button_rewind);

        imageButtonPlay.setOnClickListener(this);
        imageButtonForward.setOnClickListener(this);
        imageButtonRewind.setOnClickListener(this);

        RoutineOrchestratorImpl.getInstance().initiate(this, (List<RoutineEntry>) getIntent().getExtras().getSerializable("workoutList"),
                (CircularProgressBar) findViewById(R.id.circular_progress_timer),
                (CircularProgressBar) findViewById(R.id.circular_progress_repetitions),
                textViewRoutineEntryName,
                textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel,
                textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest,
                textViewRoutineEntrySetNumber, textViewTimerText, textViewRepetitionsText,
                imageButtonPlay, imageButtonRewind, imageButtonForward);

        RoutineOrchestratorImpl.getInstance().play();
    }

    /**
     * Pulls up the playlist fragment
     */
    public void playListFragmentUp() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_up,
                R.animator.slide_down,
                R.animator.slide_up,
                R.animator.slide_down);
        transaction.addToBackStack(null);
        transaction.replace(R.id.list_fragment_container, PlayListFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_button_play:
                if (RoutineOrchestratorImpl.getInstance().isPlaying()) {
                    RoutineOrchestratorImpl.getInstance().pause();
                } else {
                    RoutineOrchestratorImpl.getInstance().play();
                }
                break;
            case R.id.image_button_forward:
                RoutineOrchestratorImpl.getInstance().next();
                break;
            case R.id.image_button_rewind:
                RoutineOrchestratorImpl.getInstance().previous();
                break;
        }
    }
}
