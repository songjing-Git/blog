package com.threeman.common.exception;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 15:40
 */
public enum  ExceptionEnum {

    /**
     * 成功默认枚举
     */
    DEFAULT_SUCCESS(200,"success"),
    /**
     * 失败默认枚举
     */
    DEFAULT_FAILED(0,"ERROR");
    private final Integer code;

    private final String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
