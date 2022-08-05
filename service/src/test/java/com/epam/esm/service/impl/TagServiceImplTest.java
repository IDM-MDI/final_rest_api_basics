package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
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

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private final TagDto dto;
    private final Tag entity;
    private final List<TagDto> dtoList;
    private final List<Tag> entityList;
    private final TagModelMapper mapper;
    private final ResponseService response;
    @Mock
    private TagRepository repository = Mockito.mock(TagRepository.class);
    @Mock
    private TagModelMapper modelMapper = Mockito.mock(TagModelMapper.class);


    @InjectMocks
    private TagServiceImpl service;

    TagServiceImplTest() {
        this.response= new ResponseService();
        mapper = new TagModelMapper(new TagBuilder());
        this.entity = new Tag(1L,"testTag1",ACTIVE.name());
        this.dto = mapper.toDto(entity);
        this.entityList = List.of(
                entity,
                new Tag(2L,"testTag2",ACTIVE.name()),
                new Tag(3L,"testTag3",ACTIVE.name())
        );
        this.dtoList = mapper.toDtoList(entityList);
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
    void findByParamShouldThrowRepositoryException() {
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.findAll(Example.of(entity))).thenReturn(List.of());
        Assertions.assertThrows(RepositoryException.class,()->service.findByParam(dto));
    }

    @SneakyThrows
    @Test
    void delete() {
        long id = dto.getId();

        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).setDelete(id,DELETED.name());

        service.delete(id);
    }

    @SneakyThrows
    @Test
    void deleteShouldThrowRepositoryException() {
        long id = dto.getId();

        when(repository.existsById(id)).thenReturn(false);
        Assertions.assertThrows(RepositoryException.class,()->service.delete(id));
    }

    @Test
    void findByStatus() {
        int page = 1;
        int size = 1;
        String sort = "id";
        String statusName = ACTIVE.name();

        when(repository.findByStatus(statusName,PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(entityList);

        List<Tag> actual = service.findByStatus(page, size, sort,statusName);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void update() {
        Assertions.assertNull(service.update(dto));
    }
}