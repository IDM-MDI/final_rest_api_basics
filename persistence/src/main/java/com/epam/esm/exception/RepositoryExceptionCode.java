package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum RepositoryExceptionCode {
    REPOSITORY_NOTHING_FIND_EXCEPTION(42111,"NOTHING FIND"),
    REPOSITORY_NOTHING_FIND_BY_ID(42112,"NOTHING FIND BY ID"),
    REPOSITORY_NOTHING_FIND_BY_PARAM(42113,"NOTHING FIND BY PARAMETER"),
    REPOSITORY_SAVE_ERROR(42114,"ERROR WHILE SAVE"),
    REPOSITORY_UPDATE_ERROR(42115,"ERROR WHILE UPDATE"),
    REPOSITORY_NULL_POINTER(41115,"DAO NULL POINTER");

    private final int code;
    private final String name;
}
