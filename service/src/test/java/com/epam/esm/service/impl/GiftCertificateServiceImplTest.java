package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftTagRepository;
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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.*;
import static com.epam.esm.validator.SortValidator.getValidSort;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository repositoryMock = Mockito.mock(GiftCertificateRepository.class);
    @Mock
    private TagServiceImpl tagServiceImplMock = Mockito.mock(TagServiceImpl.class);;
    @Mock
    private GiftCertificateModelMapper mapperMock = Mockito.mock(GiftCertificateModelMapper.class);
    @Mock
    private GiftTagRepository giftTagRepositoryMock = Mockito.mock(GiftTagRepository.class);
    @InjectMocks
    private GiftCertificateServiceImpl service;

    private final GiftCertificateModelMapper mapper;
    private final ResponseService response;
    private final GiftCertificateDto dto;
    private final GiftCertificateDto dtoUpdated;
    private final GiftCertificate entity;
    private final GiftCertificate entityUpdated;
    private final List<GiftCertificateDto> dtoList;
    private final List<GiftCertificate> entityList;

    public GiftCertificateServiceImplTest() {
        List<TagDto> tagList = List.of(
                new TagDto(1L, "someName1",null,false, null),
                new TagDto(2L, "someName2",null,false, null),
                new TagDto(3L, "someName3",null,false, null)
        );
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
                tagList,
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
                tagList,
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
                        tagList,
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
                        tagList,
                        false,
                        false,
                        false,
                        "testStatus"));
        entityList = mapper.toEntityList(dtoList);
    }
    @SneakyThrows
    @Test
    void save() {
        when(repositoryMock.findById(dto.getId()))
                .thenReturn(Optional.empty());
        when(mapperMock.toEntity(dto))
                .thenReturn(entity);
        when(tagServiceImplMock.saveAllByName(dto.getTags()))
                .thenReturn(null);
        when(repositoryMock.save(entity))
                .thenReturn(entity);

        GiftCertificate actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void saveShouldThrowCustomNullPointer() {
        Assertions.assertThrows(RepositoryException.class,() -> service.save(new GiftCertificateDto()), REPOSITORY_NULL_POINTER.toString());
    }
    @SneakyThrows
    @Test
    void saveShouldThrowRepositoryException() {
        when(repositoryMock.findById(dto.getId()))
                .thenReturn(Optional.of(entity));
        Assertions.assertThrows(RepositoryException.class,() -> service.save(dto), REPOSITORY_SAVE_ERROR.toString());
    }

    @SneakyThrows
    @Test
    void update() {
        when(repositoryMock.findById(dtoUpdated.getId()))
                .thenReturn(Optional.of(entity));
        when(mapperMock.toEntity(dtoUpdated))
                .thenReturn(entityUpdated);
        when(repositoryMock.save(entityUpdated))
                .thenReturn(entityUpdated);
        doNothing().when(giftTagRepositoryMock).setDeleteByGift(anyLong(),eq(DELETED.name()));
        when(tagServiceImplMock.saveAllByName(dto.getTags())).thenReturn(entity.getTagList());


        GiftCertificate actual = service.update(dtoUpdated);
        Assertions.assertEquals(entityUpdated,actual);
    }

    @SneakyThrows
    @Test
    void updateShouldThrowRepositoryException() {
        when(repositoryMock.findById(dto.getId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(RepositoryException.class,() -> service.update(dto), REPOSITORY_NOTHING_FIND_BY_ID.toString());
    }

    @SneakyThrows
    @Test
    void findAll() {
        int page = 1;
        int size = 1;
        String sort = "id";
        String direction = "asc";

        when(repositoryMock.findAll(PageRequest.of(page, size, getValidSort(sort,direction))))
                .thenReturn(new PageImpl<>(entityList));

        List<GiftCertificate> actual = service.findAll(page, size, sort, direction);
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void findAllShouldThrowRepositoryException() {
        int page = 1;
        int size = 1;
        String sort = "id";
        String direction = "asc";

        when(repositoryMock.findAll(PageRequest.of(page, size, getValidSort(sort,direction))))
                .thenReturn(new PageImpl<>(List.of()));
        Assertions.assertThrows(RepositoryException.class,()->service.findAll(page,size,sort,direction));
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = dto.getId();

        when(repositoryMock.findById(id)).thenReturn(Optional.of(entity));

        GiftCertificate actual = service.findById(id);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void findUsersByStatus() {
        int page = 0;
        int size = 1;
        String sort = "id";
        String direction = "asc";

        when(repositoryMock.findGiftCertificatesByStatus(ACTIVE.name(),PageRequest.of(page,size,getValidSort(sort,direction))))
                .thenReturn(entityList);
        List<GiftCertificate> actual = service.findByStatus(page,size,sort,direction,ACTIVE.name());
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void delete() {
        long id = dto.getId();
        when(repositoryMock.existsById(id)).thenReturn(true);
        doNothing().when(repositoryMock).setDelete(id,DELETED.name());

        service.delete(id);

        verify(repositoryMock).existsById(id);
        verify(repositoryMock).setDelete(id,DELETED.name());
    }

    @SneakyThrows
    @Test
    void deleteShouldThrowRepositoryException() {
        long id = dto.getId();
        when(repositoryMock.existsById(id)).thenReturn(false);
        Assertions.assertThrows(RepositoryException.class,()->service.delete(id));
    }

    @SneakyThrows
    @Test
    void findByFullParam() {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        when(mapperMock.toEntity(dto)).thenReturn(entity);
        when(repositoryMock.findAll(Example.of(entity,matcher))).thenReturn(entityList);
        when(mapperMock.toDto(entity)).thenReturn(dto);
        when(tagServiceImplMock.findAllByName(dto.getTags())).thenReturn(entity.getTagList());
        when(repositoryMock.findByTagListIn(entity.getTagList())).thenReturn(entityList);

        List<GiftCertificate> actual = service.findByParam(dto);
        Assertions.assertEquals(entityList,actual);
    }

    @SneakyThrows
    @Test
    void findByTagsParam() {
        GiftCertificate emptyGift = new GiftCertificate();
        emptyGift.setTagList(entity.getTagList());

        when(mapperMock.toEntity(dto)).thenReturn(emptyGift);
        when(mapperMock.toDto(emptyGift)).thenReturn(dto);
        when(tagServiceImplMock.findAllByName(dto.getTags())).thenReturn(entity.getTagList());
        when(repositoryMock.findByTagListIn(entity.getTagList())).thenReturn(entityList);

        List<GiftCertificate> actual = service.findByParam(dto);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void findByParamEmptyParamsShouldThrowRepositoryException() {
        GiftCertificate emptyGift = new GiftCertificate();
        when(mapperMock.toEntity(dto)).thenReturn(emptyGift);
        Assertions.assertThrows(RepositoryException.class,()->service.findByParam(dto));
    }

    @Test
    void getCount() {
        long expected = 1;
        when(repositoryMock.count()).thenReturn(expected);
        long actual = service.getCount();
        Assertions.assertEquals(expected,actual);
    }
}