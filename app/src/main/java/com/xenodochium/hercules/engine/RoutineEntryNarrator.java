package com.xenodochium.hercules.engine;

import android.app.Activity;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.ui.CircularProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class RoutineEntryNarrator extends UtteranceProgressListener {

    private static RoutineEntryNarrator routineEntryNarrator;
    private final int
            RES_INITIATE_SPEECH = -1,
            RES_GET_IN_POSITION_SPEECH = 0,
            RES_GET_IN_POSITION_TIMER = 1,
            RES_SET_SPEECH = 2,
            RES_SET_TIMER = 3,
            RES_REST_SPEECH = 4,
            RES_REST_TIMER = 5,
            RES_SET_LOOP_CHECK = 6;
    private RoutineEntry routineEntry; //routine entry to narrate
    private TextView textViewRoutineEntrySetNumber;
    private int currentRoutineEntrySetNumber = 0;
    private int currentRoutineEntryStage = -1;
    private Activity activity;
    private CircularProgressBar timerView;
    private TextView timerTextView;

    private RoutineEntryNarrator() {

    }

    /**
     * Maintain only one instance
     *
     * @return
     */
    public static RoutineEntryNarrator getInstance() {
        if (routineEntryNarrator == null) {
            routineEntryNarrator = new RoutineEntryNarrator();
        }
        return routineEntryNarrator;
    }

    /**
     * @param activity
     * @param routineEntry
     * @param timerView
     * @param timerTextView
     */
    public void initiate(Activity activity, RoutineEntry routineEntry, CircularProgressBar timerView, TextView textViewRoutineEntrySetNumber, TextView timerTextView) {
        this.activity = activity;
        this.routineEntry = routineEntry;
        this.timerView = timerView;
        this.timerTextView = timerTextView;
        this.textViewRoutineEntrySetNumber = textViewRoutineEntrySetNumber;
        currentRoutineEntryStage = -1;
        currentRoutineEntrySetNumber = 0;
    }

    @Override
    public void onStart(String s) {

    }

    @Override
    public void onDone(String s) {
        currentRoutineEntryStage++;
        narrate();
    }

    @Override
    public void onError(String s) {

    }

    /**
     *
     */
    public synchronized void narrate() {
        Log.v(Hercules.TAG, "Narrating routing entry " + currentRoutineEntryStage);
        switch (currentRoutineEntryStage) {
            case RES_INITIATE_SPEECH:
                Log.v(Hercules.TAG, "Initiating " + routineEntry.getName());
                HerculesSpeechEngine.speak("Lets start with " + routineEntry.getName(), this);
                break;
            case RES_GET_IN_POSITION_SPEECH:
                Log.v(Hercules.TAG, "Get in position for " + routineEntry.getName() + ". Set " + (currentRoutineEntrySetNumber + 1));
                HerculesSpeechEngine.speak("Beginning set " + (currentRoutineEntrySetNumber + 1) + " for " + routineEntry.getName() + " in " + routineEntry.getTimeToGetInPosition() + " seconds. Get in position.", this);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewRoutineEntrySetNumber.setText("Set " + (currentRoutineEntrySetNumber + 1) + " of " + routineEntry.getStandardNumberOfSets());
                    }
                });
                break;
            case RES_GET_IN_POSITION_TIMER:
                updateCounter(true, routineEntry.getTimeToGetInPosition(), routineEntry.getTimeToGetInPosition(), 1000);
                break;
            case RES_SET_SPEECH:
                Log.v(Hercules.TAG, "Beginning set " + (currentRoutineEntrySetNumber + 1) + " for " + routineEntry.getName());
                HerculesSpeechEngine.speak("Beginning " + routineEntry.getName() + ". " + routineEntry.getStandardNumberOfRepetitions() + " repetitions in " + routineEntry.getDuration() + " seconds", this);
                break;
            case RES_SET_TIMER:
                updateCounter(false, routineEntry.getDuration(), routineEntry.getDuration(), 1000);
                break;
            case RES_REST_SPEECH:
                Log.v(Hercules.TAG, "Rest after " + routineEntry.getName());
                HerculesSpeechEngine.speak("Rest for " + routineEntry.getRestTimeAfterExercise() + " seconds", this);
                break;
            case RES_REST_TIMER:
                updateCounter(true, routineEntry.getRestTimeAfterExercise(), routineEntry.getRestTimeAfterExercise(), 1000);
                break;
            case RES_SET_LOOP_CHECK:
                currentRoutineEntrySetNumber++;
                if (currentRoutineEntrySetNumber < routineEntry.getStandardNumberOfSets()) {
                    currentRoutineEntryStage = RES_GET_IN_POSITION_SPEECH;
                    narrate();
                } else {
                    RoutineOrchestrator.getInstance().next();
                }
                break;
        }
    }

    /**
     * @param seconds
     */
    private synchronized void updateCounter(final boolean mute, final int totalSeconds, final int seconds, final int delay) {
        final int[] timerCount = new int[1];
        timerCount[0] = seconds;
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                Log.v(Hercules.TAG, "Updating timer to " + timerCount[0]);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!RoutineOrchestrator.getInstance().isPlaying()) {
                            timerTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerTextView.setText("--");
                            timerView.setProgressWithAnimation(0, 1000);
                            return;
                        }

                        if (!mute) {
                            timerTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            HerculesSpeechEngine.speak(String.valueOf(timerCount[0]));
                        } else {
                            timerTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                        }
                        timerTextView.setText(String.valueOf(timerCount[0]));
                        timerView.setProgressMax(totalSeconds);
                        timerView.setProgressWithAnimation(timerCount[0], delay);

                        timerCount[0]--;
                        if (timerCount[0] >= 0) {
                            updateCounter(mute, totalSeconds, timerCount[0], delay);
                        } else {
                            currentRoutineEntryStage++;
                            narrate();
                        }
                    }
                });
            }
        }, delay);
    }

    /**
     * @param routineEntry
     */
    private String getNarrationTextForRoutineEntry(RoutineEntry routineEntry, int stage) {
        String routineEntryNarration = routineEntry.getName();
        return routineEntryNarration;
    }

    public RoutineEntry getRoutineEntry() {
        return routineEntry;
    }

    public void setRoutineEntry(RoutineEntry routineEntry) {
        currentRoutineEntrySetNumber = -1;
        this.routineEntry = routineEntry;
    }

}
