package com.epam.esm.service.impl;


import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
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

    private final TagDaoImpl dao;
    private final TagModelMapper mapper;

    @Autowired
    public TagService(TagDaoImpl dao, TagModelMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public void save(TagDto dto) throws ServiceException {
        try {
            dao.create(mapper.toEntity(dto));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            dao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public void update(TagDto dto, Long id) {}

    @Override
    public List<TagDto> findAll() throws ServiceException {
        try {
            return mapper.toDtoList(dao.read());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
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
