package com.xenodochium.hercules.engine;

import android.app.Activity;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.ui.CircularProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class RoutineEntryNarratorImpl extends UtteranceProgressListener {

    private static RoutineEntryNarratorImpl routineEntryNarratorImpl;
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
    private TextView textViewRoutineEntrySetNumber, textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest,
            textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel;
    private int currentRoutineEntrySetNumber = 0;
    private int currentRoutineEntryStage = -1;
    private Activity activity;
    private CircularProgressBar timerView, repetionsView;
    private TextView timerTextView, repetionsTextView;

    private boolean narrateSeconds = false;

    private Timer repetitionsTimer, durationTimer;

    private RoutineEntryNarratorImpl() {

    }

    /**
     * Maintain only one instance
     *
     * @return
     */
    public static RoutineEntryNarratorImpl getInstance() {
        if (routineEntryNarratorImpl == null) {
            routineEntryNarratorImpl = new RoutineEntryNarratorImpl();
        }

        return routineEntryNarratorImpl;
    }

    /**
     * @param activity
     * @param routineEntry
     * @param timerView
     * @param timerTextView
     */
    public synchronized void initiate(final Activity activity, final RoutineEntry routineEntry, final CircularProgressBar timerView, final CircularProgressBar repetionsView,
                                      final TextView textViewRoutineEntryTtgpLabel, final TextView textViewRoutineEntrySetLabel, final TextView textViewRoutineEntryRestLabel,
                                      final TextView textViewRoutineEntryTtgp, final TextView textViewRoutineEntrySet, final TextView textViewRoutineEntryRest,
                                      final TextView textViewRoutineEntrySetNumber, final TextView timerTextView, final TextView repetionsTextView) {

        if (repetitionsTimer != null)
            repetitionsTimer.cancel();

        if (durationTimer != null)
            durationTimer.cancel();

        RoutineEntryNarratorImpl.this.activity = activity;
        RoutineEntryNarratorImpl.this.routineEntry = routineEntry;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                RoutineEntryNarratorImpl.this.timerView = timerView;
                RoutineEntryNarratorImpl.this.repetionsView = repetionsView;

                RoutineEntryNarratorImpl.this.textViewRoutineEntryTtgpLabel = textViewRoutineEntryTtgpLabel;
                RoutineEntryNarratorImpl.this.textViewRoutineEntrySetLabel = textViewRoutineEntrySetLabel;
                RoutineEntryNarratorImpl.this.textViewRoutineEntryRestLabel = textViewRoutineEntryRestLabel;
                RoutineEntryNarratorImpl.this.textViewRoutineEntryTtgp = textViewRoutineEntryTtgp;
                RoutineEntryNarratorImpl.this.textViewRoutineEntrySet = textViewRoutineEntrySet;
                RoutineEntryNarratorImpl.this.textViewRoutineEntryRest = textViewRoutineEntryRest;

                RoutineEntryNarratorImpl.this.timerTextView = timerTextView;
                RoutineEntryNarratorImpl.this.repetionsTextView = repetionsTextView;
                RoutineEntryNarratorImpl.this.textViewRoutineEntrySetNumber = textViewRoutineEntrySetNumber;

                repetionsTextView.setText("--");
                repetionsView.setProgressWithAnimation(0, 1000);

                timerTextView.setText("--");
                timerView.setProgressWithAnimation(0, 1000);

                currentRoutineEntryStage = -1;
                currentRoutineEntrySetNumber = 0;

                if (routineEntry.getTimeToGetInPosition() > 0) {
                    textViewRoutineEntryTtgpLabel.setText(activity.getString(R.string.get_in_position));
                    textViewRoutineEntryTtgp.setText(routineEntry.getTimeToGetInPosition() + " Seconds");
                } else {
                    textViewRoutineEntryTtgpLabel.setText("");
                    textViewRoutineEntryTtgp.setText("");
                }

                if (routineEntry.getStandardNumberOfRepetitions() > 0) {
                    textViewRoutineEntrySetLabel.setText(activity.getString(R.string.set));
                    textViewRoutineEntrySet.setText(routineEntry.getStandardNumberOfRepetitions() + " Reps in " + routineEntry.getDuration() + " Seconds");
                } else {

                    if (routineEntry.getDuration() > 0) {
                        textViewRoutineEntrySetLabel.setText(activity.getString(R.string.duration));
                        textViewRoutineEntrySet.setText(routineEntry.getDuration() + " Seconds");
                    } else {
                        textViewRoutineEntrySetLabel.setText("");
                        textViewRoutineEntrySet.setText("");
                    }
                }

                if (routineEntry.getRestTimeAfterExercise() > 0) {
                    textViewRoutineEntryRestLabel.setText(activity.getString(R.string.rest_time));
                    textViewRoutineEntryRest.setText(routineEntry.getRestTimeAfterExercise() + " Seconds");
                } else {
                    textViewRoutineEntryRestLabel.setText("");
                    textViewRoutineEntryRest.setText("");
                }

                //if repettions is zero, count seconds
                narrateSeconds = routineEntry.getStandardNumberOfRepetitions() == 0;

                if (narrateSeconds) {
                    timerView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                    repetionsView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    timerView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                    repetionsView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                }

                timerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timerView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                        repetionsView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                        Toast.makeText(activity, activity.getResources().getString(R.string.counting_duration), Toast.LENGTH_SHORT).show();
                        narrateSeconds = true;
                    }
                });

                repetionsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timerView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                        repetionsView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                        Toast.makeText(activity, activity.getResources().getString(R.string.counting_repetitions), Toast.LENGTH_SHORT).show();
                        narrateSeconds = false;
                    }
                });
            }
        });
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

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewRoutineEntryTtgpLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                textViewRoutineEntryTtgp.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));

                textViewRoutineEntrySetLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                textViewRoutineEntrySet.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));

                textViewRoutineEntryRestLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                textViewRoutineEntryRest.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));

                textViewRoutineEntryTtgpLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info));
                textViewRoutineEntryTtgp.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info));

                textViewRoutineEntrySetLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info));
                textViewRoutineEntrySet.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info));

                textViewRoutineEntryRestLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info));
                textViewRoutineEntryRest.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info));

            }
        });

        switch (currentRoutineEntryStage) {
            case RES_INITIATE_SPEECH:
                Log.v(Hercules.TAG, "Initiating " + routineEntry.getName());
                HerculesSpeechEngine.speak("Lets start with " + routineEntry.getName(), this);
                break;
            case RES_GET_IN_POSITION_SPEECH:

                if (routineEntry.getTimeToGetInPosition() > 0) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewRoutineEntryTtgpLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                            textViewRoutineEntryTtgp.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                            textViewRoutineEntryTtgpLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                            textViewRoutineEntryTtgp.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                        }
                    });

                    HerculesSpeechEngine.speak("Beginning set " + (currentRoutineEntrySetNumber + 1) + " for " + routineEntry.getName() + " in " + routineEntry.getTimeToGetInPosition() + " seconds. Get in position.", this);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (routineEntry.getStandardNumberOfSets() > 0) {
                                textViewRoutineEntrySetNumber.setText("Set " + (currentRoutineEntrySetNumber + 1) + " of " + routineEntry.getStandardNumberOfSets());
                            } else {
                                textViewRoutineEntrySetNumber.setText("");
                            }
                        }
                    });
                } else {
                    currentRoutineEntryStage = RES_SET_SPEECH;
                    narrate();
                }
                break;
            case RES_GET_IN_POSITION_TIMER:

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewRoutineEntryTtgpLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                        textViewRoutineEntryTtgp.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                        textViewRoutineEntryTtgpLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                        textViewRoutineEntryTtgp.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));

                    }
                });

                updateSecondsCounter(true, routineEntry.getTimeToGetInPosition(), routineEntry.getTimeToGetInPosition(), 1000);
                break;
            case RES_SET_SPEECH:

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewRoutineEntrySetLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                        textViewRoutineEntrySet.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                        textViewRoutineEntrySetLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                        textViewRoutineEntrySet.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                    }
                });

                HerculesSpeechEngine.speak("Beginning " + routineEntry.getName() + ". " + routineEntry.getStandardNumberOfRepetitions() + " repetitions in " + routineEntry.getDuration() + " seconds", this);
                break;
            case RES_SET_TIMER:

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewRoutineEntrySetLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                        textViewRoutineEntrySet.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                        textViewRoutineEntrySetLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                        textViewRoutineEntrySet.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                    }
                });

                updateSecondsCounter(false, routineEntry.getDuration(), routineEntry.getDuration(), 1000);
                updateRepetitionsCounter(false, routineEntry.getStandardNumberOfRepetitions(), routineEntry.getStandardNumberOfRepetitions(), (int) ((double) routineEntry.getDuration() / routineEntry.getStandardNumberOfRepetitions() * 1000));
                break;
            case RES_REST_SPEECH:

                if (routineEntry.getRestTimeAfterExercise() > 0) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewRoutineEntryRestLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                            textViewRoutineEntryRest.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                            textViewRoutineEntryRestLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                            textViewRoutineEntryRest.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                        }
                    });

                    HerculesSpeechEngine.speak("Rest for " + routineEntry.getRestTimeAfterExercise() + " seconds", this);
                } else {
                    currentRoutineEntryStage = RES_SET_LOOP_CHECK;
                    narrate();
                }
                break;
            case RES_REST_TIMER:

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewRoutineEntryRestLabel.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                        textViewRoutineEntryRest.setTextColor(activity.getResources().getColor(R.color.colorPrimary));

                        textViewRoutineEntryRestLabel.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                        textViewRoutineEntryRest.setTextSize(activity.getResources().getDimension(R.dimen.player_activity_info_large));
                    }
                });

                updateSecondsCounter(true, routineEntry.getRestTimeAfterExercise(), routineEntry.getRestTimeAfterExercise(), 1000);
                break;
            case RES_SET_LOOP_CHECK:
                currentRoutineEntrySetNumber++;
                if (currentRoutineEntrySetNumber < routineEntry.getStandardNumberOfSets()) {
                    currentRoutineEntryStage = RES_GET_IN_POSITION_SPEECH;
                    narrate();
                } else {
                    RoutineOrchestratorImpl.getInstance().next();
                }
                break;
        }
    }

    /**
     * @param mute
     * @param totalRepetitions
     * @param currentRepetition
     * @param delay
     */
    private synchronized void updateRepetitionsCounter(final boolean mute, final int totalRepetitions, final int currentRepetition, final int delay) {
        final int[] timerCount = new int[1];
        timerCount[0] = currentRepetition;

        if (repetitionsTimer != null)
            repetitionsTimer.cancel();

        repetitionsTimer = new Timer();
        repetitionsTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                Log.v(Hercules.TAG, "Updating repetitions to " + timerCount[0]);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!RoutineOrchestratorImpl.getInstance().isPlaying()) {
                            repetionsTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            repetionsView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            repetionsTextView.setText("--");
                            repetionsView.setProgressWithAnimation(0, 1000);
                            return;
                        }

                        if (!mute && !narrateSeconds) {
                            repetionsTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            repetionsView.setColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            HerculesSpeechEngine.skippableSpeak(String.valueOf(timerCount[0]));
                        } else {
                            repetionsTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            repetionsView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                        }
                        repetionsTextView.setText(String.valueOf(timerCount[0] + "\nReps"));
                        repetionsView.setProgressMax(totalRepetitions);
                        repetionsView.setProgressWithAnimation(timerCount[0], delay);

                        timerCount[0]--;
                        if (timerCount[0] > 0) {
                            updateRepetitionsCounter(mute, totalRepetitions, timerCount[0], delay);
                        } else {
                            repetionsTextView.setText("--");
                            repetionsView.setProgressWithAnimation(0, delay);
                        }
                    }
                });
            }
        }, delay);
    }


    /**
     * @param mute
     * @param totalSeconds
     * @param seconds
     * @param delay
     */
    private synchronized void updateSecondsCounter(final boolean mute, final int totalSeconds, final int seconds, final int delay) {
        final int[] timerCount = new int[1];
        timerCount[0] = seconds;
        if (durationTimer != null)
            durationTimer.cancel();

        durationTimer = new Timer();
        durationTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                Log.v(Hercules.TAG, "Updating timer to " + timerCount[0]);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!RoutineOrchestratorImpl.getInstance().isPlaying()) {
                            timerTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerTextView.setText("--");
                            timerView.setProgressWithAnimation(0, 1000);
                            return;
                        }

                        if (!mute && narrateSeconds) {
                            timerTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            HerculesSpeechEngine.skippableSpeak(String.valueOf(timerCount[0]));
                        } else {
                            timerTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                        }
                        timerTextView.setText(String.valueOf(timerCount[0] + "\nSeconds"));
                        timerView.setProgressMax(totalSeconds);
                        timerView.setProgressWithAnimation(timerCount[0], delay);

                        timerCount[0]--;
                        if (timerCount[0] >= 0) {
                            updateSecondsCounter(mute, totalSeconds, timerCount[0], delay);
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
}
