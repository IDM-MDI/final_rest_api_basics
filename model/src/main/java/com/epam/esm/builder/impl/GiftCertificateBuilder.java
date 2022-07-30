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
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
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

    public GiftCertificateBuilder setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public GiftCertificateBuilder setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
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
                createDate, updateDate,
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
        createDate = null;
        updateDate = null;
        tagList = null;
        status = null;
    }
}
