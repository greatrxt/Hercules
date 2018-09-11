package com.xenodochium.hercules.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

@Entity
public class BodyPart implements Serializable { //implementing serializable so that it could be passed from one activity to another using intent serializable extras

    static final long serialVersionUID = 83743848;

    @Id
    private Long bodyPartId;

    @NotNull
    private String name;

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
}
