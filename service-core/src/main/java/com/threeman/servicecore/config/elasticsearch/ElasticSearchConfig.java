package com.threeman.servicecore.config.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/3/8 11:46
 */
@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(){
      return   new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("106.13.23.17", 9200,"http")));
    }
}
