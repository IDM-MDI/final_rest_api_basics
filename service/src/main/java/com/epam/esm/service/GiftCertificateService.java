package com.epam.esm.service;


import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.builder.impl.ResponseDtoBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftTagRepository;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.validator.GiftValidator.*;



@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class GiftCertificateService {

    private static final String UPDATED = "Gift Certificate updated";
    private static final String CREATED = "Gift Certificate created";
    private static final String DELETED = "Gift Certificate deleted";
    private static final String GIFT_EXIST = "Gift Certificate already exist";
    private final GiftCertificateRepository repository;
    private final GiftTagRepository giftTagRepository;
    private final GiftCertificateModelMapper mapper;
    private final TagService tagService;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository repository, GiftTagRepository giftTagRepository, GiftCertificateModelMapper mapper, TagService tagService) {
        this.repository = repository;
        this.giftTagRepository = giftTagRepository;
        this.mapper = mapper;
        this.tagService = tagService;
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseDto<GiftCertificateDto> save(GiftCertificateDto dto) {
        Optional<GiftCertificate> giftFromDB = repository.findByName(dto.getName());

        if(giftFromDB.isEmpty()) {
            saveGift(dto);
            return new ResponseDtoBuilder<GiftCertificateDto>()
                    .setCode(201)
                    .setText(CREATED)
                    .build();
        }
        else {
            return new ResponseDtoBuilder<GiftCertificateDto>()
                    .setCode(406)
                    .setText(GIFT_EXIST)
                    .build();
        }
    }

    public ResponseDto<GiftCertificateDto> delete(Long id) {
        repository.setDelete(id);
        return new ResponseDtoBuilder<GiftCertificateDto>()
                .setCode(200)
                .setText(DELETED)
                .build();
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseDto<GiftCertificateDto> update(GiftCertificateDto dto, Long id) throws RepositoryException {
        Optional<GiftCertificate> optionalGiftFromDB = repository.findById(id);
        if(optionalGiftFromDB.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }

        GiftCertificateDto result = mapper.toDto(repository.save(uniteGifts(dto,
                                        optionalGiftFromDB.get())));
        return new ResponseDtoBuilder<GiftCertificateDto>()
                .setCode(202)
                .setContent(result)
                .setText(UPDATED)
                .build();
    }

    public DtoPage<GiftCertificateDto> findAll(int page, int size, String sort) throws RepositoryException {
        List<GiftCertificate> giftList = repository.findAll(PageRequest.of(page, size, Sort.by(sort))).toList();
        if(giftList.size() == 0) {
            throw new RepositoryException();
        }
        return new DtoPageBuilder<GiftCertificateDto>()
                .setContent(mapper.toDtoList(giftList))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    public DtoPage<GiftCertificateDto> findById(Long id) throws RepositoryException {
        Optional<GiftCertificate> byId = repository.findById(id);
        if(byId.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }

        return new DtoPageBuilder<GiftCertificateDto>()
                .setContent(List.of(mapper.toDto(byId.get())))
                .build();
    }


    public DtoPage<GiftCertificateDto> findAllByParam(GiftCertificateDto dto,String tags) {
        GiftCertificate gift = mapper.toEntity(dto);

        return new DtoPageBuilder<GiftCertificateDto>()
                .setContent(findByParam(gift,tags))
                .build();
    }


    private List<TagDto> createTagsByString(String tags) {
        List<TagDto> result = new ArrayList<>();
        Arrays.stream(tags.split(",")).toList().forEach(i-> {
            TagDto tag = new TagDto();
            tag.setName(i.trim());
            result.add(tag);
        });
        return result;
    }

    private List<GiftCertificate> findByTagAndEntity(List<GiftCertificate> fromDB, String tags) {
        List<GiftCertificate> giftWithTag = repository.findByTagListIn(
                                                tagService.findAllByName(
                                                        createTagsByString(tags)));
        if(fromDB == null || fromDB.size() == 0) {
            return giftWithTag;
        }
        else {
            return findEquals(fromDB,giftWithTag);
        }
    }

    private GiftCertificate uniteGifts(GiftCertificateDto update,
                            GiftCertificate fromDB) {
        GiftCertificate entity = mapper.toEntity(update);
        uniteEntities(entity,fromDB);
        if(update.getTags() != null) {
            fromDB.getTagList().forEach(i-> giftTagRepository.setDeleteByGift(i.getId()));
            entity.setTagList(tagService.saveAllByName(update.getTags()));
        }
        return entity;
    }

    private void saveGift(GiftCertificateDto dto) {
        GiftCertificate entity = mapper.toEntity(dto);
        entity.setTagList(tagService.saveAllByName(dto.getTags()));
        repository.save(entity);
    }
    private List<GiftCertificateDto> findByParam(GiftCertificate entity, String tags) {
        List<GiftCertificate> result;
        if(isGiftEmpty(entity)) {
            result = new ArrayList<>();
        }
        else {
            result = repository.findAll(Example.of(entity));
        }

        if(!isTagsEmpty(tags)) {
            result = findByTagAndEntity(result,tags);
        }
        return mapper.toDtoList(result);
    }
}