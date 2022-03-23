package com.threeman.servicecore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.common.exception.CreateException;
import com.threeman.common.utils.DateUtil;
import com.threeman.servicecore.entity.BlogInfo;
import com.threeman.servicecore.entity.User;
import com.threeman.servicecore.mapper.BlogInfoMapper;
import com.threeman.servicecore.mapper.UserMapper;
import com.threeman.servicecore.service.BlogInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    UserMapper userMapper;

    @SneakyThrows
    @Override
    public boolean insertBlogInfo(Map<String,Object> param) {

        log.info("blogInfo:{}",param);
        if (MapUtils.isEmpty(param)){
            throw new CreateException("参数不能为空");
        }
        //参数转化
        //博文作者
        String authorName = MapUtils.getString(param, "blog_author_name");
        User userInfo = userMapper.getUserInfo(authorName);
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setBlogAuthorId(userInfo.getUserId());
        blogInfo.setBlogTitle(MapUtils.getString(param, "blog_title"));
        blogInfo.setComments(MapUtils.getLongValue(param, "comments",0));
        String context = MapUtils.getString(param, "context");
        blogInfo.setContext(context);
        Date createTime = DateUtil.localDateTimeConvertToDate(LocalDateTime.now());
        blogInfo.setCreateTime(createTime);
        Object imageUrl = MapUtils.getObject(param, "image_url");
        if (imageUrl!=null){
            List<String> imageUrlList=new ArrayList<>();
            if (imageUrl instanceof List){
                for (Object o : (List<?>) imageUrl) {
                    imageUrlList.add((String) o);
                }
                log.info("imageUrl:{}",imageUrl);
                blogInfo.setImageUrl(String.join(",", imageUrlList));
            }
        }
        int open = MapUtils.getIntValue(param, "open",1);
        blogInfo.setOpen(open);
        int immediate = MapUtils.getIntValue(param, "immediate",1);
        blogInfo.setImmediate(immediate);
        if (immediate==0){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String modalTime = MapUtils.getString(param, "release_time");
            Date releaseTime = df.parse(modalTime);
            if (releaseTime.before(createTime)){
                throw new CreateException("发布时间不得早于当前时间");
            }
            blogInfo.setReleaseTime(releaseTime);
        }
        String summary = MapUtils.getString(param, "summary");
        //没有摘要信息截取文章前100个字符
        if (StringUtils.isEmpty(summary)){
            if (context.length()>=100){
                blogInfo.setSummary(context.substring(0,100));
            }else {
                blogInfo.setSummary(context.substring(0,context.length()-1));
            }

        }else {
            blogInfo.setSummary(summary);
        }

        Object tags = MapUtils.getObject(param, "tags");
        List<String> result=new ArrayList<>();
        if (tags instanceof List){
            for (Object o : (List<?>) tags) {
                result.add((String) o);
            }
            log.info("tags:{}",tags);
            String tag = String.join(",", result);
            blogInfo.setTags(tag);
        }
        blogInfo.setBlogType(MapUtils.getIntValue(param,"blog_type",1));
        blogInfo.setSupports(MapUtils.getIntValue(param,"supports",0));
        blogInfo.setViews(MapUtils.getIntValue(param,"views",0));
        //插入数据库
        int insert = blogInfoMapper.insert(blogInfo);
        if (insert==0){
            throw new CreateException("插入数据库失败");
        }
        QueryWrapper<BlogInfo> queryWrapper = new QueryWrapper<>();
        blogInfo = blogInfoMapper.selectOne(queryWrapper.eq(param.get("blog_title") != null, "blog_title", param.get("blog_title")));

        //插入es
        GetIndexRequest getRequest = new GetIndexRequest(blog_index);
        boolean exists =restHighLevelClient.indices().exists(getRequest,RequestOptions.DEFAULT);
        log.info("exists:{}",exists);
        if (!exists){
            throw new CreateException("es索引不存在");
        }
        //存入作者
        Map<String,Object> map = JSONObject.parseObject(JSON.toJSONString(blogInfo));
        map.put("blog_author_name",authorName);
        map.remove("blog_author_id");
        IndexRequest indexRequest = new IndexRequest(blog_index);
        indexRequest.id(String.valueOf(blogInfo.getBlogInfoId())).source(JSON.toJSONString(map), XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return index.status().getStatus()!=0;
    }

    @Override
    public List<Map<String,Object>> findBlogInfos(String text) {
        SearchRequest searchRequest = new SearchRequest(blog_index);
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(text)
                .field("context")
                .field("blog_title")
                .field("blog_author_name")
                .field("summary");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search ;
        try {
            search =restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            throw new CreateException(e.getMessage());
        }
        List<Map<String,Object>> result=new ArrayList<>();
        if (search!=null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                long createTime = MapUtils.getLongValue(sourceAsMap, "createTime", 0);
                sourceAsMap.put("createTime",df.format(new Date(createTime)));
                long releaseTime = MapUtils.getLongValue(sourceAsMap, "releaseTime", 0);
                if (releaseTime!=0){
                    sourceAsMap.put("releaseTime",df.format(new Date(releaseTime)));
                }else {
                    sourceAsMap.put("releaseTime",null);
                }
                String imageUrls = MapUtils.getString(sourceAsMap, "imageUrl");
                List<String> imageUrlList=new ArrayList<>();
                if (!StringUtils.isEmpty(imageUrls)){
                    String[] imageUrl = imageUrls.split(",");
                    imageUrlList= new ArrayList<>(Arrays.asList(imageUrl));
                }
                sourceAsMap.put("imageUrl",imageUrlList);
                String tags = MapUtils.getString(sourceAsMap, "tags");
                if (!StringUtils.isEmpty(tags)){
                    String[] tag = tags.split(",");
                    List<String> tagList = new ArrayList<>(Arrays.asList(tag));
                    sourceAsMap.put("tags",tagList);
                }
                log.info("sourceAsMap:{}",sourceAsMap);
                result.add(sourceAsMap);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> findBlogInfo(long blogId) {
        BlogInfo blogInfo = blogInfoMapper.selectById(blogId);
        if (blogInfo==null){
            return null;
        }
        User user = userMapper.selectById(blogInfo.getBlogAuthorId());
        if (user==null){
            throw new CreateException("无作者信息");
        }
        String tag = blogInfo.getTags();
        List<String> tagList=new ArrayList<>();
        if (!StringUtils.isEmpty(tag)){
            String[] tags = tag.split(",");
            tagList= new ArrayList<>(Arrays.asList(tags));
        }
        String imageUrl = blogInfo.getImageUrl();
        List<String> imageUrlList=new ArrayList<>();
        if (!StringUtils.isEmpty(imageUrl)){
            String[] imageUrls = imageUrl.split(",");
            imageUrlList = new ArrayList<>(Arrays.asList(imageUrls));
        }
        Map<String, Object> blogInfoMap = JSON.parseObject(JSONObject.toJSONStringWithDateFormat(blogInfo,"yyyy-MM-dd HH:mm:ss"), new TypeReference<Map<String, Object>>() {
        });
        blogInfoMap.put("tags",tagList);
        blogInfoMap.put("imageUrl",imageUrlList);
        Map<String, Object> userInfoMap = JSON.parseObject(JSONObject.toJSONStringWithDateFormat(user,"yyyy-MM-dd HH:mm:ss"), new TypeReference<Map<String, Object>>() {
        });
        blogInfoMap.putAll(userInfoMap);
        return blogInfoMap;
    }

    @Override
    public List<Map<String, Object>> findBlogInfosByPage(String text, int from, int size) {
        SearchRequest searchRequest = new SearchRequest(blog_index);
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(text)
                .field("context")
                .field("blog_title")
                .field("blog_author_name")
                .field("summary");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置高亮查询
        HighlightBuilder hiBuilder=new HighlightBuilder();
        hiBuilder.preTags("<font style='color:red'>");
        hiBuilder.postTags("</font>");
        hiBuilder.field("context")
                .field("blog_title")
                .field("blog_author_name")
                .field("summary");
        searchSourceBuilder.query(queryBuilder).from(from).size(size).highlighter(hiBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search ;
        try {
            search =restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            throw new CreateException(e.getMessage());
        }
        List<Map<String,Object>> result=new ArrayList<>();
        if (search!=null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                log.info("highlightFields:{}",highlightFields);
                HighlightField content = highlightFields.get("blog_author_name");
                log.info("blog_author_name:{}",content);
                if (content!=null){
                    Text[] fragments = content.fragments();
                    StringBuilder newTitle= new StringBuilder();
                    for (Text t : fragments) {
                        newTitle.append(t);
                    }
                    sourceAsMap.put("blog_author_name", newTitle.toString());
                }


                long createTime = MapUtils.getLongValue(sourceAsMap, "createTime", 0);
                sourceAsMap.put("createTime",df.format(new Date(createTime)));
                long releaseTime = MapUtils.getLongValue(sourceAsMap, "releaseTime", 0);
                if (releaseTime!=0){
                    sourceAsMap.put("releaseTime",df.format(new Date(releaseTime)));
                }else {
                    sourceAsMap.put("releaseTime",null);
                }
                String imageUrls = MapUtils.getString(sourceAsMap, "imageUrl");
                if (!StringUtils.isEmpty(imageUrls)){
                    String[] imageUrl = imageUrls.split(",");
                    List<String> imageUrlList = new ArrayList<>(Arrays.asList(imageUrl));
                    sourceAsMap.put("imageUrl",imageUrlList);
                }
                String tags = MapUtils.getString(sourceAsMap, "tags");
                if (!StringUtils.isEmpty(tags)){
                    String[] tag = tags.split(",");
                    List<String> tagList = new ArrayList<>(Arrays.asList(tag));
                    sourceAsMap.put("tags",tagList);
                }
                result.add(sourceAsMap);
            }
        }
        return result;
    }
}
