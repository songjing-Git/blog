package com.threeman.common.result;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/26 20:09
 */
public enum  ResultEnum {

    /**
     * 成功默认枚举
     */
    DEFAULT_SUCCESS(200,"SUCCESS"),
    /**
     * 失败默认枚举
     */
    DEFAULT_FAILED(0,"ERROR"),

    /**
     * token失效
     */
    TOKEN_EXPIRE(101,"token失效");
    private final Integer code;

    private final String message;

    ResultEnum(Integer code, String message) {
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
