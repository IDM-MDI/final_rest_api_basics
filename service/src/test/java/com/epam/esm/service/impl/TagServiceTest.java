package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.StatusName;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.ResponseService;
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

import java.util.List;
import java.util.Optional;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.service.impl.TagService.TAG;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    private final TagDto dto;
    private final Tag entity;
    private final List<TagDto> dtoList;
    private final List<Tag> entityList;
    private final TagModelMapper mapper;
    private final ResponseService response;
    @Mock
    private TagRepository repository = Mockito.mock(TagRepository.class);
    @Mock
    private StatusService statusService = Mockito.mock(StatusService.class);
    @Mock
    private TagModelMapper modelMapper = Mockito.mock(TagModelMapper.class);
    @Mock
    private ResponseService responseService = Mockito.mock(ResponseService.class);

    @InjectMocks
    private TagService service;

    TagServiceTest() {
        this.response= new ResponseService();
        Status active = new Status(1L, "ACTIVE");
        mapper = new TagModelMapper(new TagBuilder());
        this.entity = new Tag(1L,"testTag1",active);
        this.dto = mapper.toDto(entity);
        this.entityList = List.of(
                entity,
                new Tag(2L,"testTag2",active),
                new Tag(3L,"testTag3",active)
        );
        this.dtoList = mapper.toDtoList(entityList);
    }

    @SneakyThrows
    @Test
    void saveWithDtoPage() {
        ResponseDto responseDto = response.createdResponse(TAG + CREATED);
        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        save();
        when(responseService.createdResponse(TAG + CREATED)).thenReturn(responseDto);
        when(modelMapper.toDto(entity)).thenReturn(dto);

        DtoPage<TagDto> actual = service.saveWithDtoPage(dto);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void deleteWithDtoPage() {
        long id = dto.getId();
        ResponseDto responseDto = response.okResponse(TAG + DELETED);
        DtoPage<TagDto> expected = new DtoPage<>(null,responseDto,0,0,null);

        delete();
        when(responseService.okResponse(TAG + DELETED)).thenReturn(responseDto);
        DtoPage<TagDto> actual = service.deleteWithDtoPage(id);

        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findAllPage() {
        int page = 1;
        int size = 1;
        String sort = "id";
        ResponseDto responseDto = response.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort);
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,size,page,sort);

        findAll();
        when(responseService.okResponse(TAG + PAGE + "page " + page + ", size " + size + ", sort " + sort))
                .thenReturn(responseDto);
        when(modelMapper.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<TagDto> actual = service.findAllPage(page, size, sort);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findAllByParamWithDtoPage() {
        ResponseDto responseDto = response.okResponse(TAG + FOUND_BY_PARAM);
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,responseDto,0,0,null);

        findByParam();
        when(responseService.okResponse(TAG + FOUND_BY_PARAM))
                .thenReturn(responseDto);
        when(modelMapper.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<TagDto> actual = service.findAllByParamWithDtoPage(dto);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByIdWithDtoPage() {
        long id = dto.getId();
        ResponseDto responseDto = response.okResponse(TAG + FOUND_BY_PARAM);
        DtoPage<TagDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);
        findById();

        when(responseService.okResponse(TAG + FOUND_BY_ID)).thenReturn(responseDto);
        when(modelMapper.toDto(entity)).thenReturn(dto);

        DtoPage<TagDto> actual = service.findByIdWithDtoPage(id);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void saveAllByName() {
        entityList.forEach(i-> {
            when(repository.findByName(i.getName())).thenReturn(Optional.empty());
            when(modelMapper.toEntity(mapper.toDto(i))).thenReturn(i);
            when(repository.save(i)).thenReturn(i);
        });


        List<Tag> actual = service.saveAllByName(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void findAllByName() {
        entityList.forEach(i->
                when(repository.findByName(i.getName()))
                        .thenReturn(Optional.of(i))
        );
        List<Tag> actual = service.findAllByName(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void save() {
        when(repository.findByName(dto.getName())).thenReturn(Optional.empty());
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        Tag actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void findAll() {
        int page = 1;
        int size = 1;
        String sort = "id";
        when(repository.findAll(PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(new PageImpl<>(entityList));

        List<Tag> actual = service.findAll(page, size, sort);
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = dto.getId();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        Tag actual = service.findById(id);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void findByParam() {
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.findAll(Example.of(entity))).thenReturn(entityList);

        List<Tag> actual = service.findByParam(dto);
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void delete() {
        long id = dto.getId();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(statusService.findStatus(StatusName.DELETED.name())).thenReturn(null);
        doNothing().when(repository).setDelete(id,null);

        service.delete(id);
        verify(repository).setDelete(id,null);
    }
}