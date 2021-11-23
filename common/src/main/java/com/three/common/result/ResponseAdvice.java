package com.three.common.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.common.exception.CreateException;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/26 19:30
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    /**
     *  是否支持advice功能
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }


    /**
     *
     * @param body
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @SneakyThrows
    @Override
    public Object  beforeBodyWrite(Object body,
                             MethodParameter methodParameter,
                             MediaType mediaType,
                             Class<? extends HttpMessageConverter<?>> aClass,
                             ServerHttpRequest serverHttpRequest,
                             ServerHttpResponse serverHttpResponse) {
        if (body instanceof Result){
            return body;
        }
        if (body instanceof CreateException){
            return new Result<>(ResultEnum.DEFAULT_FAILED, ((CreateException) body).getMessage());
        }
        if (body instanceof String){
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString( new Result<>(body));
        }
        return new Result<>(ResultEnum.DEFAULT_SUCCESS, body);
    }
}
