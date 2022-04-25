package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.GiftTag;
import org.springframework.stereotype.Component;

@Component
public class GiftTagBuilder implements ModelBuilder {
    private long id;
    private long giftId;
    private long tagId;
    private boolean isDeleted;

    public GiftTagBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public GiftTagBuilder setGiftId(long id) {
        this.giftId = id;
        return this;
    }

    public GiftTagBuilder setTagId(long id) {
        this.tagId = id;
        return this;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public GiftTag build() {
        GiftTag result = new GiftTag();
        result.setId(id);
        result.setGiftId(giftId);
        result.setTagId(tagId);
        result.setDeleted(isDeleted);
        return result;
    }
}
