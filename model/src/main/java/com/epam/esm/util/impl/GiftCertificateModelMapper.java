package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GiftCertificateModelMapper implements ModelMapper<GiftCertificate, GiftCertificateDto> {

    private final TagModelMapper tagModelMapper;
    private final GiftCertificateBuilder builder;
    @Autowired
    public GiftCertificateModelMapper(TagModelMapper tagModelMapper, GiftCertificateBuilder builder) {
        this.tagModelMapper = tagModelMapper;
        this.builder = builder;
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDto dto) {
        return builder.setName(dto.getName()).setDescription(dto.getDescription()).
                setDuration(dto.getDuration()).setPrice(dto.getPrice()).
                setCreate_date(dto.getCreate_date()).setUpdate_date(dto.getUpdate_date())
                .setTagList(tagModelMapper.toEntityList(dto.getTags())).build();
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate entity) {
        GiftCertificateDto result = new GiftCertificateDto();
        result.setName(entity.getName());
        result.setDescription(entity.getDescription());
        result.setPrice(entity.getPrice());
        result.setDuration(entity.getDuration());
        result.setCreate_date(entity.getCreateDate());
        result.setUpdate_date(entity.getUpdateDate());
        result.setTags(tagModelMapper.toDtoList(entity.getTagList()));
        return result;
    }

    @Override
    public List<GiftCertificate> toEntityList(List<GiftCertificateDto> dtoList) {
        List<GiftCertificate> result = new ArrayList<>();
        dtoList.forEach(i -> result.add(toEntity(i)));
        return result;
    }

    @Override
    public List<GiftCertificateDto> toDtoList(List<GiftCertificate> entityList) {
        List<GiftCertificateDto> result = new ArrayList<>();
        entityList.forEach(i -> result.add(toDto(i)));
        return result;
    }
}
