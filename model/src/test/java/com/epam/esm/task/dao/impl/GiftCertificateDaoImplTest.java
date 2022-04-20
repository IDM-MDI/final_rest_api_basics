package com.epam.esm.task.dao.impl;

import com.epam.esm.task.builder.impl.GiftCertificateBuilder;
import com.epam.esm.task.config.SpringJdbcTestConfig;
import com.epam.esm.task.entity.impl.GiftCertificate;
import com.epam.esm.task.entity.impl.Tag;
import com.epam.esm.task.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = SpringJdbcTestConfig.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class GiftCertificateDaoImplTest {

    private final GiftCertificateBuilder builder = new GiftCertificateBuilder();

    @Autowired
    private GiftCertificateDaoImpl giftCertificateDao;
    @Autowired
    TagDaoImpl tagDao;
    @Autowired
    ManyToManyDaoImpl manyToManyDao;

    @Test
    @Transactional(rollbackFor = Exception.class)
    void create() throws DaoException {
        GiftCertificate actual = builder.setName("actual").setDescription("actual").
                setPrice(new BigDecimal("100.00")).setDuration(100).
                setCreate_date(LocalDateTime.parse("1111-11-11T00:00")).
                setUpdate_date(LocalDateTime.parse("2222-12-12T00:00")).
                setTagList(new ArrayList<>()).
                getResult();
        actual.setId(giftCertificateDao.create(actual));
        GiftCertificate expected = giftCertificateDao.findById(actual.getId());
        Assertions.assertEquals(actual,expected);
    }

    @Test
    void read() throws DaoException {
        List<GiftCertificate> expected = new ArrayList<>();
        expected.add(builder.setId(1L).
                setName("gift1").setDescription("description1").
                setPrice(new BigDecimal("10.00")).setDuration(1).
                setCreate_date(LocalDateTime.parse("2020-08-29T00:00")).
                setUpdate_date(LocalDateTime.parse("2020-08-29T00:00")).
                setTagList(List.of(new Tag(2,"name"))).getResult());
        expected.add(builder.setId(2L).
                setName("gift2").setDescription("description2").
                setPrice(new BigDecimal("20.00")).setDuration(2).
                setCreate_date(LocalDateTime.parse("2018-08-29T00:00")).
                setUpdate_date(LocalDateTime.parse("2018-08-29T00:00")).
                setTagList(List.of(new Tag(2,"name"))).getResult());
        expected.add(builder.setId(3L).
                setName("gift3").setDescription("description3").
                setPrice(new BigDecimal("30.00")).setDuration(3).
                setCreate_date(LocalDateTime.parse("2019-08-29T00:00")).
                setUpdate_date(LocalDateTime.parse("2019-08-29T00:00")).
                setTagList(new ArrayList<>()).getResult());
        List<GiftCertificate> actual = giftCertificateDao.read();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void update() throws DaoException {
        long id = 1;
        GiftCertificate expect = builder.setId(id).setName("update").setDescription("update").
                setPrice(new BigDecimal("1.00")).setDuration(1).
                setCreate_date(LocalDateTime.parse("2019-08-29T00:00")).
                setUpdate_date(LocalDateTime.parse("2019-08-29T00:00")).
                getResult();
        giftCertificateDao.update(expect,id);
        GiftCertificate actual = giftCertificateDao.findById(id);
        Assertions.assertEquals(actual,expect);
    }

    @Test
    void delete() throws DaoException {
        long id = 1;
        giftCertificateDao.delete(id);
        GiftCertificate actual = giftCertificateDao.findById(id);
        assertTrue(actual.isDeleted());
    }

    @Test
    void findById() throws DaoException {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(2,"name"));
        GiftCertificate actual = builder.setId(1).
                setName("gift1").setDescription("description1").
                setPrice(new BigDecimal("10.00")).setDuration(1).
                setCreate_date(LocalDateTime.parse("2020-08-29T00:00")).
                setUpdate_date(LocalDateTime.parse("2020-08-29T00:00")).
                setTagList(tagList).
                getResult();
        GiftCertificate expected = giftCertificateDao.findById(1L);
        Assertions.assertEquals(actual,expected);
    }
}