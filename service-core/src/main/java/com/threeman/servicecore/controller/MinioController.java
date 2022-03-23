package com.threeman.servicecore.controller;

import com.threeman.common.exception.CreateException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/2/28 11:20
 */
@RestController
@Slf4j
public class MinioController {

    @Autowired
    MinioClient minioClient;

    @PutMapping("/upload/avater")
    public Object upload(String username,MultipartFile file){
        log.info("username:{}",username);
        log.info("file:{}",file.isEmpty());
        try {
            PutObjectArgs putAvater = PutObjectArgs.builder()
                    .object(username+"-"+file.getOriginalFilename())
                    .bucket("avater")
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putAvater);
            log.info("etag:{}",objectWriteResponse.etag());
            log.info("versionId:{}",objectWriteResponse.versionId());
            GetPresignedObjectUrlArgs getAvater = GetPresignedObjectUrlArgs.builder()
                    .bucket("avater").object(username+"-"+file.getOriginalFilename())
                    .method(Method.GET)
                    //.expiry(60, TimeUnit.SECONDS)
                    .build();
            return minioClient.getPresignedObjectUrl(getAvater);
        }catch (Exception ignored){
            return new CreateException(0,ignored.getMessage());
        }
    }

    @PutMapping(value = "/upload/blog")
    public Object upload(MultipartFile file){
        log.info("file:{}",file.isEmpty());
        try {
            PutObjectArgs putAvater = PutObjectArgs.builder()
                    .object(file.getOriginalFilename())
                    .bucket("blog-images")
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putAvater);
            log.info("etag:{}",objectWriteResponse.etag());
            log.info("versionId:{}",objectWriteResponse.versionId());
            GetPresignedObjectUrlArgs getAvater = GetPresignedObjectUrlArgs.builder()
                    .bucket("blog-images").object(file.getOriginalFilename())
                    .method(Method.GET)
                    //.expiry(60, TimeUnit.SECONDS)
                    .build();
            return minioClient.getPresignedObjectUrl(getAvater);
        }catch (Exception ignored){
            return new CreateException(0,ignored.getMessage());
        }
    }

    /*@PutMapping("/upload/blog")
    public Object upload(int blogId, MultipartFile[] files){
        ArrayList<String> result=new ArrayList<>();
        for (MultipartFile file: files){
            try {
                PutObjectArgs putAvater = PutObjectArgs.builder()
                        .object(blogId + "-" + file.getOriginalFilename())
                        .bucket("avater")
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build();
                ObjectWriteResponse objectWriteResponse = minioClient.putObject(putAvater);
                log.info("etag:{}", objectWriteResponse.etag());
                log.info("versionId:{}", objectWriteResponse.versionId());
                GetPresignedObjectUrlArgs getAvater = GetPresignedObjectUrlArgs.builder()
                        .bucket("avater").object(blogId + "-" + file.getOriginalFilename())
                        .method(Method.GET)
                        //.expiry(60, TimeUnit.SECONDS)
                        .build();
                String url = minioClient.getPresignedObjectUrl(getAvater);
                if (!StringUtils.isEmpty(url)){
                    result.add(url);
                }
            }catch (Exception ignored){
                return new CreateException(0,ignored.getMessage());
            }
        }
        return result;
    }*/

}
