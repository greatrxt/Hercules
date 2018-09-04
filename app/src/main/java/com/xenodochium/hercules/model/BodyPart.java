package com.xenodochium.hercules.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class BodyPart {

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
}
