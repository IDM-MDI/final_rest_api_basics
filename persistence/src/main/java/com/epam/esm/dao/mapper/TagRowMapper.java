package com.epam.esm.dao.mapper;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.dao.ColumnName.*;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    private final TagBuilder builder;

    @Autowired
    public TagRowMapper(TagBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return builder.setId(rs.getLong(ID)).
                setName(rs.getString(NAME)).
                build();
    }
}
