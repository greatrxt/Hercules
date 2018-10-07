package com.xenodochium.hercules.util;

import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.DaoSession;
import com.xenodochium.hercules.model.Routine;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.model.Workout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InitialData {
    private static DaoSession daoSession;

    public static void insertBasicDataIfFirstTime(DaoSession currentDaoSession) {
        if (UserPreferences.isFirstTime()) {
            daoSession = currentDaoSession;

            insertBodyParts();
            insertWorkouts();
            insertRoutines();
        }
    }

    /**
     *
     */
    private static void insertRoutines() {
        Routine routine = new Routine();
        routine.setName("Monday");
        RoutineEntry jogging = RoutineEntry.convertWorkoutToRoutineEntry(1L, daoSession.getWorkoutDao().load(1L));
        RoutineEntry benchPress = RoutineEntry.convertWorkoutToRoutineEntry(1L, daoSession.getWorkoutDao().load(2L));
        RoutineEntry breakRoutineEntry = new RoutineEntry();
        breakRoutineEntry.setRoutineId(1L);
        breakRoutineEntry.setName(RoutineEntry.RoutineEntryType.BREAK.toString());
        breakRoutineEntry.setRoutineEntryType(RoutineEntry.RoutineEntryType.BREAK);
        breakRoutineEntry.setDuration(60);

        List<RoutineEntry> routineEntryList = new ArrayList<>();
        routineEntryList.add(jogging);
        routineEntryList.add(breakRoutineEntry);
        routineEntryList.add(benchPress);
        routine.setLinkedRoutineEntries(routineEntryList);

        daoSession.getRoutineEntryDao().insertOrReplace(jogging);
        daoSession.getRoutineEntryDao().insertOrReplace(breakRoutineEntry);
        daoSession.getRoutineEntryDao().insertOrReplace(benchPress);
        daoSession.getRoutineDao().insertOrReplace(routine);
    }

    /**
     *
     */
    private static void insertWorkouts() {
        Workout jogging = new Workout(1L, "Jogging", 600, 1L);
        daoSession.getWorkoutDao().insertOrReplace(jogging);

        // Workout(Long workoutId, String name, int standardNumberOfRepetitions, int standardNumberOfSets, int duration, int timeToGetInPosition, int restTimeAfterExercise, long bodyPartId) {
        Workout benchPress = new Workout(2L, "Bench Press", 15, 3, 30, 20, 60, 2L);
        daoSession.getWorkoutDao().insertOrReplace(benchPress);
    }

    /**
     *
     */
    private static void insertBodyParts() {
        List<String> bodyPartsList = new ArrayList<>();
        bodyPartsList.add("Full Body"); //ID 1
        bodyPartsList.add("Chest"); //ID 2
        bodyPartsList.add("Back"); //ID 3
        bodyPartsList.add("Shoulders"); //ID 4
        bodyPartsList.add("Biceps"); //ID 5
        bodyPartsList.add("Triceps"); //ID 6
        bodyPartsList.add("Legs"); //ID 7

        Iterator<String> bodyPartsIterator = bodyPartsList.iterator();
        long i = 1;
        while (bodyPartsIterator.hasNext()) {
            BodyPart bodyPart = new BodyPart();
            bodyPart.setBodyPartId(i);
            bodyPart.setName(bodyPartsIterator.next());
            daoSession.getBodyPartDao().insertOrReplace(bodyPart);
            i++;
        }
    }
}
