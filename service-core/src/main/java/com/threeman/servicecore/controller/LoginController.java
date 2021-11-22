package com.threeman.servicecore.controller;

import com.three.common.excel.entityExcel.Student;
import com.three.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/10/27 11:00
 */
@Slf4j
@RestController
public class LoginController {

    @PostMapping("/login")
    public String test(){
        return "登录成功";
    }

    @PostMapping("/excelReader")
    public String excelReader(MultipartFile multipartFile){
        log.info("===:{}",multipartFile==null);
        FileUtil.fileToObject(multipartFile, Student.class);
        return "登录成功";
    }

    @PostMapping("/excelWriter")
    public void excelWriter(MultipartFile multipartFile){
        log.info("===:{}",multipartFile==null);
        List<Object> list = FileUtil.fileSyncToObject(multipartFile, Student.class);
        FileUtil.listToFile(list,Student.class,"测试.xlsx");
    }

}
