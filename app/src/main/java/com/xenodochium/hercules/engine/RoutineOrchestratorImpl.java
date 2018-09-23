package com.xenodochium.hercules.engine;

import android.app.Activity;
import android.graphics.Typeface;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropRoutineEntryItemAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.ui.CircularProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Player engine to play all routine entries
 */
public class RoutineOrchestratorImpl extends UtteranceProgressListener implements RoutineOrchestrator {
    private static RoutineOrchestratorImpl routineOrchestratorImpl;
    public List<String> initiation;
    TextView textViewRoutineEntryName, textViewTimerText, textViewRepetitionsText, textViewRoutineEntrySetNumber,
            textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel,
            textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest;
    private DragAndDropRoutineEntryItemAdapter dragAndDropRoutineEntryItemAdapter;
    private int currentlyPlayingRoutineEntryNumber = -1;
    private List<RoutineEntry> routineEntryPlayList;
    private boolean isPlaying = false;
    private CircularProgressBar timerView, repetitionsView;
    private Activity activity;

    private ImageButton imageButtonPlay, imageButtonForward, imageButtonRewind;

    private RoutineOrchestratorImpl() {
        routineEntryPlayList = new ArrayList<>();
        initiation = Arrays.asList(Hercules.getInstance().getResources().getStringArray(R.array.player_initiation));
    }

    public static RoutineOrchestratorImpl getInstance() {
        if (routineOrchestratorImpl == null) {
            routineOrchestratorImpl = new RoutineOrchestratorImpl();
        }

        return routineOrchestratorImpl;
    }

    public void initiate(Activity activity, List<RoutineEntry> routineEntryPlayList,
                         CircularProgressBar timerView,
                         CircularProgressBar repetitionsView,
                         TextView textViewRoutineEntryName,
                         TextView textViewRoutineEntryTtgpLabel,
                         TextView textViewRoutineEntrySetLabel,
                         TextView textViewRoutineEntryRestLabel,
                         TextView textViewRoutineEntryTtgp,
                         TextView textViewRoutineEntrySet,
                         TextView textViewRoutineEntryRest,
                         TextView textViewRoutineEntrySetNumber,
                         TextView textViewTimerText,
                         TextView textViewRepetitionsText,
                         ImageButton imageButtonPlay,
                         ImageButton imageButtonRewind,
                         ImageButton imageButtonForward) {
        this.activity = activity;
        this.routineEntryPlayList = routineEntryPlayList;
        this.timerView = timerView;
        this.repetitionsView = repetitionsView;
        this.textViewRoutineEntryName = textViewRoutineEntryName;
        this.textViewRoutineEntryTtgpLabel = textViewRoutineEntryTtgpLabel;
        this.textViewRoutineEntrySetLabel = textViewRoutineEntrySetLabel;
        this.textViewRoutineEntryRestLabel = textViewRoutineEntryRestLabel;
        this.textViewRoutineEntryTtgp = textViewRoutineEntryTtgp;
        this.textViewRoutineEntrySet = textViewRoutineEntrySet;
        this.textViewRoutineEntryRest = textViewRoutineEntryRest;
        this.textViewRoutineEntrySetNumber = textViewRoutineEntrySetNumber;
        this.textViewTimerText = textViewTimerText;
        this.textViewRepetitionsText = textViewRepetitionsText;
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
        if (routineEntryPlayList.size() == 0) {
            HerculesSpeechEngine.waitAndSpeak("Workout playlist is empty");
            pause();
            return;
        }

        HerculesSpeechEngine.stopSpeaking();

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
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewRoutineEntryName.setText(String.valueOf(routineEntryPlayList.get(currentlyPlayingRoutineEntryNumber).getName()));
                    if (dragAndDropRoutineEntryItemAdapter != null) {
                        Iterator<Integer> viewsIterator = dragAndDropRoutineEntryItemAdapter.getViewHolderMap().keySet().iterator();
                        while (viewsIterator.hasNext()) {
                            Integer position = viewsIterator.next();
                            DragItemAdapter.ViewHolder viewHolder = dragAndDropRoutineEntryItemAdapter.getViewHolderMap().get(position);
                            if (position == currentlyPlayingRoutineEntryNumber) {
                                ((TextView) viewHolder.itemView.findViewById(R.id.text_view_exercise_name)).setTypeface(null, Typeface.BOLD);
                                ((TextView) viewHolder.itemView.findViewById(R.id.text_view_exercise_name)).setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                            } else {
                                ((TextView) viewHolder.itemView.findViewById(R.id.text_view_exercise_name)).setTypeface(null, Typeface.NORMAL);
                                ((TextView) viewHolder.itemView.findViewById(R.id.text_view_exercise_name)).setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                            }
                        }

                    }
                }
            });
            RoutineEntryNarratorImpl.getInstance().initiate(activity, routineEntryPlayList.get(currentlyPlayingRoutineEntryNumber), timerView, repetitionsView,
                    textViewRoutineEntryTtgpLabel, textViewRoutineEntrySetLabel, textViewRoutineEntryRestLabel,
                    textViewRoutineEntryTtgp, textViewRoutineEntrySet, textViewRoutineEntryRest,
                    textViewRoutineEntrySetNumber, textViewTimerText, textViewRepetitionsText);
            RoutineEntryNarratorImpl.getInstance().narrate();
        } else {
            HerculesSpeechEngine.waitAndSpeak("Routine over");
            pause();
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
        HerculesSpeechEngine.stopSpeaking();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public List<RoutineEntry> getRoutineEntryPlayList() {
        return routineEntryPlayList;
    }

    public void setDragAndDropRoutineEntryItemAdapter(DragAndDropRoutineEntryItemAdapter dragAndDropRoutineEntryItemAdapter) {
        this.dragAndDropRoutineEntryItemAdapter = dragAndDropRoutineEntryItemAdapter;
    }

    public int getCurrentlyPlayingRoutineEntryNumber() {
        return currentlyPlayingRoutineEntryNumber;
    }

    public void setCurrentlyPlayingRoutineEntryNumber(int currentlyPlayingRoutineEntryNumber) {
        this.currentlyPlayingRoutineEntryNumber = currentlyPlayingRoutineEntryNumber;
    }
}
