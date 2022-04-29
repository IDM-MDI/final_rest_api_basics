package com.epam.esm.service.impl;


import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CustomService;
import com.epam.esm.util.impl.TagModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class TagService implements CustomService<TagDto,Long> {

    private final TagRepository repository;
    private final TagModelMapper mapper;

    @Autowired
    public TagService(TagRepository repository, TagModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(TagDto dto) {
        repository.save(mapper.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Tag tag = new Tag();
        tag.setId(id);
        repository.delete(tag);
    }

    @Override
    public void update(TagDto dto, Long id) {}

    @Override
    public List<TagDto> findAll() {
        return mapper.toDtoList((List<Tag>) repository.findAll());
    }

    @Override
    public TagDto findById(Long id) throws ServiceException {
        try {
            return mapper.toDto(dao.findById(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }
}
