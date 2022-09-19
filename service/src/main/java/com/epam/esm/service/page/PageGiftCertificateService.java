package com.epam.esm.service.page;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.ControllerType;
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
import java.util.Arrays;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
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
                .setType(ControllerType.CERTIFICATE_ADD)
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> delete(long id) throws RepositoryException {
        service.delete(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + DELETED))
                .setType(ControllerType.CERTIFICATE_DELETE)
                .build();
    }

    @Transactional(rollbackFor = SQLException.class)
    @Override
    public DtoPage<GiftCertificateDto> update(GiftCertificateDto dto, long id) throws RepositoryException {
        dto.setId(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + UPDATED))
                .setContent(List.of(mapper.toDto(service.update(dto))))
                .setType(ControllerType.CERTIFICATE_UPDATE)
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findByPage(int page, int size, String sort, String direction) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,page,size,sort,direction)))
                .setContent(mapper.toDtoList(service.findAll(page,size,sort,direction)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .setDirection(direction)
                .setType(ControllerType.CERTIFICATE_BY_PAGE)
                .setHasNext(!service.findAll(page + 1,size,sort,direction).isEmpty())
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findById(long id) throws RepositoryException {
        GiftCertificate result = service.findById(id);
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(result)))
                .setType(ControllerType.CERTIFICATE_BY_ID)
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findByParam(GiftCertificateDto dto) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM))
                .setContent(mapper.toDtoList(service.findByParam(dto)))
                .setType(ControllerType.CERTIFICATE_BY_PARAM)
                .build();
    }

    @Override
    public DtoPage<GiftCertificateDto> findByActiveStatus(int page, int size, String sort, String direction) throws RepositoryException {
        return findByStatus(page,size,sort,direction,ACTIVE.name());
    }

    @Override
    public DtoPage<GiftCertificateDto> findByStatus(int page, int size, String sort, String direction,String statusName) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,page,size,sort,direction)))
                .setContent(mapper.toDtoList(service.findByStatus(page,size,sort,direction,statusName)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .setDirection(direction)
                .setParam(statusName)
                .setType(ControllerType.CERTIFICATE_ALL)
                .setHasNext(!service.findByStatus(page + 1,size,sort,direction,statusName).isEmpty())
                .build();
    }

    public DtoPage<GiftCertificateDto> findByParam(GiftCertificateDto dto, String tags) throws RepositoryException {
        dto.setTags(createTagsByString(tags));
        return findByParam(dto);
    }
    public byte[] getImageByID(long id,String name) throws RepositoryException {
        return service.getImageByID(id,name);
    }

    public DtoPage<GiftCertificateDto> findActiveByTag(long id, Integer page, Integer size, String sort, String direction) throws RepositoryException {
        return findByTag(id,ACTIVE.name(),page,size,sort,direction);
    }

    public DtoPage<GiftCertificateDto> findByTag(long id, String status, Integer page, Integer size, String sort, String direction) throws RepositoryException {
        return new DtoPageBuilder<GiftCertificateDto>()
                .setResponse(responseService.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,page,size,sort,direction)))
                .setContent(mapper.toDtoList(service.findByTag(id,status,page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .setDirection(direction)
                .setParam(Long.toString(id))
                .setHasNext(!service.findByTag(id,status,page + 1,size,sort).isEmpty())
                .setType(ControllerType.CERTIFICATE_BY_TAG)
                .build();
    }

    private List<TagDto> createTagsByString(String tags) {
        return !tags.isBlank() ? Arrays.stream(tags.split(","))
                .map(tagName -> {
                    TagDto tag = new TagDto();
                    tag.setName(tagName);
                    return tag;
                })
                .toList()
                :
                null;
    }
}
