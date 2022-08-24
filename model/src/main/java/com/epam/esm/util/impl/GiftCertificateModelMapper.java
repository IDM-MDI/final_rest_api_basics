package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
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
        return dto == null ? null : builder.setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setDuration(dto.getDuration())
                .setPrice(dto.getPrice())
                .setCreateDate(dto.getCreateDate())
                .setUpdateDate(dto.getUpdateDate())
                .setTagList(tagModelMapper.toEntityList(dto.getTags()))
                .setShop(dto.getShop())
                .setMainImage(dto.getMainImage() != null? Base64.getDecoder().decode(dto.getMainImage()) : null)
                .setSecondImage(dto.getSecondImage() != null? Base64.getDecoder().decode(dto.getSecondImage()) : null)
                .setThirdImage(dto.getThirdImage() != null? Base64.getDecoder().decode(dto.getThirdImage()) : null)
                .setStatus(dto.getStatus())
                .build();
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate entity) {
        if(entity == null) {
            return null;
        }
        GiftCertificateDto result = new GiftCertificateDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setDescription(entity.getDescription());
        result.setPrice(entity.getPrice());
        result.setDuration(entity.getDuration());
        result.setCreateDate(entity.getCreateDate());
        result.setUpdateDate(entity.getUpdateDate());
        result.setTags(tagModelMapper.toDtoList(entity.getTagList()));
        result.setShop(entity.getShop());
        if(entity.getMainImage() != null) {
            result.setMainImage(Base64.getEncoder().encodeToString(entity.getMainImage()));
        }
        if(entity.getSecondImage() != null) {
            result.setMainImage(Base64.getEncoder().encodeToString(entity.getSecondImage()));
        }
        if(entity.getThirdImage() != null) {
            result.setMainImage(Base64.getEncoder().encodeToString(entity.getThirdImage()));
        }
        result.setHaveMainImage(entity.getMainImage() != null);
        result.setHaveSecondImage(entity.getSecondImage() != null);
        result.setHaveThirdImage(entity.getThirdImage() != null);
        result.setStatus(entity.getStatus());
        return result;
    }

    @Override
    public List<GiftCertificate> toEntityList(List<GiftCertificateDto> dtoList) {
        return dtoList == null ? null : dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<GiftCertificateDto> toDtoList(List<GiftCertificate> entityList) {
        return entityList == null ? null : entityList.stream().map(this::toDto).toList();
    }
}
