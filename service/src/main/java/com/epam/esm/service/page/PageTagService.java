package com.epam.esm.service.page;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.PageService;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.util.impl.TagModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.service.impl.TagServiceImpl.TAG;

@Service
public class PageTagService implements PageService<DtoPage<TagDto>,TagDto> {
    private final TagServiceImpl service;
    private final TagModelMapper mapper;
    private final ResponseService responseService;

    @Autowired
    public PageTagService(TagServiceImpl service, TagModelMapper mapper, ResponseService responseService) {
        this.service = service;
        this.mapper = mapper;
        this.responseService = responseService;
    }

    @Override
    public DtoPage<TagDto> save(TagDto dto) {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.createdResponse(TAG + CREATED))
                .setContent(List.of(mapper.toDto(service.save(dto))))
                .build();
    }

    @Override
    public DtoPage<TagDto> update(TagDto dto, long id) {
        dto.setId(id);
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.createdResponse(TAG + UPDATED))
                .setContent(List.of(mapper.toDto(service.update(dto))))
                .build();
    }

    @Transactional
    @Override
    public DtoPage<TagDto> delete(long id) throws RepositoryException {
        service.delete(id);
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(TAG + DELETED))
                .build();
    }

    @Override
    public DtoPage<TagDto> findByPage(int page, int size, String sort) {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(
                        TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
                .setContent(mapper.toDtoList(service.findAll(page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }
    @Override
    public DtoPage<TagDto> findByParam(TagDto dto) throws RepositoryException {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(TAG + FOUND_BY_PARAM))
                .setContent(mapper.toDtoList(service.findByParam(dto)))
                .build();
    }

    @Override
    public DtoPage<TagDto> findByActiveStatus(int page, int size, String sort) {
        return findByStatus(page,size,sort,ACTIVE.name());
    }

    @Override
    public DtoPage<TagDto> findByStatus(int page, int size, String sort, String statusName) {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(
                        TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
                .setContent(mapper.toDtoList(service.findByStatus(page,size,sort,statusName)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    @Override
    public DtoPage<TagDto> findById(long id) throws RepositoryException {
        return new DtoPageBuilder<TagDto>()
                .setResponse(responseService.okResponse(TAG + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(service.findById(id))))
                .build();
    }
}
