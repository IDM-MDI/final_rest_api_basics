package com.epam.esm.service.impl;


import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.StatusName;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftTagRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.service.ResponseService;
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

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.exception.RepositoryExceptionCode.*;
import static com.epam.esm.validator.GiftValidator.*;
import static com.epam.esm.dto.ResponseTemplate.*;


@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
@Slf4j
public class GiftCertificateService implements EntityService<GiftCertificate,GiftCertificateDto> {

    public static final String GIFT_CERTIFICATE = "Gift Certificate ";
    private final GiftCertificateRepository repository;
    private final GiftTagRepository giftTagRepository;
    private final GiftCertificateModelMapper mapper;
    private final TagService tagService;
    private final ResponseService responseService;
    private final StatusService statusService;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository repository,
                                  GiftTagRepository giftTagRepository,
                                  GiftCertificateModelMapper mapper,
                                  TagService tagService,
                                  ResponseService responseService,
                                  StatusService statusService) {
        this.repository = repository;
        this.giftTagRepository = giftTagRepository;
        this.statusService = statusService;
        this.mapper = mapper;
        this.tagService = tagService;
        this.responseService = responseService;
    }

    @Transactional(rollbackFor = SQLException.class)
    public DtoPage<GiftCertificateDto> saveWithResponse(GiftCertificateDto dto) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.createdResponse(GIFT_CERTIFICATE + CREATED))
                .setContent(List.of(mapper.toDto(save(dto))))
                .build();
    }

    public DtoPage<GiftCertificateDto> deleteWithDtoPage(Long id) throws RepositoryException {
        delete(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + DELETED))
                .build();
    }

    @Transactional(rollbackFor = SQLException.class)
    public DtoPage<GiftCertificateDto> updateWithDtoPage(GiftCertificateDto dto, Long id) throws RepositoryException {
        dto.setId(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + UPDATED))
                .setContent(List.of(mapper.toDto(update(dto))))
                .build();
    }

    public DtoPage<GiftCertificateDto> findAllWithPage(int page, int size, String sort) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(
                        GIFT_CERTIFICATE + PAGE + "page - " + page + ", size - " + size + ", sort -" + sort
                ))
                .setContent(mapper.toDtoList(findAll(page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    public DtoPage<GiftCertificateDto> findByIdWithPage(Long id) throws RepositoryException {
        GiftCertificate result = findById(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(result)))
                .build();
    }


    public DtoPage<GiftCertificateDto> findByParamWithDtoPage(GiftCertificateDto dto, String tags) {
        GiftCertificate gift = mapper.toEntity(dto);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM))
                .setContent(findByParam(gift,tags))
                .build();
    }


    @Override
    public GiftCertificate save(GiftCertificateDto dto) throws RepositoryException {
        if(checkExist(dto)) {
            log.warn(GIFT_CERTIFICATE + IS_ALREADY_EXIST);
            throw new RepositoryException(REPOSITORY_SAVE_ERROR.toString());
        }
        GiftCertificate entity = mapper.toEntity(dto);
        entity.setTagList(tagService.saveAllByName(dto.getTags()));
        GiftCertificate result = repository.save(entity);
        log.info("gift - {} was saved", result);
        return result;
    }

    @Override
    public GiftCertificate update(GiftCertificateDto dto) throws RepositoryException {
        if(!checkExist(dto)) {
            log.warn(REPOSITORY_NOTHING_FIND_BY_ID.toString());
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        GiftCertificate uniteGifts = uniteGifts(dto, repository.findById(dto.getId()).get());
        GiftCertificate result = repository.save(uniteGifts);
        log.info("gift - {} was updated", result);
        return result;
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size, String sort) throws RepositoryException {
        List<GiftCertificate> giftList = repository
                                        .findAll(PageRequest.of(page, size, Sort.by(sort)))
                                        .toList();
        if(giftList.size() == 0) {
            log.error(REPOSITORY_NOTHING_FIND_EXCEPTION.toString());
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_EXCEPTION.toString());
        }
        return giftList;
    }

    @Override
    public GiftCertificate findById(long id) throws RepositoryException {
        return repository.findById(id)
                .orElseThrow(()-> new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString()));
    }

    @Override
    public List<GiftCertificate> findActive() throws RepositoryException {
        return findByStatus(ACTIVE.name());
    }

    @Override
    public List<GiftCertificate> findDeleted() throws RepositoryException {
        return findByStatus(StatusName.DELETED.name());
    }

    @Override
    public List<GiftCertificate> findByStatus(String statusName) throws RepositoryException {
        return repository.findByStatus(statusService.findStatus(statusName));
    }

    @Override
    public void delete(long id) throws RepositoryException {
        repository.setDelete(id, statusService.findStatus(StatusName.DELETED.name()));
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
                                                tagService.findAllByName(createTagsByString(tags)));
        if(fromDB == null || fromDB.size() == 0) {
        return giftWithTag;
        }
        return findEquals(fromDB,giftWithTag);
    }

    private GiftCertificate uniteGifts(GiftCertificateDto update,
                            GiftCertificate fromDB) throws RepositoryException {
        GiftCertificate entity = mapper.toEntity(update);
        uniteEntities(entity,fromDB);
        if(update.getTags() != null) {
            Status status = statusService.findStatus(StatusName.DELETED.name());
            fromDB.getTagList().forEach(i-> giftTagRepository.setDeleteByGift(i.getId(), status));
            entity.setTagList(tagService.saveAllByName(update.getTags()));
        }
        return entity;
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

    private boolean checkExist(GiftCertificateDto dto) throws RepositoryException {
        if(dto == null || (dto.getId() == null && dto.getName() == null)) {
            throw new RepositoryException(REPOSITORY_NULL_POINTER.toString());
        }
        if(dto.getId() != null) {
            return repository.findById(dto.getId()).isPresent();
        }
        return repository.findByName(dto.getName()).isPresent();
    }

    @Override
    public List<GiftCertificate> findByParam(GiftCertificateDto dto) throws RepositoryException {
        return null;
    }
}