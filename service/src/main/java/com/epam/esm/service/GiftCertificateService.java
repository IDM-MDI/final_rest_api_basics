package com.epam.esm.service;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
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
    public void save(GiftCertificateDto dto) {
        GiftCertificate entity = mapper.toEntity(dto);
        GiftCertificate giftFromDB = repository.findByName(entity.getName());

        if(giftFromDB == null) {
            List<TagDto> tagDtos = dto.getTags();
            entity.setTagList(tagService.saveAllByName(tagDtos));
            repository.save(entity);
        }
    }

    public void delete(Long id) {
        repository.setDelete(id);
    }

    @Transactional(rollbackFor = SQLException.class)
    public void update(GiftCertificateDto dto, Long id) throws RepositoryException {
        Optional<GiftCertificate> optionalGift = repository.findById(id);
        if(optionalGift.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        GiftCertificate giftFromDB = optionalGift.get();
        GiftCertificate entity = mapper.toEntity(dto);
        uniteEntities(entity,giftFromDB);
        if(dto.getTags() != null) {
            giftFromDB.getTagList().forEach(i-> giftTagRepository.setDeleteByGift(i.getId()));
            entity.setTagList(tagService.saveAllByName(dto.getTags()));
        }
        repository.save(entity);
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

    public DtoPage<GiftCertificateDto> findById(Long id) throws RepositoryException {
        DtoPage<GiftCertificateDto> dtoPage = new DtoPage<>();
        Optional<GiftCertificate> byId = repository.findById(id);

        if(byId.isEmpty())
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());

        dtoPage.setContent(List.of(mapper.toDto(byId.get())));
        return dtoPage;
    }


    public DtoPage<GiftCertificateDto> findAllByParam(GiftCertificateDto dto,String tags) {
        DtoPage<GiftCertificateDto> dtoPage = new DtoPage<>();
        GiftCertificate gift = mapper.toEntity(dto);
        gift.setTagList(null);
        List<GiftCertificate> result;
        if(isGiftEmpty(gift))
            result = new ArrayList<>();
        else
            result = repository.findAll(Example.of(gift));

        if(!isTagsEmpty(tags))
            result = findByTagAndEntity(result,tags);

        dtoPage.setContent(mapper.toDtoList(result));
        return dtoPage;
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

    private List<GiftCertificate> findByTagAndEntity(List<GiftCertificate> find, String tags) {
        List<Tag> tagList = tagService.findAllByName(createTagsByString(tags));
        List<GiftCertificate> giftWithTag = repository.findByTagListIn(tagList);
        if(find == null || find.size() == 0)
            return giftWithTag;
        else
            return findEquals(find,giftWithTag);
    }
}