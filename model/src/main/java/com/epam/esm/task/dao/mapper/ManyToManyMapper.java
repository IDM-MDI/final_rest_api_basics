package com.epam.esm.task.dao.mapper;

import com.epam.esm.task.dao.ColumnName;
import com.epam.esm.task.entity.impl.GiftCertificate;
import com.epam.esm.task.entity.impl.ManyToMany;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManyToManyMapper implements RowMapper<ManyToMany> {
    @Override
    public ManyToMany mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ManyToMany(rs.getLong(ColumnName.ID),rs.getLong(ColumnName.GIFT_ID),rs.getLong(ColumnName.TAG_ID));
    }
}
