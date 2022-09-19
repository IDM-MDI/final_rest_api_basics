package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.validator.ImageValidator.getByteFromImage;
import static com.epam.esm.validator.ImageValidator.getStringFromImage;
import static com.epam.esm.validator.ImageValidator.isBytesNotNull;

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
                .setMainImage(getByteFromImage(dto.getMainImage()))
                .setSecondImage(getByteFromImage(dto.getSecondImage()))
                .setThirdImage(getByteFromImage(dto.getThirdImage()))
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
        result.setMainImage(getStringFromImage(entity.getMainImage()));
        result.setMainImage(getStringFromImage(entity.getSecondImage()));
        result.setMainImage(getStringFromImage(entity.getThirdImage()));
        result.setHaveMainImage(isBytesNotNull(entity.getMainImage()));
        result.setHaveSecondImage(isBytesNotNull(entity.getSecondImage()));
        result.setHaveThirdImage(isBytesNotNull(entity.getThirdImage()));
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
