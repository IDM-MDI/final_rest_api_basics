package com.epam.esm.task.entity.impl;

import com.epam.esm.task.entity.Entity;

public class ManyToMany extends Entity {
    private long giftId;
    private long tagId;

    public ManyToMany(long id, long giftId, long tagId) {
        this.id = id;
        this.giftId = giftId;
        this.tagId = tagId;
    }
    public ManyToMany(){}

    public long getGiftId() {
        return giftId;
    }

    public void setGiftId(long giftId) {
        this.giftId = giftId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
}
