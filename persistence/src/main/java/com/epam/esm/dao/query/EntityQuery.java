package com.epam.esm.task.dao.query;

public class EntityQuery {
    public static final String FIND_BY_NAME_TAG = "SELECT * FROM tag WHERE name = ?";

    public static final String SELECT_BY_GIFT_ID_MTM = "SELECT * FROM gift_tag WHERE gift_id = ? AND deleted = 0;";
    public static final String SELECT_BY_TAG_ID_MTM = "SELECT * FROM gift_tag WHERE tag_id = ?";
    public static final String DELETE_BY_GIFT_ID_MTM = "UPDATE gift_tag SET deleted = 1 WHERE gift_id = ?";
    public static final String DELETE_BY_TAG_ID_MTM = "UPDATE gift_tag SET deleted = 1 WHERE tag_id = ?";
}
