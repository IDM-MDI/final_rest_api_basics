package com.epam.esm.service.impl;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.CustomService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
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

    private final GiftCertificateRepository repository;
    private final GiftCertificateModelMapper mapper;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository repository, Map<String, List<String>> allColumns, GiftCertificateModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(GiftCertificateDto dto) {
        repository.save(mapper.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        GiftCertificate entity = new GiftCertificate();
        entity.setId(id);
        repository.delete(entity);
    }

    @Override
    public void update(GiftCertificateDto dto, Long id) {
        GiftCertificate entity = mapper.toEntity(dto);
        entity.setId(id);
        repository.save(entity);
    }

    @Override
    public List<GiftCertificateDto> findAll() {
        return mapper.toDtoList((List<GiftCertificate>) repository.findAll());
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        return mapper.toDto(repository.findById(id).get());
    }

    public List<GiftCertificateDto> findByParam(Map<String, String> param) {
        //Map<String,String> validParam = ParameterValidator.getValidMap(param,giftCertificateColumns);
        //return mapper.toDtoList(dao.findByParam(validParam));
        return null;
    }

}