package com.xenodochium.hercules.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

@Entity
public class BodyPart implements Serializable { //implementing serializable so that it could be passed from one activity to another using intent serializable extras

    static final long serialVersionUID = 83743848;

    @Id
    private Long bodyPartId;

    @NotNull
    private String name;

    @ToMany(referencedJoinProperty = "bodyPartId")
    private List<Workout> linkedWorkouts;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 178939930)
    private transient BodyPartDao myDao;

    @Generated(hash = 448841286)
    public BodyPart(Long bodyPartId, @NotNull String name) {
        this.bodyPartId = bodyPartId;
        this.name = name;
    }

    @Generated(hash = 1218211323)
    public BodyPart() {
    }

    public Long getBodyPartId() {
        return this.bodyPartId;
    }

    public void setBodyPartId(Long bodyPartId) {
        this.bodyPartId = bodyPartId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 702906217)
    public List<Workout> getLinkedWorkouts() {
        if (linkedWorkouts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkoutDao targetDao = daoSession.getWorkoutDao();
            List<Workout> linkedWorkoutsNew = targetDao._queryBodyPart_LinkedWorkouts(bodyPartId);
            synchronized (this) {
                if (linkedWorkouts == null) {
                    linkedWorkouts = linkedWorkoutsNew;
                }
            }
        }
        return linkedWorkouts;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 758110839)
    public synchronized void resetLinkedWorkouts() {
        linkedWorkouts = null;
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
    @Generated(hash = 168013184)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBodyPartDao() : null;
    }
}
