package com.threeman.servicecore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.common.exception.CreateException;
import com.threeman.servicecore.entity.BlogInfo;
import com.threeman.servicecore.mapper.BlogInfoMapper;
import com.threeman.servicecore.service.BlogInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 博文信息表(BlogInfo)表服务实现类
 *
 * @author songjing
 * @since 2022-02-28 15:34:57
 */
@Service
@Slf4j
public class BlogInfoServiceImpl extends ServiceImpl<BlogInfoMapper, BlogInfo> implements BlogInfoService {

    private final String blog_index ="blog";

    @Autowired
    BlogInfoMapper blogInfoMapper;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @SneakyThrows
    @Override
    public boolean insertBlogInfo(BlogInfo blogInfo) {
        log.info("blogInfo:{}",blogInfo);
        if (blogInfo==null){
            throw new CreateException("参数不能为空");
        }
        log.info("blogInfo:{}",blogInfo.getBlogInfoId().toString());
        GetIndexRequest getRequest = new GetIndexRequest(blog_index);
        boolean exists =restHighLevelClient.indices().exists(getRequest,RequestOptions.DEFAULT);
        log.info("exists:{}",exists);
        if (!exists){
            throw new CreateException("es索引不存在");
        }
        IndexRequest indexRequest = new IndexRequest(blog_index);
        indexRequest.id(blogInfo.getBlogInfoId().toString()).source(JSON.toJSONString(blogInfo), XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return index.status().getStatus()!=0;
    }

    @Override
    public List<Map<String,Object>> findBlogInfos(String text) {
        SearchRequest searchRequest = new SearchRequest(blog_index);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("context", text);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search ;
        try {
            search =restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            throw new CreateException(e.getMessage());
        }
        List<Map<String,Object>> result=new ArrayList<>();
        if (search!=null){
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                result.add(sourceAsMap);
            }
        }

        return result;
    }
}
