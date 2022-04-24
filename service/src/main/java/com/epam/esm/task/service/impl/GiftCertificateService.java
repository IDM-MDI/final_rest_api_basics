package com.epam.esm.task.service.impl;

import com.epam.esm.task.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.task.dto.GiftCertificateDto;
import com.epam.esm.task.exception.DaoException;
import com.epam.esm.task.exception.ServiceException;
import com.epam.esm.task.service.CustomService;
import com.epam.esm.task.validator.ParameterValidator;
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

    private final String tableName = "gift_certificate";
    private final GiftCertificateDaoImpl dao;
    private final List<String> giftCertificateColumns;

    @Autowired
    public GiftCertificateService(GiftCertificateDaoImpl dao, Map<String,List<String>> allColumns) {
        this.dao = dao;
        this.giftCertificateColumns = allColumns.get(tableName);
    }

    @Override
    public void save(GiftCertificateDto dto) throws ServiceException {
        try {
            dao.create(GiftCertificateDto.toEntity(dto));
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
            dao.update(GiftCertificateDto.toEntity(dto),id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public List<GiftCertificateDto> findAll() throws ServiceException {
        try {
            return GiftCertificateDto.toDtoList(dao.read());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public GiftCertificateDto findById(Long id) throws ServiceException {
        try {
            return GiftCertificateDto.toDto(dao.findById(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    public List<GiftCertificateDto> findByParam(Map<String, String> param) {
        Map<String,String> validParam = ParameterValidator.getValidMap(param,giftCertificateColumns);
        return GiftCertificateDto.toDtoList(dao.findByParam(validParam));
    }

}