package com.three.common.exception;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 15:10
 */
public  class CreateException extends RuntimeException{

    private final Integer code;
    private final String message;

    public CreateException(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public Integer getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }

}
