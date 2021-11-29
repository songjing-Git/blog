package com.threeman.common.exception;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 15:10
 */
public  class CreateException extends RuntimeException{

    private  Integer code;
    private  String message;

    public CreateException(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public CreateException(String message){
        this.message=message;
    }

    public CreateException(Integer code){
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }

}
