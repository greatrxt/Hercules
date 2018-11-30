package com.xenodochium.hercules.engine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.ui.CircularProgressBar;
import com.xenodochium.hercules.ui.PlayerActivity;

import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class RoutineEntryNarratorImpl extends UtteranceProgressListener {

    private Toast userToast;
    private static RoutineEntryNarratorImpl routineEntryNarratorImpl;
    public static final int
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
    private ImageButton imageButtonPreviousSet, imageButtonNextSet;
    private int currentRoutineEntrySetNumber = 0;
    private int currentRoutineEntryStage = -1;
    private Activity activity;
    private CircularProgressBar timerView, repetitionsView;
    private TextView textViewTimerText, textViewRepetitionsText;

    private boolean narrateSeconds = false;

    private Timer repetitionsTimer, durationTimer;

    private RoutineEntryNarratorImpl() {
        registerBroadCastReceiver();
    }


    /**
     * Set routine entry. Reset set number to first one.
     *
     * @param routineEntry
     */
    public void setRoutineEntry(RoutineEntry routineEntry) {
        this.routineEntry = routineEntry;
        initiate(activity, routineEntry, timerView, repetitionsView,
                textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel,
                textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest,
                textViewRoutineEntrySetNumber, textViewTimerText, textViewRepetitionsText, imageButtonPreviousSet, imageButtonNextSet);
    }

    @SuppressLint("NewApi")
    public void setNotification() {

        NotificationManager notificationManager = (NotificationManager) Hercules.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews notificationView = new RemoteViews(Hercules.getInstance().getPackageName(), R.layout.notification_playback_controller);
        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(Hercules.getInstance(), PlayerActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(Hercules.getInstance(), 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, "default");
        builder.setCustomBigContentView(notificationView);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingNotificationIntent);
        //builder.setStyle(new NotificationCompat.MediaStyle());
        builder.setOngoing(true);

        //this is the intent that is supposed to be called when the button is clicked
        Intent playIntent = new Intent("com.xenodochium.hercules.ACTION_PLAY");
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(Hercules.getInstance(), 100, playIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.image_button_play, pendingPlayIntent);
        if (RoutineOrchestratorImpl.getInstance().isPlaying()) {
            notificationView.setImageViewResource(R.id.image_button_play, R.drawable.ic_pause_icon_gray_trim_15p_padding);
        } else {
            notificationView.setImageViewResource(R.id.image_button_play, R.drawable.ic_play_icon_gray_trim_15p_padding);
        }

        Intent nextItemIntent = new Intent("com.xenodochium.hercules.ACTION_NEXT");
        PendingIntent pendingNextItemIntent = PendingIntent.getBroadcast(Hercules.getInstance(), 101, nextItemIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.image_button_forward, pendingNextItemIntent);

        Intent previousItemIntent = new Intent("com.xenodochium.hercules.ACTION_PREVIOUS");
        PendingIntent pendingPreviousItemIntent = PendingIntent.getBroadcast(Hercules.getInstance(), 102, previousItemIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.image_button_rewind, pendingPreviousItemIntent);

        notificationView.setTextViewText(R.id.text_view_routine_entry_name, routineEntry.getName());
        //notificationManager.notify(1, notification);
        notificationManager.notify(1, builder.build());
    }

    /**
     *
     */
    private void registerBroadCastReceiver() {

        //register broadcast receiver
        NotificationControlListener broadcastReceiver = new NotificationControlListener();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        // set the custom action
        intentFilter.addAction("com.xenodochium.hercules.ACTION_PLAY");
        intentFilter.addAction("com.xenodochium.hercules.ACTION_PREVIOUS");
        intentFilter.addAction("com.xenodochium.hercules.ACTION_NEXT");
        // register the receiver
        Hercules.getInstance().registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * @param activity
     * @param routineEntry
     * @param timerView
     * @param timerTextView
     * @param imageButtonPreviousSet
     * @param imageButtonNextSet
     */
    public synchronized void initiate(final Activity activity, final RoutineEntry routineEntry, final CircularProgressBar timerView, final CircularProgressBar repetionsView,
                                      final TextView textViewRoutineEntryTtgpLabel, final TextView textViewRoutineEntrySetLabel, final TextView textViewRoutineEntryRestLabel,
                                      final TextView textViewRoutineEntryTtgp, final TextView textViewRoutineEntrySet, final TextView textViewRoutineEntryRest,
                                      final TextView textViewRoutineEntrySetNumber, final TextView timerTextView, final TextView repetionsTextView,
                                      final ImageButton imageButtonPreviousSet, final ImageButton imageButtonNextSet) {


        RoutineEntryNarratorImpl.this.activity = activity;
        RoutineEntryNarratorImpl.this.routineEntry = routineEntry;

        resetTimers();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                setNotification();

                RoutineEntryNarratorImpl.this.timerView = timerView;
                RoutineEntryNarratorImpl.this.repetitionsView = repetionsView;

                RoutineEntryNarratorImpl.this.textViewRoutineEntryTtgpLabel = textViewRoutineEntryTtgpLabel;
                RoutineEntryNarratorImpl.this.textViewRoutineEntrySetLabel = textViewRoutineEntrySetLabel;
                RoutineEntryNarratorImpl.this.textViewRoutineEntryRestLabel = textViewRoutineEntryRestLabel;
                RoutineEntryNarratorImpl.this.textViewRoutineEntryTtgp = textViewRoutineEntryTtgp;
                RoutineEntryNarratorImpl.this.textViewRoutineEntrySet = textViewRoutineEntrySet;
                RoutineEntryNarratorImpl.this.textViewRoutineEntryRest = textViewRoutineEntryRest;

                RoutineEntryNarratorImpl.this.textViewTimerText = timerTextView;
                RoutineEntryNarratorImpl.this.textViewRepetitionsText = repetionsTextView;
                RoutineEntryNarratorImpl.this.textViewRoutineEntrySetNumber = textViewRoutineEntrySetNumber;

                RoutineEntryNarratorImpl.this.imageButtonPreviousSet = imageButtonPreviousSet;
                RoutineEntryNarratorImpl.this.imageButtonNextSet = imageButtonNextSet;

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
                        if (userToast != null) {
                            userToast.cancel();
                        }
                        userToast = Toast.makeText(activity, activity.getResources().getString(R.string.counting_duration), Toast.LENGTH_SHORT);
                        userToast.show();
                        narrateSeconds = true;
                    }
                });

                repetionsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timerView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                        repetionsView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                        if (userToast != null) {
                            userToast.cancel();
                        }
                        userToast = Toast.makeText(activity, activity.getResources().getString(R.string.counting_repetitions), Toast.LENGTH_SHORT);
                        userToast.show();
                        narrateSeconds = false;
                    }
                });

                resetInfoLayout();
            }
        });
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
     * Reset all timers
     */
    private void resetTimers() {
        if (repetitionsTimer != null)
            repetitionsTimer.cancel();

        if (durationTimer != null)
            durationTimer.cancel();

        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (textViewRepetitionsText != null) textViewRepetitionsText.setText("--");
                    if (repetitionsView != null) repetitionsView.setProgressWithAnimation(0, 1000);

                    if (textViewTimerText != null) textViewTimerText.setText("--");
                    if (timerView != null) timerView.setProgressWithAnimation(0, 1000);
                }
            });
        }
    }

    /**
     * Stop speech
     */
    private void resetSpeechEngine() {
        HerculesSpeechEngine.stopSpeaking();
    }

    /**
     * Narrate routine entry
     */
    public synchronized void narrate() {
        Log.v(Hercules.TAG, "Narrating routing entry " + currentRoutineEntryStage);

        if (!RoutineOrchestratorImpl.getInstance().isPlaying())
            return;

        resetInfoLayout();
        setNotification();

        switch (currentRoutineEntryStage) {
            case RES_INITIATE_SPEECH:
                if (routineEntry.getRoutineEntryType().equals(RoutineEntry.RoutineEntryType.BREAK)) {
                    onDone(null);   //no need to speak about break
                } else {
                    HerculesSpeechEngine.speak("Lets start with " + routineEntry.getName(), this);
                }
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

                if (routineEntry.getRoutineEntryType().equals(RoutineEntry.RoutineEntryType.BREAK)) {
                    HerculesSpeechEngine.speak("Taking a break for " + routineEntry.getDuration() + " seconds", this);
                } else {
                    if (routineEntry.getStandardNumberOfRepetitions() > 0) {
                        HerculesSpeechEngine.speak("Beginning " + routineEntry.getName() + ". " + routineEntry.getStandardNumberOfRepetitions() + " repetitions in " + routineEntry.getDuration() + " seconds", this);
                    } else {
                        HerculesSpeechEngine.speak("Beginning " + routineEntry.getName() + ". For " + routineEntry.getDuration() + " seconds", this);
                    }
                }
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

        resetSetDecrementIncrementButtons();
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
     * Change stage
     *
     * @param currentRoutineEntryStage
     */
    public void changeCurrentRoutineEntryStage(int currentRoutineEntryStage) {
        if (this.currentRoutineEntryStage != currentRoutineEntryStage) {
            resetSpeechEngine();
            resetTimers();
            this.currentRoutineEntryStage = currentRoutineEntryStage;
            narrate();
        }
    }

    /**
     * Reset text in info layout
     */
    private void resetInfoLayout() {
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

                if (routineEntry.getStandardNumberOfSets() > 0) {
                    textViewRoutineEntrySetNumber.setText("Set " + (currentRoutineEntrySetNumber + 1) + " of " + routineEntry.getStandardNumberOfSets());
                    imageButtonPreviousSet.setVisibility(View.VISIBLE);
                    imageButtonNextSet.setVisibility(View.VISIBLE);
                } else {
                    textViewRoutineEntrySetNumber.setText("");
                    imageButtonPreviousSet.setVisibility(View.INVISIBLE);
                    imageButtonNextSet.setVisibility(View.INVISIBLE);
                }

            }
        });

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
                            textViewRepetitionsText.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            repetitionsView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            textViewRepetitionsText.setText("--");
                            repetitionsView.setProgressWithAnimation(0, 1000);
                            return;
                        }

                        if (!mute && !narrateSeconds) {
                            textViewRepetitionsText.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            repetitionsView.setColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            HerculesSpeechEngine.skippableSpeak(String.valueOf(timerCount[0]));
                        } else {
                            textViewRepetitionsText.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            repetitionsView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                        }
                        textViewRepetitionsText.setText(String.valueOf(timerCount[0] + "\nReps"));
                        repetitionsView.setProgressMax(totalRepetitions);
                        repetitionsView.setProgressWithAnimation(timerCount[0], delay);

                        timerCount[0]--;
                        if (timerCount[0] > 0) {
                            updateRepetitionsCounter(mute, totalRepetitions, timerCount[0], delay);
                        } else {
                            textViewRepetitionsText.setText("--");
                            repetitionsView.setProgressWithAnimation(0, delay);
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
                            textViewTimerText.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            textViewTimerText.setText("--");
                            timerView.setProgressWithAnimation(0, 1000);
                            return;
                        }

                        if (!mute && narrateSeconds) {
                            textViewTimerText.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                            if (timerCount[0] < 15) { // count all seconds if 15 seconds or less
                                HerculesSpeechEngine.skippableSpeak(String.valueOf(timerCount[0]));
                            } else {
                                if (timerCount[0] % 10 == 0) {  //count every 10 seconds
                                    HerculesSpeechEngine.skippableSpeak(String.valueOf(timerCount[0]));
                                }
                            }
                        } else {
                            textViewTimerText.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                            timerView.setColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                        }
                        textViewTimerText.setText(String.valueOf(timerCount[0] + "\nSeconds"));
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
     * Begin next set
     */
    public synchronized void previousSet() {
        if (currentRoutineEntrySetNumber > 0) {
            resetSpeechEngine();
            resetTimers();
            currentRoutineEntrySetNumber--;
            currentRoutineEntryStage = RES_GET_IN_POSITION_SPEECH;
            resetInfoLayout();
            narrate();
        }

        resetSetDecrementIncrementButtons();
    }

    /**
     * Begin next set
     */
    public synchronized void nextSet() {
        if (currentRoutineEntrySetNumber < (routineEntry.getStandardNumberOfSets() - 1)) {
            resetSpeechEngine();
            resetTimers();
            currentRoutineEntrySetNumber++;
            currentRoutineEntryStage = RES_GET_IN_POSITION_SPEECH;
            resetInfoLayout();
            narrate();
        }
        resetSetDecrementIncrementButtons();
    }

    /**
     * Change color of minus / plus set icons
     */
    private void resetSetDecrementIncrementButtons() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentRoutineEntrySetNumber <= 0) {
                    imageButtonPreviousSet.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_minus_icon_gray_0p_padding));
                } else {
                    imageButtonPreviousSet.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_minus_icon));
                }

                if (currentRoutineEntrySetNumber >= (routineEntry.getStandardNumberOfSets() - 1)) {
                    imageButtonNextSet.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_add_icon_gray_0p_padding));
                } else {
                    imageButtonNextSet.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_add_icon));
                }
            }
        });
    }

    public int getCurrentRoutineEntryStage() {
        return currentRoutineEntryStage;
    }

    /**
     * Pause narration
     */
    public void pause() {
        HerculesSpeechEngine.stopSpeaking();
        setNotification();
    }

    /**
     * Notification listener
     */
    public class NotificationControlListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("Hercules", "Received " + action);
            if (action.equalsIgnoreCase("com.xenodochium.hercules.ACTION_PLAY")) {
                if (RoutineOrchestratorImpl.getInstance().isPlaying()) {
                    RoutineOrchestratorImpl.getInstance().pause();
                } else {
                    RoutineOrchestratorImpl.getInstance().play();
                }

                setNotification();
            } else if (action.equalsIgnoreCase("com.xenodochium.hercules.ACTION_NEXT")) {
                RoutineOrchestratorImpl.getInstance().next();
            } else if (action.equalsIgnoreCase("com.xenodochium.hercules.ACTION_PREVIOUS")) {
                RoutineOrchestratorImpl.getInstance().previous();
            }
        }
    }

    /**
     * @param routineEntry
     */
    private String getNarrationTextForRoutineEntry(RoutineEntry routineEntry, int stage) {
        String routineEntryNarration = routineEntry.getName();
        return routineEntryNarration;
    }
}
