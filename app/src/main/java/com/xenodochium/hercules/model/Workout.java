package com.xenodochium.hercules.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Entity
public class Workout implements Serializable { //implementing serializable so that it could be passed from one activity to another using intent serializable extras

    static final long serialVersionUID = 322434343;

    @Id
    private Long workoutId;

    @NotNull
    private String name;

    private int standardNumberOfRepetitions;
    private boolean countRepetitionsInReverse;

    private int standardNumberOfSets;

    @NotNull
    private int duration;
    private boolean countDurationInReverse;

    private int timeToGetInPosition;
    private int restTimeAfterExercise;

    private long bodyPartId;

    /**
     * @param routineEntry
     * @return
     */
    public static Workout convertRoutineEntryToWorkout(RoutineEntry routineEntry) {
        Workout workout = new Workout();
        workout.setWorkoutId(routineEntry.getRoutineEntryId());
        workout.setName(routineEntry.getName());
        workout.setBodyPartId(routineEntry.getBodyPartId());
        workout.setStandardNumberOfRepetitions(routineEntry.getStandardNumberOfRepetitions());
        workout.setStandardNumberOfSets(routineEntry.getStandardNumberOfSets());
        workout.setDuration(routineEntry.getDuration());
        workout.setCountDurationInReverse(routineEntry.getCountDurationInReverse());
        workout.setCountRepetitionsInReverse(routineEntry.getCountRepetitionsInReverse());
        workout.setTimeToGetInPosition(routineEntry.getTimeToGetInPosition());
        workout.setRestTimeAfterExercise(routineEntry.getRestTimeAfterExercise());
        return workout;
    }

    /**
     * @param routineEntryList
     * @return
     */
    public static List<Workout> convertRoutineEntryListToWorkoutList(List<RoutineEntry> routineEntryList) {
        List<Workout> workoutList = new ArrayList<>();
        Iterator<RoutineEntry> routineEntryIterator = routineEntryList.iterator();
        while (routineEntryIterator.hasNext()) {
            workoutList.add(convertRoutineEntryToWorkout(routineEntryIterator.next()));
        }
        return workoutList;
    }

    @ToOne(joinProperty = "bodyPartId")
    private BodyPart forBodyPart;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1950649078)
    private transient WorkoutDao myDao;
    @Generated(hash = 1492005445)
    private transient Long forBodyPart__resolvedKey;

    @Generated(hash = 1117692692)
    public Workout(Long workoutId, @NotNull String name, int standardNumberOfRepetitions, boolean countRepetitionsInReverse, int standardNumberOfSets, int duration,
                   boolean countDurationInReverse, int timeToGetInPosition, int restTimeAfterExercise, long bodyPartId) {
        this.workoutId = workoutId;
        this.name = name;
        this.standardNumberOfRepetitions = standardNumberOfRepetitions;
        this.countRepetitionsInReverse = countRepetitionsInReverse;
        this.standardNumberOfSets = standardNumberOfSets;
        this.duration = duration;
        this.countDurationInReverse = countDurationInReverse;
        this.timeToGetInPosition = timeToGetInPosition;
        this.restTimeAfterExercise = restTimeAfterExercise;
        this.bodyPartId = bodyPartId;
    }

    @Generated(hash = 570607860)
    public Workout() {
    }

    public Workout(Long workoutId, String name, int duration, long bodyPartId) {
        this.workoutId = workoutId;
        this.name = name;
        this.duration = duration;
        this.bodyPartId = bodyPartId;
    }

    public Workout(Long workoutId, String name, int standardNumberOfRepetitions, int standardNumberOfSets, int duration, int timeToGetInPosition, int restTimeAfterExercise, long bodyPartId) {
        this.workoutId = workoutId;
        this.name = name;
        this.standardNumberOfRepetitions = standardNumberOfRepetitions;
        this.standardNumberOfSets = standardNumberOfSets;
        this.duration = duration;
        this.timeToGetInPosition = timeToGetInPosition;
        this.restTimeAfterExercise = restTimeAfterExercise;
        this.bodyPartId = bodyPartId;
    }

    public Workout copy() {  //creating copy to resolve disappearing drag drop item issue in RoutingActivity

        Workout workout = new Workout();

        Random random = new Random();
        workout.workoutId = random.nextLong(); //assigning random ID to resolve disappearing drag drop item issue in RoutingActivity

        workout.name = this.name;
        workout.bodyPartId = this.bodyPartId;
        workout.standardNumberOfRepetitions = this.standardNumberOfRepetitions;
        workout.standardNumberOfSets = this.standardNumberOfSets;
        workout.duration = this.duration;
        workout.countDurationInReverse = this.countDurationInReverse;
        workout.countRepetitionsInReverse = this.countRepetitionsInReverse;
        workout.duration = this.duration;
        workout.timeToGetInPosition = this.timeToGetInPosition;
        workout.restTimeAfterExercise = this.restTimeAfterExercise;

        return workout;
    }

    public Long getWorkoutId() {
        return this.workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStandardNumberOfRepetitions() {
        return this.standardNumberOfRepetitions;
    }

    public void setStandardNumberOfRepetitions(int standardNumberOfRepetitions) {
        this.standardNumberOfRepetitions = standardNumberOfRepetitions;
    }

    public boolean getCountRepetitionsInReverse() {
        return this.countRepetitionsInReverse;
    }

    public void setCountRepetitionsInReverse(boolean countRepetitionsInReverse) {
        this.countRepetitionsInReverse = countRepetitionsInReverse;
    }

    public int getStandardNumberOfSets() {
        return this.standardNumberOfSets;
    }

    public void setStandardNumberOfSets(int standardNumberOfSets) {
        this.standardNumberOfSets = standardNumberOfSets;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean getCountDurationInReverse() {
        return this.countDurationInReverse;
    }

    public void setCountDurationInReverse(boolean countDurationInReverse) {
        this.countDurationInReverse = countDurationInReverse;
    }

    public int getTimeToGetInPosition() {
        return this.timeToGetInPosition;
    }

    public void setTimeToGetInPosition(int timeToGetInPosition) {
        this.timeToGetInPosition = timeToGetInPosition;
    }

    public int getRestTimeAfterExercise() {
        return this.restTimeAfterExercise;
    }

    public void setRestTimeAfterExercise(int restTimeAfterExercise) {
        this.restTimeAfterExercise = restTimeAfterExercise;
    }

    public long getBodyPartId() {
        return this.bodyPartId;
    }

    public void setBodyPartId(long bodyPartId) {
        this.bodyPartId = bodyPartId;
    }

    @Override
    public String toString() {
        return name;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 623521386)
    public BodyPart getForBodyPart() {
        long __key = this.bodyPartId;
        if (forBodyPart__resolvedKey == null || !forBodyPart__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BodyPartDao targetDao = daoSession.getBodyPartDao();
            BodyPart forBodyPartNew = targetDao.load(__key);
            synchronized (this) {
                forBodyPart = forBodyPartNew;
                forBodyPart__resolvedKey = __key;
            }
        }
        return forBodyPart;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1004443887)
    public void setForBodyPart(@NotNull BodyPart forBodyPart) {
        if (forBodyPart == null) {
            throw new DaoException("To-one property 'bodyPartId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.forBodyPart = forBodyPart;
            bodyPartId = forBodyPart.getBodyPartId();
            forBodyPart__resolvedKey = bodyPartId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1398188052)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWorkoutDao() : null;
    }


}
