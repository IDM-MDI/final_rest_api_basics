package com.epam.esm.task.dao;

import com.epam.esm.task.dao.query.QueryCreator;
import com.epam.esm.task.entity.Entity;
import com.epam.esm.task.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public abstract class AbstractDao<T extends Entity, K>{

    protected final JdbcTemplate jdbcTemplate;
    protected final QueryCreator creator;

    public AbstractDao(JdbcTemplate jdbcTemplate,QueryCreator creator) {
        this.jdbcTemplate = jdbcTemplate;
        this.creator = creator;
    }

    public abstract T findById(K id) throws DaoException;
    public abstract List<T> findByColumn(String columnName, String data);
    protected abstract void fillPreparedStatement(T entity,PreparedStatement statement) throws SQLException;
    protected abstract long executeEntity(T entity,String query) throws DaoException;
}
