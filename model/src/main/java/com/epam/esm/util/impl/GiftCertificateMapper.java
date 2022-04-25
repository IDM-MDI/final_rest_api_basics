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
public class GiftCertificateMapper implements ModelMapper<GiftCertificate, GiftCertificateDto> {

    private final TagMapper tagMapper;

    @Autowired
    public GiftCertificateMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDto dto) {
        return new GiftCertificateBuilder().setId(dto.getId()).setName(dto.getName()).setDescription(dto.getDescription()).
                setDuration(dto.getDuration()).setPrice(dto.getPrice()).
                setCreate_date(dto.getCreate_date()).setUpdate_date(dto.getUpdate_date())
                .setTagList(tagMapper.toEntityList(dto.getTags())).build();
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate entity) {
        GiftCertificateDto result = new GiftCertificateDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setDescription(entity.getDescription());
        result.setPrice(entity.getPrice());
        result.setDuration(entity.getDuration());
        result.setCreate_date(entity.getCreate_date());
        result.setUpdate_date(entity.getUpdate_date());
        result.setTags(tagMapper.toDtoList(entity.getTags()));
        return result;
    }

    @Override
    public List<GiftCertificate> toEntityList(List<GiftCertificateDto> dtoList) {
        List<GiftCertificate> result = new ArrayList<>();
        dtoList.forEach(i -> {
            result.add(toEntity(i));
        });
        return result;
    }

    @Override
    public List<GiftCertificateDto> toDtoList(List<GiftCertificate> entityList) {
        List<GiftCertificateDto> result = new ArrayList<>();
        entityList.forEach(i -> {
            result.add(toDto(i));
        });
        return result;
    }
}
