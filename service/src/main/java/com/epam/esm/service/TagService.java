package com.epam.esm.service;


import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.impl.TagModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.validator.TagValidator.isTagEqualsToDB;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class TagService {

    private final TagRepository repository;
    private final TagModelMapper mapper;

    @Autowired
    public TagService(TagRepository repository, TagModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Tag save(TagDto dto) {
        Tag entity = mapper.toEntity(dto);
        Tag entityFromDB = repository.findByName(entity.getName());
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException exception) {
            return entityFromDB;
        }
    }

    public long delete(Long id) {
        return repository.setDelete(id);
    }


    public List<TagDto> findAll() {
        return mapper.toDtoList((List<Tag>) repository.findAll());
    }

    public TagDto findById(Long id) {
        return mapper.toDto(repository.findById(id).get());
    }

    public List<Tag> findAllByName(List<TagDto> dtos) {
        List<Tag> validTags = new ArrayList<>();
        dtos.forEach(i -> validTags.add(save(i)));
        return validTags;
    }
}
