package com.epam.esm.task.dao.impl;

import com.epam.esm.task.builder.impl.TagBuilder;
import com.epam.esm.task.dao.AbstractDao;
import com.epam.esm.task.dao.query.EntityQuery;
import com.epam.esm.task.dao.TagDao;
import com.epam.esm.task.dao.mapper.TagMapper;
import com.epam.esm.task.dao.query.QueryCreator;
import com.epam.esm.task.entity.impl.Tag;
import com.epam.esm.task.exception.DaoException;
import com.epam.esm.task.exception.DaoExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.epam.esm.task.exception.DaoExceptionCode.DAO_NULL_POINTER;

@Repository
@Profile("test")
public class TagDaoImpl extends AbstractDao<Tag,Long> implements TagDao {

    private final String tableName = "tag";
    private final List<String> tableColumns;
    private final TagBuilder builder;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, QueryCreator creator, Map<String,List<String>> tableColumns, TagBuilder builder) {
        super(jdbcTemplate,creator);
        this.tableColumns = tableColumns.get(tableName);
        this.builder = builder;
    }

    @Override
    public long create(Tag entity) throws DaoException {
        try {
            String query = creator.insert(tableName,tableColumns);
            return executeEntity(entity,query);
        } catch (DataAccessException e) {
            throw new DaoException(DaoExceptionCode.DAO_SAVE_ERROR.toString(), e);
        }catch (NullPointerException e) {
            throw new DaoException(DAO_NULL_POINTER.toString(),e);
        }
    }

    @Override
    public List<Tag> read() throws DaoException {
        try {
            String query = creator.findAll(tableName);
            return jdbcTemplate.query(query,new TagMapper(builder));
        } catch (DataAccessException e) {
            throw new DaoException(DaoExceptionCode.DAO_NOTHING_FIND_EXCEPTION.toString(), e);
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        try {
            String query = creator.delete(tableName);
            jdbcTemplate.update(query,id);
        } catch (DataAccessException e) {
            throw new DaoException(DaoExceptionCode.DAO_NOTHING_FIND_BY_ID.toString(), e);
        }
    }

    @Override
    public Tag findById(Long id) throws DaoException {
        try {
            String query = creator.findById(tableName);
            return jdbcTemplate.queryForObject(query,new TagMapper(builder),id);
        } catch (DataAccessException e) {
            throw new DaoException(DaoExceptionCode.DAO_NOTHING_FIND_BY_ID.toString(), e);
        }
    }

    @Override
    public List<Tag> findByColumn(String columnName, String data) {
        return jdbcTemplate.query(creator.findByColumn(tableName,columnName),
                new TagMapper(builder),data);
    }

    @Override
    public List<Long> createWithList(List<Tag> tagList) throws DaoException {
        try {
            List<Long> idList = new ArrayList<>();
            tagList.forEach(i -> {
                try {idList.add(create(i));}
                catch (DaoException ignored) {}
            });
            return idList;
        } catch (DataAccessException e) {
            throw new DaoException(DaoExceptionCode.DAO_SAVE_ERROR.toString(), e);
        }
    }

    @Override
    public Tag findByName(String name) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(EntityQuery.FIND_BY_NAME_TAG,new TagMapper(builder),name);
        } catch (DataAccessException e) {
            throw new DaoException(DaoExceptionCode.DAO_NOTHING_FIND_BY_ID.toString(), e);
        }
    }

    @Override
    protected long executeEntity(Tag entity, String query) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.
                    prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(entity,statement);
            return statement;
        },keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    protected void fillPreparedStatement(Tag entity, PreparedStatement statement) throws SQLException {
        statement.setString(1,entity.getName());
    }

}
