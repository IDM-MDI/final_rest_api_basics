package com.epam.esm.controller;


import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

import static com.epam.esm.exception.ExceptionCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Rest Controller Advice
 * Created By Ishangulyev Dayanch
 * This class need to catch exceptions
 * @return custom one.
 */
@RestControllerAdvice
@Profile("prod")
public class ExceptionController {

    /**
     * @param e -exception
     * Validation by ID Path
     * @return custom one
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>(BAD_PATH_ID.toString(), BAD_REQUEST);
    }

    /**
     * @param exception
     * Validation by DAO exception
     * @return custom one
     */
    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<String> handleDaoExceptions(ServiceException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(RepositoryException.class)
    public final ResponseEntity<String> repositoryException(RepositoryException exception) {
        return new ResponseEntity<>(exception.getMessage(), NOT_FOUND);
    }

    /**
     * Validation by Bad Request
     * @return custom one
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, JsonProcessingException.class})
    public final ResponseEntity<String> handleBadRequestExceptions() {
        return new ResponseEntity<>(ExceptionCode.BAD_REQUEST.toString(), BAD_REQUEST);
    }

    /**
     * Validation by not found url
     * @return custom one
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public final ResponseEntity<String> handleNotFoundException() {
        return new ResponseEntity<>(ExceptionCode.NOT_FOUND_EXCEPTION.toString(), NOT_FOUND);
    }


    /**
     * Validation by method not alowed
     * @return custom one
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<String> methodNotAllowedExceptionException() {
        return new ResponseEntity<>(METHOD_NOT_ALLOWED.toString(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Validation by json type only
     * @return custom one
     */
    @ExceptionHandler(HttpMediaTypeException.class)
    public final ResponseEntity<String> handleBadMediaTypeException() {
        return new ResponseEntity<>(BAD_MEDIA_TYPE.toString(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<String> handleNullPointerExceptions(ServiceException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}