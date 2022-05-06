package com.threeman.common.exception;

import com.threeman.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 14:39
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public <T> Result<T> exceptionHandler(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        return new Result<>(e.hashCode(), e.getMessage());
    }

    @ExceptionHandler(value = CreateException.class)
    @ResponseBody
    public CreateException createExceptionHandler(CreateException e) {
        e.printStackTrace();
        return new CreateException(e.getCode(), e.getMessage());
    }

}
