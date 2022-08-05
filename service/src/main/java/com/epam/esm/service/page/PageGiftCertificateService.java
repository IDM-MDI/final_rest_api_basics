package com.epam.esm.service.page;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.PageService;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.dto.ResponseTemplate.FOUND_BY_PARAM;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.service.impl.GiftCertificateServiceImpl.GIFT_CERTIFICATE;

@Service
public class PageGiftCertificateService implements PageService<DtoPage<GiftCertificateDto>,GiftCertificateDto> {
    private final GiftCertificateServiceImpl service;
    private final ResponseService responseService;
    private final GiftCertificateModelMapper mapper;

    @Autowired
    public PageGiftCertificateService(GiftCertificateServiceImpl service, ResponseService responseService, GiftCertificateModelMapper mapper) {
        this.service = service;
        this.responseService = responseService;
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = SQLException.class)
    @Override
    public DtoPage<GiftCertificateDto> save(GiftCertificateDto dto) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.createdResponse(GIFT_CERTIFICATE + CREATED))
                .setContent(List.of(mapper.toDto(service.save(dto))))
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> delete(long id) throws RepositoryException {
        service.delete(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + DELETED))
                .build();
    }

    @Transactional(rollbackFor = SQLException.class)
    @Override
    public DtoPage<GiftCertificateDto> update(GiftCertificateDto dto, long id) throws RepositoryException {
        dto.setId(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + UPDATED))
                .setContent(List.of(mapper.toDto(service.update(dto))))
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findByPage(int page, int size, String sort) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(
                        GIFT_CERTIFICATE + PAGE + "page - " + page + ", size - " + size + ", sort -" + sort
                ))
                .setContent(mapper.toDtoList(service.findAll(page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findById(long id) throws RepositoryException {
        GiftCertificate result = service.findById(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(result)))
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findByParam(GiftCertificateDto dto) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM))
                .setContent(mapper.toDtoList(service.findByParam(dto)))
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findByActiveStatus(int page, int size, String sort) throws RepositoryException {
        return findByStatus(page,size,sort,ACTIVE.name());
    }

    @Override
    public DtoPage<GiftCertificateDto> findByStatus(int page, int size, String sort, String statusName) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(
                        GIFT_CERTIFICATE + PAGE + "page - " + page + ", size - " + size + ", sort -" + sort
                ))
                .setContent(mapper.toDtoList(service.findByStatus(page,size,sort,statusName)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    public DtoPage<GiftCertificateDto> findByParam(GiftCertificateDto dto, String tags) throws RepositoryException {
        dto.setTags(createTagsByString(tags));
        return findByParam(dto);
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
}
