package com.threeman.common.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threeman.common.exception.CreateException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/26 19:30
 */
@Slf4j
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
        return !methodParameter.getDeclaringClass().getName().contains("springfox");
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
        log.info("resultBody:{}",body);
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
        if (body instanceof Map){
            Object status = ((Map<?, ?>) body).get("status");
            if (status==null){
                return new Result<>(ResultEnum.DEFAULT_SUCCESS,body);
            }
            if (200==((int)status)){
                Object message = ((Map<?, ?>) body).get("message");
                return new Result<>(ResultEnum.DEFAULT_SUCCESS,(String)message);
            }
            Object error = ((Map<?, ?>) body).get("error");
            return new Result<>((int)status,(String)error);
        }
        return new Result<>(ResultEnum.DEFAULT_SUCCESS, body);
    }
}
