package com.epam.esm.service.impl;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftTagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.IS_ALREADY_EXIST;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.*;
import static com.epam.esm.validator.GiftValidator.findEquals;
import static com.epam.esm.validator.GiftValidator.isGiftEmpty;
import static com.epam.esm.validator.GiftValidator.isStringEmpty;
import static com.epam.esm.validator.GiftValidator.uniteEntities;
import static com.epam.esm.validator.ListValidator.isListEmpty;
import static com.epam.esm.validator.TagValidator.isListTagEmpty;


@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
@Slf4j
public class GiftCertificateServiceImpl implements GiftCertificateService {
    public static final String GIFT_CERTIFICATE = "Gift Certificate ";
    private final GiftCertificateRepository repository;
    private final GiftTagRepository giftTagRepository;
    private final GiftCertificateModelMapper mapper;
    private final TagServiceImpl tagServiceImpl;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository repository,
                                      GiftTagRepository giftTagRepository,
                                      GiftCertificateModelMapper mapper,
                                      TagServiceImpl tagServiceImpl) {
        this.repository = repository;
        this.giftTagRepository = giftTagRepository;
        this.mapper = mapper;
        this.tagServiceImpl = tagServiceImpl;
    }

    @Override
    public GiftCertificate save(GiftCertificateDto dto) throws RepositoryException {
        if(checkExist(dto)) {
            log.warn(GIFT_CERTIFICATE + IS_ALREADY_EXIST);
            throw new RepositoryException(REPOSITORY_SAVE_ERROR.toString());
        }
        GiftCertificate entity = mapper.toEntity(dto);
        validateGift(entity);
        entity.setTagList(tagServiceImpl.saveAllByName(dto.getTags()));
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
        GiftCertificate uniteGifts = uniteGifts(dto, repository.findById(dto.getId()).orElse(new GiftCertificate()));
        GiftCertificate result = repository.save(uniteGifts);
        log.info("gift - {} was updated", result);
        return result;
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size, String sort, String direction) throws RepositoryException {
        List<GiftCertificate> giftList = repository
                                        .findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()),sort)))
                                        .toList();
        if(giftList.isEmpty()) {
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
    public List<GiftCertificate> findByStatus(int page, int size, String sort, String direction, String statusName) throws RepositoryException {
        return repository.findByStatus(statusName,PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()),sort)));
    }

    @Override
    public void delete(long id) throws RepositoryException {
        if(!repository.existsById(id)) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        repository.setDelete(id, DELETED.name());
    }

    @Override
    public List<GiftCertificate> findByParam(GiftCertificateDto dto) throws RepositoryException {
        List<GiftCertificate> result;
        GiftCertificate entity = mapper.toEntity(dto);

        result = findByGift(entity);
        result = findByTagAndEntity(result,entity);

        if(isListEmpty(result)) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_PARAM.toString());
        }

        return result;
    }

    public byte[] getImageByID(long id,String name) throws RepositoryException {
        GiftCertificate byId = findById(id);
        byte[] result;
        switch (name) {
            case "second" -> result = byId.getSecondImage();
            case "third" -> result = byId.getThirdImage();
            default -> result = byId.getMainImage();
        }
        return result;
    }


    public List<GiftCertificate> findByTag(long id, String status, int page, int size, String sort) throws RepositoryException {
        return repository.findByTagListInAndStatus(List.of(tagServiceImpl.findById(id)),status,PageRequest.of(page, size,Sort.by(sort)));
    }
    private GiftCertificate uniteGifts(
            GiftCertificateDto update,
            GiftCertificate fromDB) throws RepositoryException {
        GiftCertificate entity = mapper.toEntity(update);
        entity = uniteEntities(entity,fromDB);
        if(update.getTags() != null) {
            fromDB.getTagList().forEach(i-> giftTagRepository.setDeleteByGift(i.getId(), DELETED.name()));
            entity.setTagList(tagServiceImpl.saveAllByName(update.getTags()));
        }
        return entity;
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

    private List<GiftCertificate> findByTagAndEntity(List<GiftCertificate> fromDB,GiftCertificate desired) {
        if(isListTagEmpty(desired.getTagList()) && isListEmpty(fromDB)) {
            return fromDB;
        }

        GiftCertificateDto desiredDto = mapper.toDto(desired);
        List<GiftCertificate> giftWithTag = repository.findByTagListIn(
                tagServiceImpl.findAllByName(desiredDto.getTags())
        );

        if(isListEmpty(fromDB)) {
            return giftWithTag;
        }

        return findEquals(fromDB,giftWithTag);
    }

    private List<GiftCertificate> findByGift(GiftCertificate desired) {
        if(isGiftEmpty(desired)) {
            return new ArrayList<>();
        }
        return repository.findAll(Example.of(desired));
    }

    private void validateGift(GiftCertificate entity) {
        if(isStringEmpty(entity.getStatus())) {
            entity.setStatus(ACTIVE.name());
        }
    }
}