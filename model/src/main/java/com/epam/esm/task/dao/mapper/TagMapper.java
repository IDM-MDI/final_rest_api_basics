package com.epam.esm.task.dao.mapper;

import com.epam.esm.task.builder.impl.TagBuilder;
import com.epam.esm.task.dao.ColumnName;
import com.epam.esm.task.entity.impl.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper<Tag> {

    private final TagBuilder builder;

    public TagMapper(TagBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return builder.setId(rs.getLong(ColumnName.ID)).setName(rs.getString(ColumnName.NAME)).getResult();
    }
}
