package com.epam.esm.service.impl;


import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
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

import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_EXCEPTION;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_WRONG_NAME;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class TagServiceImpl implements TagService {
    public static final String TAG = "Tag ";
    private final TagRepository repository;
    private final TagModelMapper mapper;

    @Autowired
    public TagServiceImpl(TagRepository repository,
                          TagModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
    }       //TODO MAKE UPDATE TO TAG

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
    public List<Tag> findByStatus(int page, int size, String sort, String statusName) {
        return repository.findByStatus(statusName,PageRequest.of(page, size, Sort.by(sort)));
    }

    @Override
    public void delete(long id) throws RepositoryException {
        if(!repository.existsById(id)) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        repository.setDelete(id, DELETED.name());
    }

}
