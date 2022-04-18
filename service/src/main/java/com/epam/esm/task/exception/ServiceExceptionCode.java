package com.epam.esm.task.exception;

public enum ServiceExceptionCode {
    SERVICE_BAD_ID(42421,"BAD ID"),
    SERVICE_BAD_NAME(42422,"BAD NAME"),
    SERVICE_BAD_DESCRIPTION(42423,"BAD DESCRIPTION"),
    SERVICE_BAD_PRICE(42424,"BAD PRICE"),
    SERVICE_BAD_DURATION(42425,"BAD DURATION"),
    SERVICE_BAD_DATE(42426, "BAD DATE");

    private final int code;
    private final String name;

    ServiceExceptionCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceExceptionCode{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
