package com.epam.esm.dao.impl;

import com.epam.esm.builder.impl.GiftTagBuilder;
import com.epam.esm.dao.AbstractDao;
import com.epam.esm.dao.GiftTagDao;
import com.epam.esm.dao.query.EntityQuery;
import com.epam.esm.dao.mapper.GiftTagRowMapper;
import com.epam.esm.dao.query.QueryCreator;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Repository
@Profile("prod")
public class GiftTagDaoImpl extends AbstractDao<GiftTag,Long> implements GiftTagDao {

    private final String tableName = "gift_tag";
    private final String giftId = "gift_id";
    private final String tagId = "tag_id";
    private final List<String> tableColumns;
    private final GiftTagBuilder builder;

    @Autowired
    public GiftTagDaoImpl(JdbcTemplate jdbcTemplate, QueryCreator creator,
                          Map<String, List<String>> tableColumns, GiftTagBuilder builder) {
        super(jdbcTemplate,creator);
        this.tableColumns = tableColumns.get(tableName);
        this.builder = builder;
    }

    @Override
    public GiftTag findById(Long id) {
        return jdbcTemplate.queryForObject(creator.findById(tableName), new GiftTagRowMapper(builder),id);
    }

    @Override
    public List<GiftTag> findByColumn(String columnName, String data) {
        return jdbcTemplate.query(creator.findByColumn(tableName,columnName), new GiftTagRowMapper(builder),data);
    }

    @Override
    public List<GiftTag> findByGiftId(long id) {
        return jdbcTemplate.query(creator.findByColumn(tableName,giftId),
                new GiftTagRowMapper(builder),id);
    }

    @Override
    public List<GiftTag> findByTagId(long id) {
        return jdbcTemplate.query(creator.findByColumn(tableName,tagId),
                new GiftTagRowMapper(builder),id);
    }

    @Override
    public void create(long giftId, List<Long> tagIdList) {
        String query = creator.insert(tableName,tableColumns);
        tagIdList.forEach((i)->{
            jdbcTemplate.update(query,giftId,i);
        });
    }

    @Override
    public void update(long giftId, List<Tag> tagList) {
        String query = creator.update(tableName,tableColumns);
        tagList.forEach((i)->{
            jdbcTemplate.update(query,giftId,i.getId());
        });
    }

    @Override
    public void deleteByGiftId(long id) {
        jdbcTemplate.update(EntityQuery.DELETE_BY_GIFT_ID_MTM,id);
    }

    @Override
    public void deleteByTagId(long id) {
        jdbcTemplate.update(EntityQuery.DELETE_BY_TAG_ID_MTM,id);
    }

    @Override
    protected void fillPreparedStatement(GiftTag entity, PreparedStatement statement) throws SQLException {
        statement.setLong(1,entity.getGiftId());
        statement.setLong(2,entity.getTagId());
    }

    @Override
    protected long executeEntity(GiftTag entity, String query) {
        return 0;
    }
}
