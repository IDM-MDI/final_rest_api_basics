package com.epam.esm.dao.mapper;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.dao.ColumnName.*;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    private final GiftCertificateBuilder builder;

    @Autowired
    public GiftCertificateMapper(GiftCertificateBuilder builder) {
        this.builder = builder;
    }

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return builder.setId(rs.getLong(ID)).setName(rs.getString(NAME)).
                setDescription(rs.getString(DESCRIPTION)).
                setPrice(rs.getBigDecimal(PRICE)).
                setDuration(rs.getInt(DURATION)).
                setCreate_date(rs.getTimestamp(CREATE_DATE).toLocalDateTime()).
                setUpdate_date(rs.getTimestamp(LAST_UPDATE_DATE).toLocalDateTime()).
                setDeleted(rs.getBoolean(DELETED)).
                build();
    }
}
