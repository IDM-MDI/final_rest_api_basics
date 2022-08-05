package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NULL_POINTER;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_SAVE_ERROR;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository repository = Mockito.mock(GiftCertificateRepository.class);
    @Mock
    private TagServiceImpl tagServiceImpl = Mockito.mock(TagServiceImpl.class);;
    @Mock
    private GiftCertificateModelMapper modelMapper = Mockito.mock(GiftCertificateModelMapper.class);
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
                null,
                "testStatus"
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
                null,
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
                        null, null, null,
                        "testStatus"),
                new GiftCertificateDto(
                        3L,
                        "name 3",
                        "description 3",
                        new BigDecimal("3"),
                        3,
                        null, null, null,
                        "testStatus"));
        entityList = mapper.toEntityList(dtoList);
    }
    @SneakyThrows
    @Test
    void save() {
        when(repository.findById(dto.getId()))
                .thenReturn(Optional.empty());
        when(modelMapper.toEntity(dto))
                .thenReturn(entity);
        when(tagServiceImpl.saveAllByName(dto.getTags()))
                .thenReturn(null);
        when(repository.save(entity))
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
        when(repository.findById(dto.getId()))
                .thenReturn(Optional.of(entity));
        Assertions.assertThrows(RepositoryException.class,() -> service.save(dto), REPOSITORY_SAVE_ERROR.toString());
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
    void updateShouldThrowRepositoryException() {
        when(repository.findById(dto.getId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(RepositoryException.class,() -> service.update(dto), REPOSITORY_NOTHING_FIND_BY_ID.toString());
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

    @SneakyThrows
    @Test
    void findByStatus() {
        when(repository.findByStatus(ACTIVE.name(),PageRequest.of(0, 1,Sort.by("id")))).thenReturn(entityList);
        List<GiftCertificate> actual = service.findByStatus(0,1,"id",ACTIVE.name());
        Assertions.assertEquals(entityList,actual);
    }
}