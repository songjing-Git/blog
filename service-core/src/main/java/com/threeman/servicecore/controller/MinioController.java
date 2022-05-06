package com.threeman.servicecore.controller;

import com.threeman.common.exception.CreateException;
import com.threeman.servicecore.config.minio.MinioConfig;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
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
    MinioConfig minioConfig;

    @Autowired
    MinioClient minioClient;

    @PutMapping("/upload/avatar")
    public Object upload(String username, MultipartFile file) {
        log.info("username:{}", username);
        log.info("file:{}", file.isEmpty());
        try {
            PutObjectArgs putAvatar = PutObjectArgs.builder()
                    .object(username + "-" + file.getOriginalFilename())
                    .bucket("avatar")
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putAvatar);
            log.info("etag:{}", objectWriteResponse.etag());
            log.info("versionId:{}", objectWriteResponse.versionId());
            return minioConfig.getEndPoint() + "/avatar/" + username + "-" + file.getOriginalFilename();
        } catch (Exception ignored) {
            return new CreateException(0, ignored.getMessage());
        }
    }

    @PutMapping(value = "/upload/blog")
    public Object upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CreateException("上传文件不能为空");
        }
        try {
            PutObjectArgs putAvatar = PutObjectArgs.builder()
                    .object(file.getOriginalFilename())
                    .bucket("blog-images")
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putAvatar);
            log.info("etag:{}", objectWriteResponse.etag());
            log.info("versionId:{}", objectWriteResponse.versionId());
            return minioConfig.getEndPoint() + "/blog-images/" + file.getOriginalFilename();
        } catch (Exception ignored) {
            return new CreateException(0, ignored.getMessage());
        }
    }

}
