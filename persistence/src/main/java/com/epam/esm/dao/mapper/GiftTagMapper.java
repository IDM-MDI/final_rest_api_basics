package com.epam.esm.task.dao.mapper;

import com.epam.esm.task.dao.ColumnName;
import com.epam.esm.task.entity.impl.GiftTag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftTagMapper implements RowMapper<GiftTag> {
    @Override
    public GiftTag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GiftTag(rs.getLong(ColumnName.ID),rs.getLong(ColumnName.GIFT_ID),rs.getLong(ColumnName.TAG_ID));
    }
}
