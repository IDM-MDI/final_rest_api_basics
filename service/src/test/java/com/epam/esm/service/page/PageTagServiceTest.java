package com.epam.esm.service.page;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.ResponseTemplate;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.util.impl.TagModelMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.FOUND_BY_ID;
import static com.epam.esm.dto.ResponseTemplate.FOUND_BY_PARAM;
import static com.epam.esm.dto.ResponseTemplate.pageResponseTemplate;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.service.impl.TagServiceImpl.TAG;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageTagServiceTest {
    private static final int PAGE = 0;
    private static final int SIZE = 0;
    private static final String SORT = "ID";
    private static final String DIRECTION = "asc";
    @Mock
    private ResponseService responseServiceMock = Mockito.mock(ResponseService.class);
    @Mock
    private TagServiceImpl serviceMock = Mockito.mock(TagServiceImpl.class);
    @Mock
    private TagModelMapper mapperMock = Mockito.mock(TagModelMapper.class);

    @InjectMocks
    private PageTagService pageTagService;

    private final ResponseService responseService;
    private final TagModelMapper mapper;
    private final TagDto dto;
    private final Tag entity;
    private final List<TagDto> dtoList;
    private final List<Tag> entityList;

    public PageTagServiceTest() {
        this.responseService = new ResponseService();
        mapper = new TagModelMapper(new TagBuilder());
        this.entity = new Tag(1L,"testTag1",null,ACTIVE.name());
        this.dto = mapper.toDto(entity);
        this.entityList = List.of(
                entity,
                new Tag(2L,"testTag2",null,ACTIVE.name()),
                new Tag(3L,"testTag3",null,ACTIVE.name())
        );
        this.dtoList = mapper.toDtoList(entityList);
    }

    @SneakyThrows
    @Test
    void save() {
        ResponseDto responseDto = responseService.createdResponse(TAG + ResponseTemplate.CREATED);
        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null,null,false,null,null);

        when(responseServiceMock.createdResponse(TAG + ResponseTemplate.CREATED)).thenReturn(responseDto);
        when(serviceMock.save(dto)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        DtoPage<TagDto> actual = pageTagService.save(dto);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void update() {
        ResponseDto responseDto = responseService.createdResponse(TAG + ResponseTemplate.UPDATED);
        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null,null,false,null,null);

        when(responseServiceMock.createdResponse(TAG + ResponseTemplate.UPDATED)).thenReturn(responseDto);
        when(serviceMock.update(dto)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        DtoPage<TagDto> actual = pageTagService.update(dto,dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void delete() {
        long id = dto.getId();
        ResponseDto responseDto = responseService.okResponse(TAG + ResponseTemplate.DELETED);
        DtoPage<TagDto> expected = new DtoPage<>(null,responseDto,0,0,null,null,false,null,null);

        doNothing()
                .when(serviceMock).
                delete(dto.getId());
        when(responseServiceMock.okResponse(TAG + ResponseTemplate.DELETED))
                .thenReturn(responseDto);
        DtoPage<TagDto> actual = pageTagService.delete(id);

        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByPage() {
        ResponseDto responseDto = responseService.okResponse(pageResponseTemplate(TAG,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT,DIRECTION,false,null,ControllerType.TAG_ALL);

        when(responseServiceMock.okResponse(pageResponseTemplate(TAG,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findAll(PAGE,SIZE,SORT,DIRECTION))
                .thenReturn(entityList);
        when(serviceMock.findAll(PAGE + 1,SIZE,SORT,DIRECTION))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<TagDto> actual = pageTagService.findByPage(PAGE, SIZE, SORT, DIRECTION);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByParam() {
        ResponseDto responseDto = responseService.okResponse(TAG + FOUND_BY_PARAM);
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,0,0,null,null,false,null,null);

        when(responseServiceMock.okResponse(TAG + FOUND_BY_PARAM))
                .thenReturn(responseDto);
        when(serviceMock.findByParam(dto)).thenReturn(entityList);
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<TagDto> actual = pageTagService.findByParam(dto);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByActiveStatus() {
        String statusName = ACTIVE.name();
        ResponseDto responseDto = responseService.okResponse(pageResponseTemplate(TAG,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT,DIRECTION,false,null,ControllerType.TAG_ALL);

        when(responseServiceMock.okResponse(pageResponseTemplate(TAG,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findByStatus(PAGE,SIZE,SORT, DIRECTION,statusName))
                .thenReturn(entityList);
        when(serviceMock.findByStatus(PAGE + 1,SIZE,SORT, DIRECTION,statusName))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<TagDto> actual = pageTagService.findByActiveStatus(PAGE, SIZE, SORT, DIRECTION);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findUsersByStatus() {
        String statusName = DELETED.name();

        ResponseDto responseDto = responseService.okResponse(pageResponseTemplate(TAG,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,SORT,DIRECTION,false,null, ControllerType.TAG_ALL);

        when(responseServiceMock.okResponse(pageResponseTemplate(TAG,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findByStatus(PAGE,SIZE,SORT,DIRECTION,statusName))
                .thenReturn(entityList);
        when(serviceMock.findByStatus(PAGE + 1,SIZE,SORT,DIRECTION,statusName))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<TagDto> actual = pageTagService.findByStatus(PAGE, SIZE, SORT, DIRECTION, statusName);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = dto.getId();
        ResponseDto responseDto = responseService.okResponse(TAG + FOUND_BY_ID);
        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null,null,false,null,null);

        when(responseServiceMock.okResponse(TAG + FOUND_BY_ID)).thenReturn(responseDto);
        when(serviceMock.findById(dto.getId())).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        DtoPage<TagDto> actual = pageTagService.findById(id);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void getImageByID() {
        long id = dto.getId();
        byte[] expected = new byte[]{1, 1, 1, 1};

        when(serviceMock.getImageByID(id)).thenReturn(expected);
        byte[] actual = pageTagService.getImageByID(id);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findTop() {
        ResponseDto responseDto = responseService.okResponse(
                TAG + ResponseTemplate.PAGE + "page " + PAGE + ", size " + SIZE
        );
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,SIZE,PAGE,null,null,false,null, ControllerType.TAG_TOP);

        when(responseServiceMock.okResponse(
                TAG + ResponseTemplate.PAGE + "page " + PAGE + ", size " + SIZE
        ))
                .thenReturn(responseDto);
        when(serviceMock.findTop(PAGE,SIZE))
                .thenReturn(entityList);
        when(serviceMock.findTop(PAGE + 1,SIZE))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<TagDto> actual = pageTagService.findTop(PAGE, SIZE);
        Assertions.assertEquals(expected,actual);
    }
}