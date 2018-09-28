package com.xenodochium.hercules.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
public class RoutineEntry implements Serializable {

    static final long serialVersionUID = 259600321;

    @Id
    private Long routineEntryId;

    @NotNull
    private String name;

    private long routineId;

    @NotNull
    @ToOne(joinProperty = "routineId")
    private Routine linkedRoutine;

    @NotNull
    private int routineEntryPosition;

    private int standardNumberOfRepetitions;
    private boolean countRepetitionsInReverse;

    private int standardNumberOfSets;

    @NotNull
    private int duration;
    private boolean countDurationInReverse;

    private int timeToGetInPosition;
    private int restTimeAfterExercise;

    private long bodyPartId;

    @ToOne(joinProperty = "bodyPartId")
    private BodyPart forBodyPart;

    @Convert(converter = RoutineEntryTypeConvertor.class, columnType = String.class)
    private RoutineEntryType routineEntryType;

    /**
     * Convert workout object to routine entry.
     *
     * @param workout
     * @return
     */
    public static RoutineEntry convertWorkoutToRoutineEntry(Long routineId, Workout workout) {
        RoutineEntry routineEntry = new RoutineEntry();
        if (routineId != null) routineEntry.setRoutineId(routineId);
        routineEntry.setRoutineEntryId(workout.getWorkoutId());
        routineEntry.setRoutineEntryType(RoutineEntry.RoutineEntryType.WORKOUT);
        routineEntry.setName(workout.getName());
        routineEntry.setBodyPartId(workout.getBodyPartId());
        routineEntry.setStandardNumberOfRepetitions(workout.getStandardNumberOfRepetitions());
        routineEntry.setStandardNumberOfSets(workout.getStandardNumberOfSets());
        routineEntry.setDuration(workout.getDuration());
        routineEntry.setCountDurationInReverse(workout.getCountDurationInReverse());
        routineEntry.setCountRepetitionsInReverse(workout.getCountRepetitionsInReverse());
        routineEntry.setTimeToGetInPosition(workout.getTimeToGetInPosition());
        routineEntry.setRestTimeAfterExercise(workout.getRestTimeAfterExercise());
        return routineEntry;
    }

    public static List<RoutineEntry> convertWorkoutListToRoutineEntryList(Long routineId, List<Workout> workoutList) {
        List<RoutineEntry> routineEntryList = new ArrayList<>();
        Iterator<Workout> workoutIterator = workoutList.iterator();
        while (workoutIterator.hasNext()) {
            routineEntryList.add(convertWorkoutToRoutineEntry(routineId, workoutIterator.next()));
        }
        return routineEntryList;
    }
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1945310476)
    private transient RoutineEntryDao myDao;
    @Generated(hash = 1492005445)
    private transient Long forBodyPart__resolvedKey;

    @Generated(hash = 1019995934)
    private transient Long linkedRoutine__resolvedKey;

    @Generated(hash = 1493557286)
    public RoutineEntry(Long routineEntryId, @NotNull String name, long routineId,
                        int routineEntryPosition, int standardNumberOfRepetitions,
                        boolean countRepetitionsInReverse, int standardNumberOfSets, int duration,
                        boolean countDurationInReverse, int timeToGetInPosition, int restTimeAfterExercise,
                        long bodyPartId, RoutineEntryType routineEntryType) {
        this.routineEntryId = routineEntryId;
        this.name = name;
        this.routineId = routineId;
        this.routineEntryPosition = routineEntryPosition;
        this.standardNumberOfRepetitions = standardNumberOfRepetitions;
        this.countRepetitionsInReverse = countRepetitionsInReverse;
        this.standardNumberOfSets = standardNumberOfSets;
        this.duration = duration;
        this.countDurationInReverse = countDurationInReverse;
        this.timeToGetInPosition = timeToGetInPosition;
        this.restTimeAfterExercise = restTimeAfterExercise;
        this.bodyPartId = bodyPartId;
        this.routineEntryType = routineEntryType;
    }

    @Generated(hash = 1869357062)
    public RoutineEntry() {
    }

    public Long getRoutineEntryId() {
        return this.routineEntryId;
    }

    public void setRoutineEntryId(Long routineEntryId) {
        this.routineEntryId = routineEntryId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRoutineId() {
        return this.routineId;
    }

    public void setRoutineId(long routineId) {
        this.routineId = routineId;
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

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1004443887)
    public void setForBodyPart(@NotNull BodyPart forBodyPart) {
        if (forBodyPart == null) {
            throw new DaoException(
                    "To-one property 'bodyPartId' has not-null constraint; cannot set to-one to null");
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

    public int getRoutineEntryPosition() {
        return this.routineEntryPosition;
    }

    public void setRoutineEntryPosition(int routineEntryPosition) {
        this.routineEntryPosition = routineEntryPosition;
    }

    public RoutineEntryType getRoutineEntryType() {
        return this.routineEntryType;
    }

    public void setRoutineEntryType(RoutineEntryType routineEntryType) {
        this.routineEntryType = routineEntryType;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1691906188)
    public Routine getLinkedRoutine() {
        long __key = this.routineId;
        if (linkedRoutine__resolvedKey == null || !linkedRoutine__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoutineDao targetDao = daoSession.getRoutineDao();
            Routine linkedRoutineNew = targetDao.load(__key);
            synchronized (this) {
                linkedRoutine = linkedRoutineNew;
                linkedRoutine__resolvedKey = __key;
            }
        }
        return linkedRoutine;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1756804977)
    public void setLinkedRoutine(@NotNull Routine linkedRoutine) {
        if (linkedRoutine == null) {
            throw new DaoException("To-one property 'routineId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.linkedRoutine = linkedRoutine;
            routineId = linkedRoutine.getRoutineId();
            linkedRoutine__resolvedKey = routineId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1215879219)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoutineEntryDao() : null;
    }

    public enum RoutineEntryType {
        WORKOUT, BREAK
    }

    static class RoutineEntryTypeConvertor implements PropertyConverter<RoutineEntryType, String> {
        @Override
        public RoutineEntryType convertToEntityProperty(String databaseValue) {
            return RoutineEntryType.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(RoutineEntryType entityProperty) {
            return entityProperty.name();
        }
    }

    static class RoutineEntryConverter implements PropertyConverter<RoutineEntryType, String> {
        @Override
        public RoutineEntryType convertToEntityProperty(String databaseValue) {
            return RoutineEntryType.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(RoutineEntryType entityProperty) {
            return entityProperty.name();
        }
    }

}
