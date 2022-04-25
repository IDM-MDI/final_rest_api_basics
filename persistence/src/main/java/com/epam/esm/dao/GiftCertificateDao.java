package com.epam.esm.dao;


import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDao extends CrudDao<GiftCertificate,Long> {
    List<GiftCertificate> findByParam(Map<String,String> param);
}
