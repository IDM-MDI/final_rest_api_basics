package com.epam.esm.task.dao;

import com.epam.esm.task.entity.impl.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDao extends CrudDao<GiftCertificate,Long> {
    List<GiftCertificate> findByParam(Map<String,String> param);
}
