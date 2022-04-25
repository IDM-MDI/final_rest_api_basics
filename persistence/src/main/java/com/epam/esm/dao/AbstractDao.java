package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.epam.esm.dao.query.QueryCreator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public abstract class AbstractDao<T, K>{

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
