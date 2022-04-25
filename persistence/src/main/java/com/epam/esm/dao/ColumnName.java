package com.epam.esm.dao;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@PropertySource("classpath:columnName.properties")
public class ColumnName {
    @Value("${tag}")
    private String tag;
    @Value("${gift_certificate}")
    private String giftCertificate;
    @Value("${gift_tag}")
    private String giftTag;


    @Bean("columns")
    public Map<String,List<String>> getColumns() {
        Map<String, List<String>> columns = new HashMap<>();
        columns.put("gift_certificate",List.of(giftCertificate.split(",")));
        columns.put("tag",List.of(tag.split(",")));
        columns.put("gift_tag",List.of(giftTag.split(",")));
        return columns;
    }

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String DURATION = "duration";
    public static final String CREATE_DATE = "create_date";
    public static final String LAST_UPDATE_DATE = "last_update_date";
    public static final String GIFT_ID = "gift_id";
    public static final String TAG_ID = "tag_id";
    public static final String DELETED = "deleted";

}
