package com.threeman.servicecore.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.threeman.common.excel.entityExcel.Student;
import com.threeman.common.utils.FileUtil;
import com.threeman.security.entity.User;
import com.threeman.security.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 11:00
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/login")
    public String test(){
        return "登录成功";
    }


    @GetMapping("/userAdd")
    @Transactional
    public Object userAdd(){
        Object o = redisTemplate.opsForValue().get("1");
        log.info("o:{}",o);
        return o;
    }


    @GetMapping("/writeRedis")
    @Transactional
    public String writeRedis(){
            User userInfoById = userMapper.findUserInfoById(1);
            redisTemplate.opsForValue().set("1", JSONObject.toJSONString(userInfoById));
        return "writeRedis";
    }


    @PostMapping("/excelReader")
    public String excelReader(MultipartFile multipartFile){
        List<Object> list = FileUtil.fileSync(multipartFile);
        BigDecimal result=new BigDecimal("0.0");
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            Map<String, Object> map = JSONObject.parseObject( JSONObject.toJSON(o).toString(), new TypeReference<Map<String, Object>>() {
            });
            Object o1 = map.get("31");

            double v = Double.parseDouble(o1.toString());
            BigDecimal bigDecimal = BigDecimal.valueOf(v);
            BigDecimal bigDecimal32 = new BigDecimal("3.2");
            BigDecimal bigDecimal18 = new BigDecimal("1.8");
            BigDecimal bigDecimal103 = new BigDecimal("10.3");

            if (v>3.2&&v<10.3){
                BigDecimal subtract = bigDecimal.subtract(bigDecimal32);
                BigDecimal multiply = subtract.multiply(bigDecimal18);
                BigDecimal a = bigDecimal32.add(multiply);
                result=result.add(a);
                log.info(v+"--->:{}",a);
            }
            if (v>10.3){
                BigDecimal subtract = bigDecimal.subtract(bigDecimal103);
                BigDecimal multiply = subtract.multiply(bigDecimal18);
                BigDecimal a = bigDecimal103.add(multiply);
                result=result.add(a);
                log.info(v+"--->:{}",a);
            }
        }
        log.info("result:{}",result);
        return "登录成功";
    }

    @PostMapping("/excelWriter")
    public void excelWriter(MultipartFile multipartFile){
        log.info("===:{}",multipartFile==null);
        List<Object> list = FileUtil.fileSyncToObject(multipartFile, Student.class);
        FileUtil.listToFile(list,Student.class,"测试.xlsx");
    }


    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
