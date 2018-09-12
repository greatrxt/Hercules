package com.xenodochium.hercules.engine;

import android.speech.tts.UtteranceProgressListener;

import com.xenodochium.hercules.model.RoutineEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerEngine {


    /**
     * Stages
     * <p>
     * 1) Initiation
     * 2)
     */
    public static List<String> initiation = new ArrayList<>();
    private static int currentlyPlayingRoutineEntryNumber = 0;
    private static List<RoutineEntry> routineEntryPlayList;

    public static int getCurrentlyPlayingRoutineEntryNumber() {
        return currentlyPlayingRoutineEntryNumber;
    }

    public static void setCurrentlyPlayingRoutineEntryNumber(int currentlyPlayingRoutineEntryNumber) {
        PlayerEngine.currentlyPlayingRoutineEntryNumber = currentlyPlayingRoutineEntryNumber;
    }

    public static List<RoutineEntry> getRoutineEntryPlayList() {
        return routineEntryPlayList;
    }

    public static void setRoutineEntryPlayList(List<RoutineEntry> routineEntryPlayList) {
        PlayerEngine.routineEntryPlayList = routineEntryPlayList;
    }

    //randomly selected
    public static void add() {
        initiation.add("Okay ! Lets get started");
        initiation.add("Lets go");
        initiation.add("Beginning today's routine");
        initiation.add("3, 2, 1. Lets go !");
    }

    /**
     * Plays routine based on routine entries
     */
    public static void playRoutine() {
        if (initiation.size() == 0) {
            add();
        }
        final int[] counter = {0};
        counter[0] = currentlyPlayingRoutineEntryNumber;
        Random random = new Random();

        final UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                if (routineEntryPlayList.get(counter[0]) != null)
                    HerculesSpeechEngine.speak(getNarrationTextForRoutineEntry(routineEntryPlayList.get(counter[0]++)), this);
            }

            @Override
            public void onError(String s) {

            }
        };

        HerculesSpeechEngine.speak(initiation.get(random.nextInt(initiation.size())), utteranceProgressListener);
    }

    /**
     * @param routineEntry
     */
    private static String getNarrationTextForRoutineEntry(RoutineEntry routineEntry) {
        String routineEntryNarration = routineEntry.getName();
        return routineEntryNarration;
    }
}
