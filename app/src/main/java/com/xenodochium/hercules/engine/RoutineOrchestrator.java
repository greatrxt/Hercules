package com.xenodochium.hercules.engine;

import android.app.Activity;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.ui.CircularProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Player engine to play all routine entries
 */
public class RoutineOrchestrator extends UtteranceProgressListener {
    private static RoutineOrchestrator routineOrchestrator;
    public List<String> initiation;
    TextView textViewRoutineEntryName, textViewTimerText, textViewRoutineEntrySetNumber;
    private int currentlyPlayingRoutineEntryNumber = -1;
    private List<RoutineEntry> routineEntryPlayList;
    private boolean isPlaying = false;
    private CircularProgressBar timerView;
    private Activity activity;

    private ImageButton imageButtonPlay, imageButtonForward, imageButtonRewind;

    private RoutineOrchestrator() {
        routineEntryPlayList = new ArrayList<>();
        initiation = Arrays.asList(Hercules.getInstance().getResources().getStringArray(R.array.player_initiation));
    }

    public static RoutineOrchestrator getInstance() {
        if (routineOrchestrator == null) {
            routineOrchestrator = new RoutineOrchestrator();
        }

        return routineOrchestrator;
    }

    public void initiate(Activity activity, List<RoutineEntry> routineEntryPlayList,
                         CircularProgressBar timerView,
                         TextView textViewRoutineEntryName,
                         TextView textViewRoutineEntrySetNumber,
                         TextView textViewTimerText,
                         ImageButton imageButtonPlay,
                         ImageButton imageButtonRewind,
                         ImageButton imageButtonForward) {
        this.activity = activity;
        this.routineEntryPlayList = routineEntryPlayList;
        this.timerView = timerView;
        this.textViewRoutineEntryName = textViewRoutineEntryName;
        this.textViewRoutineEntrySetNumber = textViewRoutineEntrySetNumber;
        this.textViewTimerText = textViewTimerText;
        this.imageButtonPlay = imageButtonPlay;
        this.imageButtonForward = imageButtonForward;
        this.imageButtonRewind = imageButtonRewind;
    }

    @Override
    public void onStart(String s) {

    }

    @Override
    public void onDone(String s) {
        next();
    }

    @Override
    public void onError(String s) {

    }

    /**
     * Play previous
     */
    public void previous() {
        if (currentlyPlayingRoutineEntryNumber > -1) {
            currentlyPlayingRoutineEntryNumber--;
            play();
        }
    }

    /**
     * Play next
     */
    public void next() {
        if (currentlyPlayingRoutineEntryNumber < routineEntryPlayList.size()) {
            currentlyPlayingRoutineEntryNumber++;
            play();
        }
    }

    /**
     * Start playing
     */
    public void play() {
        Log.v(Hercules.TAG, "Playing routine");
        isPlaying = true;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageButtonPlay.setImageDrawable(activity.getDrawable(R.drawable.ic_pause_icon));
            }
        });

        if (currentlyPlayingRoutineEntryNumber == -1) {
            Random random = new Random();
            Log.v(Hercules.TAG, "Initiating");
            HerculesSpeechEngine.speak(initiation.get(random.nextInt(initiation.size())), this);
        } else if (currentlyPlayingRoutineEntryNumber < routineEntryPlayList.size()) {
            Log.v(Hercules.TAG, "Playing routine entry " + currentlyPlayingRoutineEntryNumber);
            textViewRoutineEntryName.setText(String.valueOf(routineEntryPlayList.get(currentlyPlayingRoutineEntryNumber).getName()));
            RoutineEntryNarrator.getInstance().initiate(activity, routineEntryPlayList.get(currentlyPlayingRoutineEntryNumber), timerView, textViewRoutineEntrySetNumber, textViewTimerText);
            RoutineEntryNarrator.getInstance().narrate();
        } else {
            HerculesSpeechEngine.speak("Routine over");
        }
    }

    public void pause() {
        isPlaying = false;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageButtonPlay.setImageDrawable(activity.getDrawable(R.drawable.ic_play_icon));
            }
        });
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public List<RoutineEntry> getRoutineEntryPlayList() {
        return routineEntryPlayList;
    }
}
