package com.threeman.servicecore.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/3/9 17:17
 */
@RestController
@Slf4j
public class EsController {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @SneakyThrows
    @PostMapping("/insertEsModel")
    public void insertEsModel(){
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("blog");
        XContentBuilder mapping= XContentFactory.jsonBuilder();
        mapping.startObject().startObject("properties")

                .startObject("blog_info_id").field("type","long").endObject()

                .startObject("blog_author_name").field("type","keyword").endObject()

                .startObject("blog_title").field("type","text").endObject()
                //摘要
                .startObject("summary").field("type","text").endObject()
                //封面
                .startObject("image_url").field("type","text").endObject()
                //标签
                .startObject("tags").field("type","text").endObject()

                .startObject("create_time").field("type","date").field("format","yyyy-mm-dd HH:mm:ss").endObject()
                //内容
                .startObject("context").field("type","text").endObject()
                //阅读量
                .startObject("views").field("type","long").endObject()
                //点赞数
                .startObject("supports").field("type","long").endObject()
                //评论数
                .startObject("comments").field("type","long").endObject()
                //发布形式 1-公开 0-私密
                .startObject("open").field("type","integer").endObject()
                //发布时间
                .startObject("release_time").field("type","date").field("format","yyyy-mm-dd HH:mm:ss").endObject()
                //博文类型 1-html 0-markdown
                .startObject("blog_type").field("type","integer").endObject()
                //1立刻发布 0定时发布
                .startObject("immediate").field("type","integer").endObject()
        .endObject().endObject();
        createIndexRequest.mapping(mapping);
        CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

    }
}
