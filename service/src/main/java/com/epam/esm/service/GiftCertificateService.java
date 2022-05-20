package com.epam.esm.service;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Map;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class GiftCertificateService {

    private final GiftCertificateRepository repository;
    private final GiftCertificateModelMapper mapper;
    private final TagService tagService;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository repository, Map<String, List<String>> allColumns, GiftCertificateModelMapper mapper, TagService tagService) {
        this.repository = repository;
        this.mapper = mapper;
        this.tagService = tagService;
    }

    public GiftCertificate save(GiftCertificateDto dto) {
        GiftCertificate entity = mapper.toEntity(dto);
        GiftCertificate giftFromDB = repository.findByName(entity.getName());

        if(giftFromDB == null) {
            List<TagDto> tagDtos = dto.getTags();
            entity.setTagList(tagService.findAllByName(tagDtos));
            return repository.save(entity);
        }
        else {
            return giftFromDB;
        }
    }

    public long delete(Long id) {
        return repository.setDelete(id);
    }

    public GiftCertificate update(GiftCertificateDto dto, Long id) {
        List<TagDto> tagDtos = dto.getTags();
        GiftCertificate entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setTagList(tagService.findAllByName(tagDtos));
        return repository.save(entity);
    }

    public DtoPage<GiftCertificateDto> findAll(int page, int size, String sort) {
        List<GiftCertificate> giftList = repository.findAll(PageRequest.of(page, size, Sort.by(sort))).toList();
        DtoPage<GiftCertificateDto> dtoPage = new DtoPage<>();
        dtoPage.setContent(mapper.toDtoList(giftList));
        dtoPage.setNumberOfPage(page);
        dtoPage.setSize(size);
        dtoPage.setSortBy(sort);
        return dtoPage;
    }

    public GiftCertificateDto findById(Long id) {
        return mapper.toDto(repository.findById(id).get());
    }

    public List<GiftCertificateDto> findByParam(Map<String,String> param) {
//        return mapper.toDtoList(repository.findGiftCertificatesByIdAndNameAndDescriptionAndPriceAndDurationAndCreate_dateAndUpdate_date(
//                Long.parseLong(param.get("id")),
//                param.get("name"),param.get("description"),
//                new BigDecimal(param.get("price")), Integer.parseInt(param.get("duration")),
//                LocalDateTime.parse(param.get("createDate")),LocalDateTime.parse(param.get("updateDate"))
//        ));
        return null;
    }

}