package com.threeman.common.result;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/26 19:21
 */
public class Result<T> implements Serializable {

    /**
     * 返回状态码
     */
    private final Integer code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回消息
     */
    private final String message;


    public Integer getCode() {
        return code;
    }


    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }


    public Result(ResultEnum resultEnum) {
        this.code=resultEnum.getCode();
        this.message=resultEnum.getMessage();
    }

    public Result(ResultEnum resultEnum, T data) {
        this.code=resultEnum.getCode();
        this.message=resultEnum.getMessage();
        this.data=data;
    }

    public Result( T data) {
        this.code=ResultEnum.DEFAULT_SUCCESS.getCode();
        this.message=ResultEnum.DEFAULT_SUCCESS.getMessage();
        this.data=data;
    }

    public Result(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result<?> result = (Result<?>) o;
        return Objects.equals(getCode(), result.getCode()) &&
                Objects.equals(getData(), result.getData()) &&
                Objects.equals(getMessage(), result.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getData(), getMessage());
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }



}
