package com.xenodochium.hercules.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.engine.RoutineEntryNarratorImpl;
import com.xenodochium.hercules.engine.RoutineOrchestratorImpl;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.service.OnClearFromRecentService;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButtonPlay, imageButtonForward, imageButtonRewind, imageButtonPreviousSet, imageButtonNextSet, imageButtonLoop, imageButtonHelp;
    private TextView textViewRoutineName, textViewRoutineEntryName, textViewTimerText, textViewRepetitionsText, textViewRoutineEntrySetNumber,
            textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel,
            textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest;

    private LinearLayout linearLayoutTtgp, linearLayoutSet, linearLayoutRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() == null) {
            onDestroy();
            return;
        }
        setContentView(R.layout.activity_player);
        initializeViews();
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
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

        linearLayoutTtgp = findViewById(R.id.layout_ttgp);
        linearLayoutSet = findViewById(R.id.layout_set);
        linearLayoutRest = findViewById(R.id.layout_rest);

        linearLayoutTtgp.setOnClickListener(this);
        linearLayoutSet.setOnClickListener(this);
        linearLayoutRest.setOnClickListener(this);

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

        imageButtonPreviousSet = findViewById(R.id.image_button_previous_set);
        imageButtonNextSet = findViewById(R.id.image_button_next_set);
        imageButtonLoop = findViewById(R.id.image_button_loop);
        imageButtonHelp = findViewById(R.id.button_help_player);

        imageButtonPlay.setOnClickListener(this);
        imageButtonForward.setOnClickListener(this);
        imageButtonRewind.setOnClickListener(this);

        imageButtonPreviousSet.setOnClickListener(this);
        imageButtonNextSet.setOnClickListener(this);
        imageButtonLoop.setOnClickListener(this);

        imageButtonHelp.setOnClickListener(this);

        textViewRoutineEntryName.setOnClickListener(this);
        RoutineOrchestratorImpl.getInstance().initiate(this, (List<RoutineEntry>) getIntent().getExtras().getSerializable("workoutList"),
                (CircularProgressBar) findViewById(R.id.circular_progress_timer),
                (CircularProgressBar) findViewById(R.id.circular_progress_repetitions),
                textViewRoutineEntryName,
                textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel,
                textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest,
                textViewRoutineEntrySetNumber, textViewTimerText, textViewRepetitionsText,
                imageButtonPlay, imageButtonRewind, imageButtonForward, imageButtonPreviousSet, imageButtonNextSet, imageButtonLoop);

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
            case R.id.button_help_player:
                RoutineOrchestratorImpl.getInstance().pause();

                new ShowcaseHelper.PlayerHelperBuilder(PlayerActivity.this,
                        findViewById(R.id.layout_info),
                        findViewById(R.id.layout_repetitions_counter),
                        findViewById(R.id.layout_seconds_counter),
                        findViewById(R.id.layout_controls),
                        findViewById(R.id.location_image_button_slider_toggle))
                        .startShowcase(System.currentTimeMillis() + "_layoutInfo");
                break;
            case R.id.text_view_routine_entry_name:
                playListFragmentUp();
                break;
            case R.id.layout_ttgp:
                if (RoutineEntryNarratorImpl.getInstance().getCurrentRoutineEntryStage() != RoutineEntryNarratorImpl.RES_GET_IN_POSITION_SPEECH
                        && RoutineEntryNarratorImpl.getInstance().getCurrentRoutineEntryStage() != RoutineEntryNarratorImpl.RES_GET_IN_POSITION_TIMER) {
                    RoutineEntryNarratorImpl.getInstance().changeCurrentRoutineEntryStage(RoutineEntryNarratorImpl.RES_GET_IN_POSITION_SPEECH);
                }
                break;
            case R.id.layout_set:
                if (RoutineEntryNarratorImpl.getInstance().getCurrentRoutineEntryStage() != RoutineEntryNarratorImpl.RES_SET_SPEECH
                        && RoutineEntryNarratorImpl.getInstance().getCurrentRoutineEntryStage() != RoutineEntryNarratorImpl.RES_SET_TIMER) {
                    RoutineEntryNarratorImpl.getInstance().changeCurrentRoutineEntryStage(RoutineEntryNarratorImpl.RES_SET_SPEECH);
                }
                break;
            case R.id.layout_rest:
                if (RoutineEntryNarratorImpl.getInstance().getCurrentRoutineEntryStage() != RoutineEntryNarratorImpl.RES_REST_SPEECH
                        && RoutineEntryNarratorImpl.getInstance().getCurrentRoutineEntryStage() != RoutineEntryNarratorImpl.RES_REST_TIMER) {
                    RoutineEntryNarratorImpl.getInstance().changeCurrentRoutineEntryStage(RoutineEntryNarratorImpl.RES_REST_SPEECH);
                }
                break;
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
            case R.id.image_button_previous_set:
                RoutineEntryNarratorImpl.getInstance().previousSet();
                break;
            case R.id.image_button_next_set:
                RoutineEntryNarratorImpl.getInstance().nextSet();
                break;
            case R.id.image_button_loop:
                RoutineOrchestratorImpl.getInstance().loop();
                break;

        }
    }
}
