package com.epam.esm.service.impl;


import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.builder.impl.ResponseDtoBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.StatusRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.util.impl.TagModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_EXCEPTION;
import static com.epam.esm.entity.StatusName.*;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class TagService implements EntityService<Tag,TagDto> {
    private static final long DELETED_STATUS_ID = 2;
    private final TagRepository repository;
    private final StatusRepository statusRepository;
    private final TagModelMapper mapper;

    @Autowired
    public TagService(TagRepository repository, StatusRepository statusRepository, TagModelMapper mapper) {
        this.repository = repository;
        this.statusRepository = statusRepository;
        this.mapper = mapper;
    }

    public ResponseDto<TagDto> saveWithResponse(TagDto dto) throws RepositoryException {
        return new ResponseDtoBuilder<TagDto>()
                .setCode(200)
                .setText("")
                .setContent(mapper.toDto(save(dto)))
                .build();
    }

    public ResponseDto<TagDto> deleteWithResponse(Long id) {
        Optional<Status> statusOptional = statusRepository.findById(DELETED_STATUS_ID);
        statusOptional.ifPresent(status -> repository.setDelete(id, status));
        return new ResponseDtoBuilder<TagDto>()
                .setCode(402)
                .setText("")
                .build();
    }


    public DtoPage<TagDto> findAllPage(int page, int size, String sort) throws RepositoryException {
        return new DtoPageBuilder<TagDto>()
                .setContent(mapper.toDtoList(findAll(page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }
    public DtoPage<TagDto> findAllByParamPage(TagDto dto) throws RepositoryException {
        List<Tag> tagList = repository.findAll(Example.of(mapper.toEntity(dto)));
        if(tagList.size() == 0) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_EXCEPTION.toString());
        }
        return new DtoPageBuilder<TagDto>()
                .setContent(mapper.toDtoList(tagList))
                .build();
    }

    public DtoPage<TagDto> findByIdWithPage(Long id) throws RepositoryException {
        return new DtoPageBuilder<TagDto>()
                .setContent(List.of(mapper.toDto(findById(id))))
                .build();
    }

    public List<Tag> saveAllByName(List<TagDto> dtos) {
        List<Tag> validTags = new ArrayList<>();
        dtos.forEach(i -> {
            try {
                validTags.add(save(i));
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        });
        return validTags;
    }

    public List<Tag> findAllByName(List<TagDto> dtos) {
        List<Tag> result = new ArrayList<>();
        dtos.forEach(i-> repository.findByName(i.getName()).ifPresent(result::add));
        return result;
    }

    @Override
    public Tag save(TagDto dto) throws RepositoryException {
        repository.findByName(dto.getName())
                            .orElseThrow(() -> new RepositoryException(""));
        return repository.save(mapper.toEntity(dto));
    }

    @Override
    public Tag update(Tag entity) {
        return null;
    }

    @Override
    public List<Tag> findAll(int page, int size, String sort) {
        return repository.findAll(PageRequest.of(page, size, Sort.by(sort))).toList();
    }

    @Override
    public Tag findById(long id) throws RepositoryException {
        Optional<Tag> byId = repository.findById(id);
        if(byId.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        return byId.get();
    }

    @Override
    public List<Tag> findActive() {
        return findByStatus(ACTIVE.name());
    }

    @Override
    public List<Tag> findDeleted() {
        return findByStatus(DELETED.name());
    }

    @Override
    public List<Tag> findByStatus(String statusName) {
        return repository.findByStatus(statusRepository.findByNameIgnoreCase(statusName));
    }

    @Override
    public void delete(long id) {
        repository.setDelete(id,statusRepository.findByNameIgnoreCase(DELETED.name()));
    }

}
