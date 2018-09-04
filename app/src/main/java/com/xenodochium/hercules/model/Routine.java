package com.xenodochium.hercules.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity
public class Routine {

    @Id
    private Long routineId;

    @NotNull
    private String name;

    @ToMany(referencedJoinProperty = "routineItemId")
    private List<RoutineItem> routineItems;

    private boolean playInLoop;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1822163310)
    private transient RoutineDao myDao;

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
    @Generated(hash = 429888693)
    public List<RoutineItem> getRoutineItems() {
        if (routineItems == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoutineItemDao targetDao = daoSession.getRoutineItemDao();
            List<RoutineItem> routineItemsNew = targetDao
                    ._queryRoutine_RoutineItems(routineId);
            synchronized (this) {
                if (routineItems == null) {
                    routineItems = routineItemsNew;
                }
            }
        }
        return routineItems;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1629029994)
    public synchronized void resetRoutineItems() {
        routineItems = null;
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
    @Generated(hash = 360604408)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoutineDao() : null;
    }
}
