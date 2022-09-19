package com.epam.esm.exception;

public class DataGeneratorException extends RuntimeException {
    public DataGeneratorException() {
        super();
    }

    public DataGeneratorException(String message) {
        super(message);
    }

    public DataGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataGeneratorException(Throwable cause) {
        super(cause);
    }

    protected DataGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
