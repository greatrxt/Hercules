package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.engine.RoutineOrchestrator;
import com.xenodochium.hercules.model.RoutineEntry;

import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButtonPlay, imageButtonForward, imageButtonRewind;
    TextView textViewRoutineName, textViewRoutineEntryName, textViewTimerText, textViewRoutineEntrySetNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initializeViews();
    }


    @Override
    protected void onPause() {
        super.onPause();
        RoutineOrchestrator.getInstance().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoutineOrchestrator.getInstance().pause();
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

        textViewRoutineName.setText(String.valueOf(Hercules.getInstance().getDaoSession().getRoutineDao().load(getIntent().getExtras().getLong("routineId")).getName()));
        imageButtonPlay = findViewById(R.id.image_button_play);
        imageButtonForward = findViewById(R.id.image_button_forward);
        imageButtonRewind = findViewById(R.id.image_button_rewind);

        imageButtonPlay.setOnClickListener(this);
        imageButtonForward.setOnClickListener(this);
        imageButtonRewind.setOnClickListener(this);

        RoutineOrchestrator.getInstance().initiate(this, (List<RoutineEntry>) getIntent().getExtras().getSerializable("workoutList"),
                (CircularProgressBar) findViewById(R.id.circular_progress_repetitions), textViewRoutineEntryName, textViewRoutineEntrySetNumber, textViewTimerText,
                imageButtonPlay, imageButtonRewind, imageButtonForward);
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
                if (RoutineOrchestrator.getInstance().isPlaying()) {
                    RoutineOrchestrator.getInstance().pause();
                    imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_play_icon));
                } else {
                    RoutineOrchestrator.getInstance().play();

                }
                break;
            case R.id.image_button_forward:
                RoutineOrchestrator.getInstance().next();
                break;
            case R.id.image_button_rewind:
                RoutineOrchestrator.getInstance().previous();
                break;
        }
    }
}
