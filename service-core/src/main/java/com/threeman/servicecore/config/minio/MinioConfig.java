package com.threeman.servicecore.config.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/2/28 11:09
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {


    private String endPoint;

    private String accessKey;

    private String secretKey;

    /**
     * 注入minio 客户端
     *
     * @return
     */
    @Bean
    public MinioClient minioClient() {

        return MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
