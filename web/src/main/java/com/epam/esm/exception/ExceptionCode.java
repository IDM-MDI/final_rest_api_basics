package com.epam.esm.exception;

public enum ExceptionCode {
    NOT_FOUND_EXCEPTION(42401,"NOT FOUND"),
    BAD_REQUEST(42404,"BAD REQUEST"),
    BAD_PATH_ID(42424,"BAD ID PATH"),
    BAD_ARGUMENTS(42422,"BAD ARGUMENTS"),
    BAD_MEDIA_TYPE(42415,"BAD MEDIA TYPE"),
    METHOD_NOT_ALLOWED(42501,"METHOD NOT ALLOWED");

    private final int code;
    private final String name;

    ExceptionCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
