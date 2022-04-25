package com.epam.esm.dao.mapper;

import com.epam.esm.builder.impl.GiftTagBuilder;
import com.epam.esm.entity.GiftTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.dao.ColumnName.*;
@Component
public class GiftTagRowMapper implements RowMapper<GiftTag> {

    private final GiftTagBuilder builder;

    @Autowired
    public GiftTagRowMapper(GiftTagBuilder builder) {
        this.builder = builder;
    }

    @Override
    public GiftTag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return builder.setId(rs.getLong(ID)).
                setGiftId(rs.getLong(GIFT_ID)).
                setTagId(rs.getLong(TAG_ID)).
                build();
    }
}
