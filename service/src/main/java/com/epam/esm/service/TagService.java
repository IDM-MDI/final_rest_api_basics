package com.epam.esm.service;


import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.builder.impl.ResponseDtoBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
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
import java.util.Optional;

import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_EXCEPTION;

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
        return repository.findByName(dto.getName())
                .orElseGet(() -> repository.save(mapper.toEntity(dto)));
    }

    public ResponseDto<TagDto> delete(Long id) {
        repository.setDelete(id);
        return new ResponseDtoBuilder<TagDto>()
                .setCode(402)
                .setText("")
                .build();
    }


    public DtoPage<TagDto> findAll(int page, int size, String sort) {
        List<Tag> tagList = repository.findAll(PageRequest.of(page, size, Sort.by(sort))).toList();
        return new DtoPageBuilder<TagDto>()
                .setContent(mapper.toDtoList(tagList))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }
    public DtoPage<TagDto> findAllByParam(TagDto dto) throws RepositoryException {
        List<Tag> tagList = repository.findAll(Example.of(mapper.toEntity(dto)));
        if(tagList.size() == 0) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_EXCEPTION.toString());
        }
        return new DtoPageBuilder<TagDto>()
                .setContent(mapper.toDtoList(tagList))
                .build();
    }

    public DtoPage<TagDto> findById(Long id) throws RepositoryException {
        Optional<Tag> byId = repository.findById(id);
        if(byId.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        return new DtoPageBuilder<TagDto>()
                .setContent(List.of(mapper.toDto(byId.get())))
                .build();
    }

    public List<Tag> saveAllByName(List<TagDto> dtos) {
        List<Tag> validTags = new ArrayList<>();
        dtos.forEach(i -> validTags.add(save(i)));
        return validTags;
    }

    public List<Tag> findAllByName(List<TagDto> dtos) {
        List<Tag> result = new ArrayList<>();
        dtos.forEach(i-> repository.findByName(i.getName()).ifPresent(result::add));
        return result;
    }
}
