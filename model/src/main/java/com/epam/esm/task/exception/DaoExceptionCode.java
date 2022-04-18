package com.epam.esm.task.exception;

public enum DaoExceptionCode {
    DAO_NOTHING_FIND_EXCEPTION(42111,"NOTHING FIND"),
    DAO_NOTHING_FIND_BY_ID(42112,"NOTHING FIND BY ID"),
    DAO_NOTHING_FIND_BY_PARAM(42113,"NOTHING FIND BY PARAMETER"),
    DAO_SAVE_ERROR(42114,"ERROR WHILE SAVE"),
    DAO_UPDATE_ERROR(42115,"ERROR WHILE UPDATE");

    private final int code;
    private final String name;

    DaoExceptionCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "DaoExceptionCode{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
