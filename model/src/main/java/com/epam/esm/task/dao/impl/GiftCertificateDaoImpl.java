package com.epam.esm.task.dao.impl;

import com.epam.esm.task.builder.impl.GiftCertificateBuilder;
import com.epam.esm.task.dao.AbstractDao;
import com.epam.esm.task.dao.GiftCertificateDao;
import com.epam.esm.task.dao.mapper.GiftCertificateMapper;
import com.epam.esm.task.dao.query.QueryCreator;
import com.epam.esm.task.entity.impl.GiftCertificate;
import com.epam.esm.task.entity.impl.Tag;
import com.epam.esm.task.exception.DaoException;
import static com.epam.esm.task.exception.DaoExceptionCode.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Profile("test")
public class GiftCertificateDaoImpl extends AbstractDao<GiftCertificate,Long> implements GiftCertificateDao {

    private final String tableName = "gift_certificate";
    private final List<String> tableColumns;
    private final GiftCertificateBuilder builder;
    private final TagDaoImpl tagDao;
    private final ManyToManyDaoImpl mtmDao;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate,
                                  Map<String,List<String>> allColumns, QueryCreator creator, GiftCertificateBuilder builder,
                                  TagDaoImpl tagDao,
                                  ManyToManyDaoImpl mtmDao) {
        super(jdbcTemplate,creator);
        this.builder = builder;
        this.tagDao = tagDao;
        this.mtmDao = mtmDao;
        this.tableColumns = allColumns.get(tableName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long create(GiftCertificate entity) throws DaoException {
        try{
            String query = creator.insert(tableName,tableColumns);
            long giftId = executeEntity(entity,query);
            mtmDao.create(giftId,tagDao.createWithList(entity.getTags()));
            return giftId;
        }catch (DataAccessException e) {
            throw new DaoException(DAO_SAVE_ERROR.toString(),e);
        }catch (NullPointerException e) {
            throw new DaoException(DAO_NULL_POINTER.toString(),e);
        }
    }

    @Transactional
    @Override
    public List<GiftCertificate> read() throws DaoException {
        try {
            String query = creator.findAll(tableName);
            List<GiftCertificate> gifts = jdbcTemplate.query(query,
                    new GiftCertificateMapper(builder));
            findByListGift(gifts);
            return gifts;
        } catch(DataAccessException e) {
            throw new DaoException(DAO_NOTHING_FIND_EXCEPTION.toString(), e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(GiftCertificate entity, Long id) throws DaoException {
        try {
            String query = creator.update(tableName,tableColumns);
            jdbcTemplate.update(query,
                    entity.getName(),entity.getDescription(),
                    entity.getPrice(),entity.getDuration(),
                    entity.getCreate_date(),entity.getUpdate_date(),id);
            mtmDao.deleteByGiftId(id);
            mtmDao.create(id,tagDao.createWithList(entity.getTags()));
        } catch(DataAccessException e) {
            throw new DaoException(DAO_UPDATE_ERROR.toString(),e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) throws DaoException {
        try {
            String query = creator.delete(tableName);
            jdbcTemplate.update(query,id);
            mtmDao.deleteByGiftId(id);
        } catch (DataAccessException e) {
            throw new DaoException(DAO_NOTHING_FIND_BY_ID.toString(),e);
        }
    }

    @Override
    public GiftCertificate findById(Long id) throws DaoException {
        try {
            String query = creator.findById(tableName);
            GiftCertificate result = jdbcTemplate.queryForObject(query,
                    new GiftCertificateMapper(builder),id);
            List<Tag> tagList = new ArrayList<>();
            mtmDao.findByGiftId(id).forEach(i -> {

                try {
                    tagList.add(tagDao.findById(i.getTagId()));
                } catch (DaoException ignored) {}

            });
            if (result != null) {
                result.setTags(tagList);
            }
            return result;
        } catch (DataAccessException e) {
            throw new DaoException(DAO_NOTHING_FIND_BY_ID.toString(),e);
        }
    }

    @Override
    public List<GiftCertificate> findByColumn(String columnName, String data) {
        String query = creator.findByColumn(tableName,columnName);
        return jdbcTemplate.query(query,new GiftCertificateMapper(builder),data);
    }

    public List<GiftCertificate> findByParam(Map<String, String> validParam) {
        List<GiftCertificate> result = jdbcTemplate.query(
                creator.findByParam(tableName,validParam),
                new GiftCertificateMapper(builder));
        findByListGift(result);
        return result;
    }

    @Override
    protected long executeEntity(GiftCertificate entity, String query) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.
                    prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(entity,statement);
            return statement;
        },keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    protected void fillPreparedStatement(GiftCertificate entity,PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(2,entity.getDescription());
        statement.setBigDecimal(3,entity.getPrice());
        statement.setInt(4, entity.getDuration());
        statement.setDate(5,Date.valueOf(entity.getCreate_date().toLocalDate()));
        statement.setDate(6,Date.valueOf(entity.getUpdate_date().toLocalDate()));
    }

    private void findByListGift(List<GiftCertificate> gifts){
        gifts.forEach(i -> {
            List<Tag> tagList = new ArrayList<>();
            mtmDao.findByGiftId(i.getId()).forEach( j -> {

                try {
                    tagList.add(tagDao.findById(j.getTagId()));
                } catch (DaoException ignored) {}

            });
            i.setTags(tagList);
        });
    }
}
