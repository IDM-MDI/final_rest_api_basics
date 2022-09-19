package com.epam.esm.dto;

public class ResponseTemplate {

    private ResponseTemplate(){}

    public static final String IS_ALREADY_EXIST = "is already exist";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";
    public static final String DELETED = "deleted";
    public static final String FOUND_BY_ID = "found by id";
    public static final String FOUND_BY_PARAM = "found by param";
    public static final String PAGE = "page: ";
    public static final String LOGGED_IN = "logged in";

    public static String pageResponseTemplate(String entityName, int page, int size, String sort, String direction) {
        return entityName + PAGE +
                "page - " + page +
                ", size - " + size +
                ", sort -" + sort +
                ", direction - " + direction;
    }
}
