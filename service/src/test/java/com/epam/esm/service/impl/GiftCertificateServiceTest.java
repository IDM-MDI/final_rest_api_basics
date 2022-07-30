package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.ResponseService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import com.epam.esm.util.impl.TagModelMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.service.impl.GiftCertificateService.GIFT_CERTIFICATE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository repository = Mockito.mock(GiftCertificateRepository.class);
    @Mock
    private TagService tagService = Mockito.mock(TagService.class);;
    @Mock
    private StatusService statusService = Mockito.mock(StatusService.class);;
    @Mock
    private GiftCertificateModelMapper modelMapper = Mockito.mock(GiftCertificateModelMapper.class);
    @Mock
    private ResponseService responseService = Mockito.mock(ResponseService.class);
    @InjectMocks
    private GiftCertificateService service;

    private final GiftCertificateModelMapper mapper;
    private final ResponseService response;
    private final GiftCertificateDto dto;
    private final GiftCertificateDto dtoUpdated;
    private final GiftCertificate entity;
    private final GiftCertificate entityUpdated;
    private final List<GiftCertificateDto> dtoList;
    private final List<GiftCertificate> entityList;

    public GiftCertificateServiceTest() {
        this.mapper = new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder());
        this.response = new ResponseService();
        this.dto = new GiftCertificateDto(
                1L,
                "name",
                "description",
                new BigDecimal("0"),
                0,
                null,
                null,
                null
        );
        this.entity = mapper.toEntity(dto);
        this.dtoUpdated = new GiftCertificateDto(
                1L,
                "name 2",
                "description 2",
                new BigDecimal("2"),
                2,
                null,
                null,
                null
        );
        this.entityUpdated = mapper.toEntity(dtoUpdated);
        dtoList = List.of(
                dto,
                new GiftCertificateDto(
                        2L,
                        "name 2",
                        "description 2",
                        new BigDecimal("2"),
                        2,
                        null, null, null),
                new GiftCertificateDto(
                        3L,
                        "name 3",
                        "description 3",
                        new BigDecimal("3"),
                        3,
                        null, null, null));
        entityList = mapper.toEntityList(dtoList);
    }

    @SneakyThrows
    @Test
    void saveWithResponse() {
        ResponseDto responseDto = response.createdResponse(GIFT_CERTIFICATE + CREATED);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseService.createdResponse(anyString()))
                .thenReturn(responseDto);
        when(repository.findById(dto.getId()))
                .thenReturn(Optional.empty());
        when(modelMapper.toEntity(dto))
                .thenReturn(entity);
        when(tagService.saveAllByName(dto.getTags()))
                .thenReturn(null);
        when(repository.save(entity))
                .thenReturn(entity);
        when(modelMapper.toDto(entity))
                .thenReturn(dto);

        DtoPage<GiftCertificateDto> actual = service.saveWithResponse(dto);
        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    void deleteWithDtoPage() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + DELETED);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(null,responseDto,0,0,null);
        when(statusService.findStatus(anyString()))
                .thenReturn(null);
        doNothing()
                .when(repository)
                .setDelete(dto.getId(),null);
        when(responseService.okResponse(GIFT_CERTIFICATE + DELETED))
                .thenReturn(responseDto);
        DtoPage<GiftCertificateDto> actual = service.deleteWithDtoPage(dto.getId());
        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    void updateWithDtoPage() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + UPDATED);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dtoUpdated),responseDto,0,0,null);

        when(responseService.okResponse(GIFT_CERTIFICATE + UPDATED))
                .thenReturn(responseDto);
        when(repository.findById(dtoUpdated.getId()))
                .thenReturn(Optional.of(entity));
        when(modelMapper.toEntity(dtoUpdated))
                .thenReturn(entityUpdated);
        when(repository.save(entityUpdated))
                .thenReturn(entityUpdated);
        when(modelMapper.toDto(entityUpdated))
                .thenReturn(dtoUpdated);
        DtoPage<GiftCertificateDto> actual = service.updateWithDtoPage(dtoUpdated, dtoUpdated.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findAllWithPage() {
        int page = 1;
        int size = 1;
        String sort = "id";
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + PAGE + "page - " + page + ", size - " + size + ", sort -" + sort);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(dtoList,responseDto,size,page,sort);

        when(responseService.okResponse(GIFT_CERTIFICATE + PAGE + "page - " + page + ", size - " + size + ", sort -" + sort))
                .thenReturn(responseDto);
        when(repository.findAll(PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(new PageImpl<>(entityList));
        when(modelMapper.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<GiftCertificateDto> actual = service.findAllWithPage(page, size, sort);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByIdWithPage() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + FOUND_BY_ID);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        when(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_ID)).thenReturn(responseDto);
        when(modelMapper.toDto(entity)).thenReturn(dto);

        DtoPage<GiftCertificateDto> actual = service.findByIdWithPage(dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByParamWithDtoPage() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM);
        List<TagDto> tags = List.of(
                new TagDto(1L,"testTag1"),
                new TagDto(2L,"testTag2"),
                new TagDto(3L,"testTag3"));
        GiftCertificateDto dtoByParam = new GiftCertificateDto(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getDuration(),
                dto.getCreateDate(),
                dto.getUpdateDate(),
                tags
        );
        List<Tag> tagList = mapper.toEntity(dtoByParam).getTagList();
        List<TagDto> empty = List.copyOf(tags);
        empty.forEach(i->i.setId(null));
        GiftCertificate entityByParam = mapper.toEntity(dtoByParam);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dtoByParam),responseDto,0,0,null);

        when(modelMapper.toEntity(dtoByParam)).thenReturn(entityByParam);
        when(responseService.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM)).thenReturn(responseDto);
        when(repository.findAll(Example.of(entityByParam))).thenReturn(List.of(entityByParam));
        when(tagService.findAllByName(empty)).thenReturn(tagList);
        when(repository.findByTagListIn(tagList)).thenReturn(List.of(entityByParam));
        when(modelMapper.toDtoList(List.of(entityByParam))).thenReturn(List.of(dtoByParam));

        DtoPage<GiftCertificateDto> actual = service.findByParamWithDtoPage(dtoByParam, "testTag1,testTag2,testTag3");
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void save() {
        when(repository.findById(dto.getId()))
                .thenReturn(Optional.empty());
        when(modelMapper.toEntity(dto))
                .thenReturn(entity);
        when(tagService.saveAllByName(dto.getTags()))
                .thenReturn(null);
        when(repository.save(entity))
                .thenReturn(entity);

        GiftCertificate actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void update() {
        when(repository.findById(dtoUpdated.getId()))
                .thenReturn(Optional.of(entity));
        when(modelMapper.toEntity(dtoUpdated))
                .thenReturn(entityUpdated);
        when(repository.save(entityUpdated))
                .thenReturn(entityUpdated);

        GiftCertificate actual = service.update(dtoUpdated);
        Assertions.assertEquals(entityUpdated,actual);
    }

    @SneakyThrows
    @Test
    void findAll() {
        int page = 1;
        int size = 1;
        String sort = "id";

        when(repository.findAll(PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(new PageImpl<>(entityList));

        List<GiftCertificate> actual = service.findAll(page, size, sort);
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = dto.getId();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        GiftCertificate actual = service.findById(id);
        Assertions.assertEquals(entity,actual);
    }
}