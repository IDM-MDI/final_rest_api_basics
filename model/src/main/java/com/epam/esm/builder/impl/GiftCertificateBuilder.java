package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class GiftCertificateBuilder implements ModelBuilder {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private LocalDateTime create_date;
    private LocalDateTime update_date;
    private boolean deleted;

    private List<Tag> tagList;

    public GiftCertificateBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public GiftCertificateBuilder setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public GiftCertificateBuilder setTagList(List<Tag> tagList) {
        this.tagList = tagList;
        return this;
    }

    public GiftCertificateBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public GiftCertificateBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public GiftCertificateBuilder setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
        return this;
    }

    public GiftCertificateBuilder setUpdate_date(LocalDateTime update_date) {
        this.update_date = update_date;
        return this;
    }

    public GiftCertificateBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public GiftCertificateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public GiftCertificate build() {
        GiftCertificate result = new GiftCertificate();
        result.setId(id);
        result.setName(name);
        result.setDescription(description);
        result.setDuration(duration);
        result.setPrice(price);
        result.setCreate_date(create_date);
        result.setUpdate_date(update_date);
        result.setTags(tagList == null ? new ArrayList<>() : tagList);
        result.setDeleted(deleted);
        return result;
    }
}
