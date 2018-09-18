package com.xenodochium.hercules.model;

import com.xenodochium.hercules.application.Hercules;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.query.DeleteQuery;

import java.io.Serializable;
import java.util.List;

@Entity
public class Routine implements Serializable { //implementing serializable so that it could be passed from one activity to another using intent serializable extras

    static final long serialVersionUID = 14334778;

    @Id
    private Long routineId;

    @NotNull
    private String name;

    private boolean playInLoop;

    @ToMany(referencedJoinProperty = "routineId")
    private List<RoutineEntry> linkedRoutineEntries;

    public void deleteAllLinkedEntries() {
        if (routineId != null) {
            final DeleteQuery<RoutineEntry> tableDeleteQuery = Hercules.getInstance().getDaoSession().queryBuilder(RoutineEntry.class)
                    .where(RoutineEntryDao.Properties.RoutineId.eq(routineId))
                    .buildDelete();
            tableDeleteQuery.executeDeleteWithoutDetachingEntities();
            Hercules.getInstance().getDaoSession().clear();
        }
    }
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1822163310)
    private transient RoutineDao myDao;

    @Override
    public String toString() {
        return name;
    }

    @Generated(hash = 202976362)
    public Routine(Long routineId, @NotNull String name, boolean playInLoop) {
        this.routineId = routineId;
        this.name = name;
        this.playInLoop = playInLoop;
    }

    @Generated(hash = 708323760)
    public Routine() {
    }


    public Long getRoutineId() {
        return this.routineId;
    }

    public void setRoutineId(Long routineId) {
        this.routineId = routineId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getPlayInLoop() {
        return this.playInLoop;
    }

    public void setPlayInLoop(boolean playInLoop) {
        this.playInLoop = playInLoop;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1704873183)
    public List<RoutineEntry> getLinkedRoutineEntries() {
        if (linkedRoutineEntries == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoutineEntryDao targetDao = daoSession.getRoutineEntryDao();
            List<RoutineEntry> linkedRoutineEntriesNew = targetDao._queryRoutine_LinkedRoutineEntries(routineId);
            synchronized (this) {
                if (linkedRoutineEntries == null) {
                    linkedRoutineEntries = linkedRoutineEntriesNew;
                }
            }
        }
        return linkedRoutineEntries;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 18279760)
    public synchronized void resetLinkedRoutineEntries() {
        linkedRoutineEntries = null;
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
    @Generated(hash = 360604408)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoutineDao() : null;
    }


}
