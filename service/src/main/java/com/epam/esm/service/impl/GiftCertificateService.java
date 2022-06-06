package com.epam.esm.service.impl;


import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.builder.impl.ResponseDtoBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Status;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftTagRepository;
import com.epam.esm.repository.StatusRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GiftCertificateService implements EntityService<GiftCertificate,GiftCertificateDto> {

    private static final String UPDATED = "Gift Certificate updated";
    private static final String CREATED = "Gift Certificate created";
    private static final String DELETED = "Gift Certificate deleted";
    private static final String GIFT_EXIST = "Gift Certificate already exist";
    private static final long DELETED_STATUS_ID = 2;
    private final GiftCertificateRepository repository;
    private final GiftTagRepository giftTagRepository;
    private final StatusRepository statusRepository;
    private final GiftCertificateModelMapper mapper;
    private final TagService tagService;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository repository, GiftTagRepository giftTagRepository, StatusRepository statusRepository, GiftCertificateModelMapper mapper, TagService tagService) {
        this.repository = repository;
        this.giftTagRepository = giftTagRepository;
        this.statusRepository = statusRepository;
        this.mapper = mapper;
        this.tagService = tagService;
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseDto<GiftCertificateDto> saveWithResponse(GiftCertificateDto dto) throws RepositoryException {
        save(dto);
        return new ResponseDtoBuilder<GiftCertificateDto>()
                .setCode(201)
                .setText(CREATED)
                .build();
    }

    public ResponseDto<GiftCertificateDto> deleteWithResponse(Long id) {
        delete(id);
        return new ResponseDtoBuilder<GiftCertificateDto>()
                .setCode(200)
                .setText(DELETED)
                .build();
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseDto<GiftCertificateDto> updateWithResponse(GiftCertificateDto dto, Long id) throws RepositoryException {
        dto.setId(id);
        GiftCertificateDto result = mapper.toDto(update(mapper.toEntity(dto)));
        return new ResponseDtoBuilder<GiftCertificateDto>()
                .setCode(202)
                .setContent(result)
                .setText(UPDATED)
                .build();
    }

    public DtoPage<GiftCertificateDto> findAllWithPage(int page, int size, String sort) throws RepositoryException {
        List<GiftCertificate> result = findAll(page,size,sort);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setContent(mapper.toDtoList(result))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    public DtoPage<GiftCertificateDto> findByIdWithPage(Long id) throws RepositoryException {
        GiftCertificate result = findById(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setContent(List.of(mapper.toDto(result)))
                .build();
    }


    public DtoPage<GiftCertificateDto> findByParamWithPage(GiftCertificateDto dto, String tags) {
        GiftCertificate gift = mapper.toEntity(dto);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setContent(findByParamWithPage(gift,tags))
                .build();
    }


    @Override
    public GiftCertificate save(GiftCertificateDto dto) throws RepositoryException {
        Optional<GiftCertificate> giftFromDB = repository.findByName(dto.getName());
        if(giftFromDB.isPresent()) {
            log.warn("NOTHING FIND BY ID - GIFT SAVE");
            throw new RepositoryException();
        }
        GiftCertificate entity = mapper.toEntity(dto);
        entity.setTagList(tagService.saveAllByName(dto.getTags()));
        GiftCertificate result = repository.save(entity);
        log.info("gift - {} was saved", result);
        return result;
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) throws RepositoryException {
        Optional<GiftCertificate> optionalGiftFromDB = repository.findById(entity.getId());
        GiftCertificateDto dto = mapper.toDto(entity);
        if(optionalGiftFromDB.isEmpty()) {
            log.warn("NOTHING FIND BY ID - GIFT UPDATE");
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }

        GiftCertificate result = repository.save(uniteGifts(dto,
                optionalGiftFromDB.get()));
        log.info("gift - {} was updated", result);
        return result;
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size, String sort) throws RepositoryException {
        List<GiftCertificate> giftList = repository.findAll(PageRequest.of(page, size, Sort.by(sort)))
                .toList();
        if(giftList.size() == 0) {
            throw new RepositoryException();
        }
        return giftList;
    }

    @Override
    public GiftCertificate findById(long id) throws RepositoryException {
        Optional<GiftCertificate> byId = repository.findById(id);
        if(byId.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        return byId.get();
    }

    @Override
    public List<GiftCertificate> findActive() {
        String active = "ACTIVE";
        return findByStatus(active);
    }

    @Override
    public List<GiftCertificate> findDeleted() {
        String deleted = "DELETED";
        return findByStatus(deleted);
    }

    @Override
    public List<GiftCertificate> findByStatus(String statusName) {
        return repository.findByStatus(statusRepository.findByNameIgnoreCase(statusName));
    }

    @Override
    public void delete(long id) {
        Optional<Status> statusOptional = statusRepository.findById(DELETED_STATUS_ID);
        statusOptional.ifPresent(status -> repository.setDelete(id, status));
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
            Optional<Status> statusOptional = statusRepository.findById(DELETED_STATUS_ID);
            fromDB.getTagList().forEach(i-> {
                statusOptional.ifPresent(status -> giftTagRepository.setDeleteByGift(i.getId(), status));
            });
            entity.setTagList(tagService.saveAllByName(update.getTags()));
        }
        return entity;
    }

    private List<GiftCertificateDto> findByParamWithPage(GiftCertificate entity, String tags) {
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