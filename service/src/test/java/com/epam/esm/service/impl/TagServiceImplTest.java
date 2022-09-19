package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.OrderRepository;
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
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.validator.SortValidator.getValidSort;
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
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    @Mock
    private TagModelMapper modelMapper = Mockito.mock(TagModelMapper.class);


    @InjectMocks
    private TagServiceImpl service;

    TagServiceImplTest() {
        this.response= new ResponseService();
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

    @SneakyThrows
    @Test
    void save() {
        when(repository.existsByName(dto.getName())).thenReturn(false);
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        Tag actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void saveShouldThrowRepositoryException() {
        when(repository.existsByName(dto.getName())).thenReturn(true);
        Assertions.assertThrows(RepositoryException.class,() -> service.save(dto));
    }

    @Test
    void findAll() {
        int page = 1;
        int size = 1;
        String sort = "id";
        String direction = "asc";

        when(repository.findAll(PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(new PageImpl<>(entityList));

        List<Tag> actual = service.findAll(page, size, sort,direction);
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
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.findAll(Example.of(entity,matcher))).thenReturn(entityList);

        List<Tag> actual = service.findByParam(dto);
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void findByParamShouldThrowRepositoryException() {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.findAll(Example.of(entity,matcher))).thenReturn(List.of());
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
        Assertions.assertThrows(RepositoryException.class, () -> service.delete(id));
    }

    @Test
    void findByStatus() {
        int page = 0;
        int size = 1;
        String sort = "id";
        String direction = "asc";
        String statusName = ACTIVE.name();

        when(repository.findTagsByStatus(statusName,PageRequest.of(page, size, getValidSort(sort,direction))))
                .thenReturn(entityList);

        List<Tag> actual = service.findByStatus(page, size, sort,direction,statusName);
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void update() {
        TagDto expectedDto = new TagDto(1L,"update",null,false,ACTIVE.name());
        Tag expected = new Tag(1L,"update",null,ACTIVE.name());

        when(modelMapper.toEntity(expectedDto)).thenReturn(expected);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(entity));
        when(repository.existsByName(expected.getName())).thenReturn(false);
        when(repository.save(expected)).thenReturn(expected);
        Tag actual = service.update(expectedDto);
        Assertions.assertEquals(expected,actual);
    }
    @Test
    void updateShouldThrowRepositoryException() {
        TagDto expectedDto = new TagDto(1L,"update",null,false,ACTIVE.name());
        Tag expected = new Tag(1L,"update",null,ACTIVE.name());

        when(modelMapper.toEntity(expectedDto)).thenReturn(expected);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(entity));
        when(repository.existsByName(expected.getName())).thenReturn(true);
        Assertions.assertThrows(RepositoryException.class, () -> service.update(expectedDto));
    }
    @Test
    void getCount() {
        long expected = 1;
        when(repository.count()).thenReturn(expected);
        long actual = service.getCount();
        Assertions.assertEquals(expected,actual);
    }
}