package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class GiftCertificateBuilder implements ModelBuilder {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime create_date;
    private LocalDateTime update_date;
    private Status status;

    private List<Tag> tagList;

    public GiftCertificateBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public GiftCertificateBuilder setStatus(Status status) {
        this.status = status;
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

    public GiftCertificateBuilder setDuration(Integer duration) {
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

    public GiftCertificateBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public GiftCertificateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public GiftCertificate build() {
        GiftCertificate result = new GiftCertificate(
                id,
                name,description,
                price,duration,
                create_date,update_date,
                tagList == null ? new ArrayList<>() : tagList,
                status
        );
        clear();
        return result;
    }

    @Override
    public void clear() {
        id = null;
        name = null;
        description = null;
        duration = null;
        price = null;
        create_date = null;
        update_date = null;
        tagList = null;
        status = null;
    }
}
