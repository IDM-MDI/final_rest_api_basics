//package com.epam.esm.service.page;
//
//import com.epam.esm.builder.impl.TagBuilder;
//import com.epam.esm.dto.DtoPage;
//import com.epam.esm.dto.ResponseDto;
//import com.epam.esm.dto.ResponseTemplate;
//import com.epam.esm.dto.TagDto;
//import com.epam.esm.entity.Tag;
//import com.epam.esm.service.ResponseService;
//import com.epam.esm.service.impl.TagServiceImpl;
//import com.epam.esm.util.impl.TagModelMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static com.epam.esm.dto.ResponseTemplate.FOUND_BY_ID;
//import static com.epam.esm.dto.ResponseTemplate.FOUND_BY_PARAM;
//import static com.epam.esm.dto.ResponseTemplate.PAGE;
//import static com.epam.esm.entity.StatusName.ACTIVE;
//import static com.epam.esm.entity.StatusName.DELETED;
//import static com.epam.esm.service.impl.TagServiceImpl.TAG;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class PageTagServiceTest {
//
//    @Mock
//    private ResponseService responseServiceMock = Mockito.mock(ResponseService.class);
//    @Mock
//    private TagServiceImpl serviceMock = Mockito.mock(TagServiceImpl.class);
//    @Mock
//    private TagModelMapper mapperMock = Mockito.mock(TagModelMapper.class);
//
//    @InjectMocks
//    private PageTagService pageTagService;
//
//    private final ResponseService responseService;
//    private final TagModelMapper mapper;
//    private final TagDto dto;
//    private final Tag entity;
//    private final List<TagDto> dtoList;
//    private final List<Tag> entityList;
//
//    public PageTagServiceTest() {
//        this.responseService = new ResponseService();
//        mapper = new TagModelMapper(new TagBuilder());
//        this.entity = new Tag(1L,"testTag1",ACTIVE.name());
//        this.dto = mapper.toDto(entity);
//        this.entityList = List.of(
//                entity,
//                new Tag(2L,"testTag2",ACTIVE.name()),
//                new Tag(3L,"testTag3",ACTIVE.name())
//        );
//        this.dtoList = mapper.toDtoList(entityList);
//    }
//
//    @Test
//    void save() {
//        ResponseDto responseDto = responseService.createdResponse(TAG + ResponseTemplate.CREATED);
//        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);
//
//        when(responseServiceMock.createdResponse(TAG + ResponseTemplate.CREATED)).thenReturn(responseDto);
//        when(serviceMock.save(dto)).thenReturn(entity);
//        when(mapperMock.toDto(entity)).thenReturn(dto);
//
//        DtoPage<TagDto> actual = pageTagService.save(dto);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    void update() {
//        ResponseDto responseDto = responseService.createdResponse(TAG + ResponseTemplate.UPDATED);
//        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);
//
//        when(responseServiceMock.createdResponse(TAG + ResponseTemplate.UPDATED)).thenReturn(responseDto);
//        when(serviceMock.update(dto)).thenReturn(entity);
//        when(mapperMock.toDto(entity)).thenReturn(dto);
//
//        DtoPage<TagDto> actual = pageTagService.update(dto,dto.getId());
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @SneakyThrows
//    @Test
//    void delete() {
//        long id = dto.getId();
//        ResponseDto responseDto = responseService.okResponse(TAG + ResponseTemplate.DELETED);
//        DtoPage<TagDto> expected = new DtoPage<>(null,responseDto,0,0,null);
//
//        doNothing()
//                .when(serviceMock).
//                delete(dto.getId());
//        when(responseServiceMock.okResponse(TAG + ResponseTemplate.DELETED))
//                .thenReturn(responseDto);
//        DtoPage<TagDto> actual = pageTagService.delete(id);
//
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    void findByPage() {
//        int page = 1;
//        int size = 1;
//        String sort = "id";
//        ResponseDto responseDto = responseService.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort);
//        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,size,page,sort);
//
//        when(responseServiceMock.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
//                .thenReturn(responseDto);
//        when(serviceMock.findAll(page,size,sort)).thenReturn(entityList);
//        when(mapperMock.toDtoList(entityList))
//                .thenReturn(dtoList);
//
//        DtoPage<TagDto> actual = pageTagService.findByPage(page, size, sort);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @SneakyThrows
//    @Test
//    void findByParam() {
//        ResponseDto responseDto = responseService.okResponse(TAG + FOUND_BY_PARAM);
//        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,0,0,null);
//
//        when(responseServiceMock.okResponse(TAG + FOUND_BY_PARAM))
//                .thenReturn(responseDto);
//        when(serviceMock.findByParam(dto)).thenReturn(entityList);
//        when(mapperMock.toDtoList(entityList))
//                .thenReturn(dtoList);
//
//        DtoPage<TagDto> actual = pageTagService.findByParam(dto);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    void findByActiveStatus() {
//        int page = 1;
//        int size = 1;
//        String sort = "id";
//        ResponseDto responseDto = responseService.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort);
//        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,size,page,sort);
//
//        when(responseServiceMock.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
//                .thenReturn(responseDto);
//        when(serviceMock.findByStatus(page,size,sort,ACTIVE.name())).thenReturn(entityList);
//        when(mapperMock.toDtoList(entityList))
//                .thenReturn(dtoList);
//
//        DtoPage<TagDto> actual = pageTagService.findByActiveStatus(page, size, sort);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @Test
//    void findByStatus() {
//        int page = 1;
//        int size = 1;
//        String sort = "id";
//        String statusName = DELETED.name();
//        ResponseDto responseDto = responseService.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort);
//        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,size,page,sort);
//
//        when(responseServiceMock.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
//                .thenReturn(responseDto);
//        when(serviceMock.findByStatus(page,size,sort,statusName)).thenReturn(entityList);
//        when(mapperMock.toDtoList(entityList))
//                .thenReturn(dtoList);
//
//        DtoPage<TagDto> actual = pageTagService.findByStatus(page, size, sort,statusName);
//        Assertions.assertEquals(expected,actual);
//    }
//
//    @SneakyThrows
//    @Test
//    void findById() {
//        long id = dto.getId();
//        ResponseDto responseDto = responseService.okResponse(TAG + FOUND_BY_ID);
//        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);
//
//        when(responseServiceMock.okResponse(TAG + FOUND_BY_ID)).thenReturn(responseDto);
//        when(serviceMock.findById(dto.getId())).thenReturn(entity);
//        when(mapperMock.toDto(entity)).thenReturn(dto);
//
//        DtoPage<TagDto> actual = pageTagService.findById(id);
//        Assertions.assertEquals(expected,actual);
//    }
//}