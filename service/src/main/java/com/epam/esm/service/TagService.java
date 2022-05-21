package com.epam.esm.service;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
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
import java.util.stream.Collectors;

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

        if(entityFromDB == null) {
            return repository.save(entity);
        }
        else {
            return entityFromDB;
        }
    }

    public long delete(Long id) {
        return repository.setDelete(id);
    }


    public DtoPage<TagDto> findAll(int page, int size, String sort) {
        List<Tag> tagList = repository.findAll(PageRequest.of(page, size, Sort.by(sort))).toList();
        DtoPage<TagDto> dtoPage = new DtoPage<>();
        dtoPage.setContent(mapper.toDtoList(tagList));
        dtoPage.setNumberOfPage(page);
        dtoPage.setSize(size);
        dtoPage.setSortBy(sort);
        return dtoPage;
    }
    public DtoPage<TagDto> findAllByParam(TagDto dto) {
        Tag tag = mapper.toEntity(dto);
        List<Tag> tagList = repository.findAll(Example.of(tag));
        DtoPage<TagDto> dtoPage = new DtoPage<>();
        dtoPage.setContent(mapper.toDtoList(tagList));
        return dtoPage;
    }

    public DtoPage<TagDto> findById(Long id) {
        DtoPage<TagDto> dtoPage = new DtoPage<>();
        dtoPage.setContent(List.of(mapper.toDto(repository.findById(id).get())));
        return dtoPage;
    }

    public List<Tag> saveAllByName(List<TagDto> dtos) {
        List<Tag> validTags = new ArrayList<>();
        dtos.forEach(i -> validTags.add(save(i)));
        return validTags;
    }

    public List<Tag> findAllByName(List<TagDto> dtos) {
        List<Tag> result = new ArrayList<>();
        dtos.forEach(i-> result.add(repository.findByName(i.getName())));
        return result;
    }
}
