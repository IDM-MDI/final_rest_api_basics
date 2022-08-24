package com.epam.esm.service.page;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.ControllerType;
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
    private static final DtoPageBuilder<TagDto> builder = new DtoPageBuilder<>();
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
    public DtoPage<TagDto> save(TagDto dto) throws RepositoryException {
        return builder
                .setResponse(responseService.createdResponse(TAG + CREATED))
                .setContent(List.of(mapper.toDto(service.save(dto))))
                .build();
    }

    @Override
    public DtoPage<TagDto> update(TagDto dto, long id) throws RepositoryException {
        dto.setId(id);
        return builder
                .setResponse(responseService.createdResponse(TAG + UPDATED))
                .setContent(List.of(mapper.toDto(service.update(dto))))
                .build();
    }

    @Transactional
    @Override
    public DtoPage<TagDto> delete(long id) throws RepositoryException {
        service.delete(id);
        return builder
                .setResponse(responseService.okResponse(TAG + DELETED))
                .build();
    }

    @Override
    public DtoPage<TagDto> findByPage(int page, int size, String sort, String direction) {
        return builder
                .setResponse(responseService.okResponse(
                        TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
                .setContent(mapper.toDtoList(service.findAll(page,size,sort,direction)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .setDirection(direction)
                .setType(ControllerType.TAG_ALL)
                .setHasNext(!service.findAll(page + 1,size,sort,direction).isEmpty())
                .build();
    }
    @Override
    public DtoPage<TagDto> findByParam(TagDto dto) throws RepositoryException {
        return builder
                .setResponse(responseService.okResponse(TAG + FOUND_BY_PARAM))
                .setContent(mapper.toDtoList(service.findByParam(dto)))
                .build();
    }

    @Override
    public DtoPage<TagDto> findByActiveStatus(int page, int size, String sort, String direction) {
        return findByStatus(page,size,sort,direction,ACTIVE.name());
    }

    @Override
    public DtoPage<TagDto> findByStatus(int page, int size, String sort, String direction, String statusName) {
        return builder
                .setResponse(responseService.okResponse(
                        TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
                .setContent(mapper.toDtoList(service.findByStatus(page,size,sort,direction,statusName)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .setDirection(direction)
                .setType(ControllerType.TAG_ALL)
                .setHasNext(!service.findByStatus(page + 1,size,sort,direction,statusName).isEmpty())
                .build();
    }

    public byte[] getImageByID(long id) throws RepositoryException {
        return service.getImageByID(id);
    }

    @Override
    public DtoPage<TagDto> findById(long id) throws RepositoryException {
        return builder
                .setResponse(responseService.okResponse(TAG + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(service.findById(id))))
                .build();
    }

    public DtoPage<TagDto> findTop(int page, int size) {
        return builder
                .setResponse(responseService.okResponse(
                        TAG + PAGE + "page " + page + ", size " + size))
                .setContent(mapper.toDtoList(service.findTop(page,size)))
                .setSize(size)
                .setNumberOfPage(page)
                .setType(ControllerType.TAG_TOP)
                .setHasNext(!service.findTop(page + 1,size).isEmpty())
                .build();
    }
}
