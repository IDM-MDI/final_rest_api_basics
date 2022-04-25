package com.epam.esm.service.impl;


import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.CustomService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import com.epam.esm.validator.ParameterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Map;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class GiftCertificateService implements CustomService<GiftCertificateDto,Long> {

    private final GiftCertificateDaoImpl dao;
    private final List<String> giftCertificateColumns;
    private final GiftCertificateModelMapper mapper;

    @Autowired
    public GiftCertificateService(GiftCertificateDaoImpl dao, Map<String, List<String>> allColumns, GiftCertificateModelMapper mapper) {
        String tableName = "gift_certificate";
        this.dao = dao;
        this.giftCertificateColumns = allColumns.get(tableName);
        this.mapper = mapper;
    }

    @Override
    public void save(GiftCertificateDto dto) throws ServiceException {
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
    public void update(GiftCertificateDto dto, Long id) throws ServiceException {
        try {
            dao.update(mapper.toEntity(dto),id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public List<GiftCertificateDto> findAll() throws ServiceException {
        try {
            return mapper.toDtoList(dao.read());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public GiftCertificateDto findById(Long id) throws ServiceException {
        try {
            return mapper.toDto(dao.findById(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    public List<GiftCertificateDto> findByParam(Map<String, String> param) {
        Map<String,String> validParam = ParameterValidator.getValidMap(param,giftCertificateColumns);
        return mapper.toDtoList(dao.findByParam(validParam));
    }

}