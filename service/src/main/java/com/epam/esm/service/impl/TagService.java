package com.epam.esm.service.impl;


import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.StatusName;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.service.ResponseService;
import com.epam.esm.util.impl.TagModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.exception.RepositoryExceptionCode.*;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class TagService implements EntityService<Tag,TagDto> {
    public static final String TAG = "Tag ";
    private final TagRepository repository;
    private final StatusService statusService;
    private final TagModelMapper mapper;
    private final ResponseService responseService;

    @Autowired
    public TagService(TagRepository repository,
                      StatusService statusService,
                      TagModelMapper mapper,
                      ResponseService responseService) {
        this.repository = repository;
        this.statusService = statusService;
        this.mapper = mapper;
        this.responseService = responseService;
    }

    public DtoPage<TagDto> saveWithDtoPage(TagDto dto) {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.createdResponse(TAG + CREATED))
                .setContent(List.of(mapper.toDto(save(dto))))
                .build();
    }

    @Transactional
    public DtoPage<TagDto> deleteWithDtoPage(Long id) throws RepositoryException {
        delete(id);
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(TAG + DELETED))
                .build();
    }


    public DtoPage<TagDto> findAllPage(int page, int size, String sort) {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(
                        TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
                .setContent(mapper.toDtoList(findAll(page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }
    public DtoPage<TagDto> findAllByParamWithDtoPage(TagDto dto) throws RepositoryException {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(TAG + FOUND_BY_PARAM))
                .setContent(mapper.toDtoList(findByParam(dto)))
                .build();
    }

    public DtoPage<TagDto> findByIdWithDtoPage(Long id) throws RepositoryException {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(TAG + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(findById(id))))
                .build();
    }

    public List<Tag> saveAllByName(List<TagDto> dtos) {
        List<Tag> validTags = new ArrayList<>();
        for (TagDto dto : dtos) {
            validTags.add(save(dto));
        }
        return validTags;
    }

    public List<Tag> findAllByName(List<TagDto> dtos) {
        List<Tag> result = new ArrayList<>();
        dtos.forEach(i-> repository.findByName(i.getName()).ifPresent(result::add));
        return result;
    }

    @Override
    public Tag save(TagDto dto) {
        repository.findByName(dto.getName())
                            .ifPresent(user -> new RepositoryException(REPOSITORY_WRONG_NAME.toString()));
        return repository.save(mapper.toEntity(dto));
    }

    @Override
    public Tag update(TagDto dto) {
        return null;
    }

    @Override
    public List<Tag> findAll(int page, int size, String sort) {
        return repository.findAll(PageRequest.of(page, size, Sort.by(sort))).toList();
    }

    @Override
    public Tag findById(long id) throws RepositoryException {
        return repository.findById(id)
                         .orElseThrow(() -> new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString()));
    }

    @Override
    public List<Tag> findByParam(TagDto dto) throws RepositoryException {
        List<Tag> tags = repository.findAll(Example.of(mapper.toEntity(dto)));
        if(tags.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_EXCEPTION.toString());
        }
        return tags;
    }

    @Override
    public List<Tag> findActive() throws RepositoryException {
        return findByStatus(ACTIVE.name());
    }

    @Override
    public List<Tag> findDeleted() throws RepositoryException {
        return findByStatus(StatusName.DELETED.name());
    }

    @Override
    public List<Tag> findByStatus(String statusName) throws RepositoryException {
        return repository.findByStatus(statusService.findStatus(statusName));
    }

    @Override
    public void delete(long id) throws RepositoryException {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString()));
        if(tag.getStatus()
                .getName()
                .toUpperCase()
                .equals(StatusName.DELETED.name())) {
            return;
        }
        repository.setDelete(id,statusService.findStatus(StatusName.DELETED.name()));
    }

}
