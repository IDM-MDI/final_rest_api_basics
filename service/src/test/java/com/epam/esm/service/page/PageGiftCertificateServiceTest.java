package com.epam.esm.service.page;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.StatusName;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.service.impl.GiftCertificateServiceImpl.GIFT_CERTIFICATE;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageGiftCertificateServiceTest {

    private static final int PAGE = 0;
    private static final int SIZE = 0;
    private static final String SORT = "ID";
    private static final String DIRECTION = "asc";

    @Mock
    private GiftCertificateServiceImpl serviceMock;
    @Mock
    private GiftCertificateModelMapper mapperMock;
    @Mock
    private ResponseService responseServiceMock = Mockito.mock(ResponseService.class);

    @InjectMocks
    private PageGiftCertificateService service;

    private final GiftCertificateModelMapper mapper;
    private final ResponseService response;
    private final GiftCertificateDto dto;
    private final GiftCertificateDto dtoUpdated;
    private final GiftCertificate entity;
    private final GiftCertificate entityUpdated;
    private final List<GiftCertificateDto> dtoList;
    private final List<GiftCertificate> entityList;

    public PageGiftCertificateServiceTest() {
        this.mapper = new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder());
        this.response = new ResponseService();
        this.dto = new GiftCertificateDto(
                1L,
                "name",
                "description",
                new BigDecimal("0"),
                0,
                "shop",
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                "testStatus"
        );
        this.entity = mapper.toEntity(dto);
        this.dtoUpdated = new GiftCertificateDto(
                1L,
                "name 2",
                "description 2",
                new BigDecimal("2"),
                2,
                "shop 2",
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                "testStatus"
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
                        "shop 2",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        false,
                        false,
                        false,
                        "testStatus"),
                new GiftCertificateDto(
                        3L,
                        "name 3",
                        "description 3",
                        new BigDecimal("3"),
                        3,
                        "shop 3",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        false,
                        false,
                        false,
                        "testStatus"));
        entityList = mapper.toEntityList(dtoList);

    }

    @SneakyThrows
    @Test
    void save() {
        ResponseDto responseDto = response.createdResponse(GIFT_CERTIFICATE + CREATED);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null,null,false,null, ControllerType.CERTIFICATE_ADD);

        when(responseServiceMock.createdResponse(GIFT_CERTIFICATE + CREATED))
                .thenReturn(responseDto);
        when(serviceMock.save(dto)).thenReturn(entity);
        when(mapperMock.toDto(entity))
                .thenReturn(dto);

        DtoPage<GiftCertificateDto> actual = service.save(dto);
        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    void delete() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + DELETED);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(null,responseDto,0,0,null,null,false,null,ControllerType.CERTIFICATE_DELETE);
        when(responseServiceMock.okResponse(GIFT_CERTIFICATE + DELETED))
                .thenReturn(responseDto);
        doNothing()
                .when(serviceMock)
                .delete(dto.getId());
        when(responseServiceMock.okResponse(GIFT_CERTIFICATE + DELETED))
                .thenReturn(responseDto);
        DtoPage<GiftCertificateDto> actual = service.delete(dto.getId());
        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    void update() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + UPDATED);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dtoUpdated),responseDto,0,0,null,null,false,null,ControllerType.CERTIFICATE_UPDATE);

        when(responseServiceMock.okResponse(GIFT_CERTIFICATE + UPDATED))
                .thenReturn(responseDto);
        when(serviceMock.update(dto))
                .thenReturn(entityUpdated);
        when(mapperMock.toDto(entityUpdated))
                .thenReturn(dtoUpdated);
        DtoPage<GiftCertificateDto> actual = service.update(dto, dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByPage() {
        ResponseDto responseDto = response.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT,DIRECTION,false,null,ControllerType.CERTIFICATE_BY_PAGE);

        when(responseServiceMock.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findAll(PAGE, SIZE, SORT, DIRECTION))
                .thenReturn(entityList);
        when(serviceMock.findAll(PAGE + 1, SIZE, SORT, DIRECTION))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<GiftCertificateDto> actual = service.findByPage(PAGE, SIZE, SORT, DIRECTION);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findById() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + FOUND_BY_ID);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null,null,false,null,ControllerType.CERTIFICATE_BY_ID);

        when(responseServiceMock.okResponse(GIFT_CERTIFICATE + FOUND_BY_ID)).thenReturn(responseDto);
        when(serviceMock.findById(dto.getId())).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        DtoPage<GiftCertificateDto> actual = service.findById(dto.getId());
        Assertions.assertEquals(expected,actual);
    }
    @SneakyThrows
    @Test
    void findByParam() {
        GiftCertificateDto giftByParam = new GiftCertificateDto(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getDuration(),
                dto.getShop(),
                null,
                null,
                null,
                dto.getCreateDate(),
                dto.getUpdateDate(),
                List.of(
                        new TagDto(null,"testTag1",null,false,null),
                        new TagDto(null,"testTag2",null,false,null),
                        new TagDto(null,"testTag3",null,false,null)
                ),
                false,false,false,
                dto.getStatus()
        );
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(giftByParam),responseDto,0,0,null,null,false,null,ControllerType.CERTIFICATE_BY_PARAM);


        when(serviceMock.findByParam(dto)).thenReturn(List.of(entity));
        when(responseServiceMock.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM)).thenReturn(responseDto);
        when(mapperMock.toDtoList(List.of(entity))).thenReturn(List.of(giftByParam));

        DtoPage<GiftCertificateDto> actual = service.findByParam(dto, "testTag1,testTag2,testTag3");
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByActiveStatus() {
        String statusName = ACTIVE.name();
        ResponseDto responseDto = response.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT,DIRECTION,false,ACTIVE.name(),ControllerType.CERTIFICATE_ALL);

        when(responseServiceMock.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findByStatus(PAGE, SIZE, SORT, DIRECTION, statusName))
                .thenReturn(entityList);
        when(serviceMock.findByStatus(PAGE + 1, SIZE, SORT, DIRECTION, statusName))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<GiftCertificateDto> actual = service.findByActiveStatus(PAGE, SIZE, SORT, DIRECTION);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findUsersByStatus() {
        String statusName = StatusName.DELETED.name();

        ResponseDto responseDto = response.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT, DIRECTION,false,statusName,ControllerType.CERTIFICATE_ALL);

        when(responseServiceMock.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findByStatus(PAGE, SIZE, SORT, DIRECTION,statusName))
                .thenReturn(entityList);
        when(serviceMock.findByStatus(PAGE + 1, SIZE, SORT, DIRECTION,statusName))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<GiftCertificateDto> actual = service.findByStatus(PAGE, SIZE, SORT, DIRECTION,statusName);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByParamGift() {
        ResponseDto responseDto = response.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM);
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null,null,false,null,ControllerType.CERTIFICATE_BY_PARAM);


        when(serviceMock.findByParam(dto)).thenReturn(List.of(entity));
        when(responseServiceMock.okResponse(GIFT_CERTIFICATE + FOUND_BY_PARAM)).thenReturn(responseDto);
        when(mapperMock.toDtoList(List.of(entity))).thenReturn(List.of(dto));

        DtoPage<GiftCertificateDto> actual = service.findByParam(dto);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void getImageByID() {
        long id = dto.getId();
        String name = "someName";
        byte[] expected = new byte[]{1, 1, 1, 1};

        when(serviceMock.getImageByID(id,name)).thenReturn(expected);
        byte[] actual = service.getImageByID(id,name);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findActiveByTag() {
        long id = entity.getId();
        String statusName = ACTIVE.name();

        ResponseDto responseDto = response.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT, DIRECTION,false,Long.toString(id),ControllerType.CERTIFICATE_BY_TAG);

        when(responseServiceMock.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findByTag(id,statusName, PAGE, SIZE, SORT))
                .thenReturn(entityList);
        when(serviceMock.findByTag(id,statusName, PAGE + 1, SIZE, SORT))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<GiftCertificateDto> actual = service.findActiveByTag(id, PAGE, SIZE, SORT, DIRECTION);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByTag() {
        long id = entity.getId();
        String statusName = StatusName.DELETED.name();

        ResponseDto responseDto = response.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT, DIRECTION,false,Long.toString(id),ControllerType.CERTIFICATE_BY_TAG);

        when(responseServiceMock.okResponse(pageResponseTemplate(GIFT_CERTIFICATE,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findByTag(id,statusName, PAGE, SIZE, SORT))
                .thenReturn(entityList);
        when(serviceMock.findByTag(id,statusName, PAGE + 1, SIZE, SORT))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<GiftCertificateDto> actual = service.findByTag(id, statusName, PAGE, SIZE, SORT, DIRECTION);
        Assertions.assertEquals(expected,actual);
    }
}