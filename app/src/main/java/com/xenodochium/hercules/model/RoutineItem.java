package com.xenodochium.hercules.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;

@Entity
public class RoutineItem {

    @Id
    private Long routineItemId;

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

    @ToOne(joinProperty = "bodyPartId")
    private BodyPart forBodyPart;
    @Convert(converter = RoutineItemTypeConverter.class, columnType = String.class)
    private RoutineItemType routineItemType;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1798729585)
    private transient RoutineItemDao myDao;
    @Generated(hash = 1492005445)
    private transient Long forBodyPart__resolvedKey;

    @Generated(hash = 1152608144)
    public RoutineItem(Long routineItemId, @NotNull String name, int standardNumberOfRepetitions,
                       boolean countRepetitionsInReverse, int standardNumberOfSets, int duration,
                       boolean countDurationInReverse, int timeToGetInPosition, int restTimeAfterExercise,
                       long bodyPartId, RoutineItemType routineItemType) {
        this.routineItemId = routineItemId;
        this.name = name;
        this.standardNumberOfRepetitions = standardNumberOfRepetitions;
        this.countRepetitionsInReverse = countRepetitionsInReverse;
        this.standardNumberOfSets = standardNumberOfSets;
        this.duration = duration;
        this.countDurationInReverse = countDurationInReverse;
        this.timeToGetInPosition = timeToGetInPosition;
        this.restTimeAfterExercise = restTimeAfterExercise;
        this.bodyPartId = bodyPartId;
        this.routineItemType = routineItemType;
    }

    @Generated(hash = 409773735)
    public RoutineItem() {
    }

    public Long getRoutineItemId() {
        return this.routineItemId;
    }

    public void setRoutineItemId(Long routineItemId) {
        this.routineItemId = routineItemId;
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

    public RoutineItemType getRoutineItemType() {
        return this.routineItemType;
    }

    public void setRoutineItemType(RoutineItemType routineItemType) {
        this.routineItemType = routineItemType;
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1593055851)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoutineItemDao() : null;
    }

    enum RoutineItemType {
        REST, EXERCISE
    }

    static class RoutineItemTypeConverter implements PropertyConverter<RoutineItemType, String> {
        @Override
        public RoutineItemType convertToEntityProperty(String databaseValue) {
            return RoutineItemType.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(RoutineItemType entityProperty) {
            return entityProperty.name();
        }
    }
}
