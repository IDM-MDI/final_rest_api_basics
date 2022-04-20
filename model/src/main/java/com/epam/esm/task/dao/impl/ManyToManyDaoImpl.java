package com.epam.esm.task.dao.impl;

import com.epam.esm.task.dao.AbstractDao;
import com.epam.esm.task.dao.query.EntityQuery;
import com.epam.esm.task.dao.ManyToManyDao;
import com.epam.esm.task.dao.mapper.ManyToManyMapper;
import com.epam.esm.task.dao.query.QueryCreator;
import com.epam.esm.task.entity.impl.ManyToMany;
import com.epam.esm.task.entity.impl.Tag;
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
public class ManyToManyDaoImpl extends AbstractDao<ManyToMany,Long> implements ManyToManyDao {

    private final String tableName = "gift_tag";
    private final String giftId = "gift_id";
    private final String tagId = "tag_id";
    private final List<String> tableColumns;

    @Autowired
    public ManyToManyDaoImpl(JdbcTemplate jdbcTemplate, QueryCreator creator, Map<String,List<String>> tableColumns) {
        super(jdbcTemplate,creator);
        this.tableColumns = tableColumns.get(tableName);
    }

    @Override
    public ManyToMany findById(Long id) {
        return jdbcTemplate.queryForObject(creator.findById(tableName), new ManyToManyMapper(),id);
    }

    @Override
    public List<ManyToMany> findByColumn(String columnName, String data) {
        return jdbcTemplate.query(creator.findByColumn(tableName,columnName), new ManyToManyMapper(),data);
    }

    @Override
    public List<ManyToMany> findByGiftId(long id) {
        return jdbcTemplate.query(creator.findByColumn(tableName,giftId),
                new ManyToManyMapper(),id);
    }

    @Override
    public List<ManyToMany> findByTagId(long id) {
        return jdbcTemplate.query(creator.findByColumn(tableName,tagId),
                new ManyToManyMapper(),id);
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
    protected void fillPreparedStatement(ManyToMany entity, PreparedStatement statement) throws SQLException {
        statement.setLong(1,entity.getGiftId());
        statement.setLong(2,entity.getTagId());
    }

    @Override
    protected long executeEntity(ManyToMany entity, String query) {
        return 0;
    }
}
