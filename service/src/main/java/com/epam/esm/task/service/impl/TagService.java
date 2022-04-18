package com.epam.esm.task.service.impl;

import com.epam.esm.task.dao.impl.TagDaoImpl;
import com.epam.esm.task.dto.impl.TagDto;
import com.epam.esm.task.exception.DaoException;
import com.epam.esm.task.exception.ServiceException;
import com.epam.esm.task.service.CustomService;
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

    @Autowired
    public TagService(TagDaoImpl dao) {
        this.dao = dao;
    }

    @Override
    public void save(TagDto dto) throws ServiceException {
        try {
            dao.create(TagDto.toEntity(dto));
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
            return TagDto.toDtoList(dao.read());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public TagDto findById(Long id) throws ServiceException {
        try {
            return TagDto.toDto(dao.findById(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }
}
