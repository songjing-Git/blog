package com.threeman.servicecore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.common.exception.CreateException;
import com.threeman.common.thread.JucConfig;
import com.threeman.common.utils.DateUtil;
import com.threeman.servicecore.entity.BlogInfo;
import com.threeman.servicecore.entity.Comment;
import com.threeman.servicecore.entity.Support;
import com.threeman.servicecore.entity.User;
import com.threeman.servicecore.mapper.BlogInfoMapper;
import com.threeman.servicecore.mapper.CommentMapper;
import com.threeman.servicecore.mapper.UserMapper;
import com.threeman.servicecore.service.BlogInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ???????????????(BlogInfo)??????????????????
 *
 * @author songjing
 * @since 2022-02-28 15:34:57
 */
@Service
@Slf4j
public class BlogInfoServiceImpl extends ServiceImpl<BlogInfoMapper, BlogInfo> implements BlogInfoService {

    private final String blog_index = "blog";

    @Autowired
    BlogInfoMapper blogInfoMapper;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    CommentMapper commentMapper;

    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
    @SneakyThrows
    @Override
    public boolean insertBlogInfo(Map<String, Object> param) {

        log.info("blogInfo:{}", param);
        if (MapUtils.isEmpty(param)) {
            throw new CreateException("??????????????????");
        }
        //????????????
        //????????????
        String authorName = MapUtils.getString(param, "blog_author_name");
        User userInfo = userMapper.getUserInfo(authorName);
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setBlogAuthorId(userInfo.getUserId());
        blogInfo.setBlogTitle(MapUtils.getString(param, "blog_title"));
        blogInfo.setComments(MapUtils.getLongValue(param, "comments", 0));
        String context = MapUtils.getString(param, "context");
        blogInfo.setContext(context);
        Date createTime = DateUtil.localDateTimeConvertToDate(LocalDateTime.now());
        blogInfo.setCreateTime(createTime);
        Object imageUrl = MapUtils.getObject(param, "image_url");
        if (imageUrl != null) {
            List<String> imageUrlList = new ArrayList<>();
            if (imageUrl instanceof List) {
                for (Object o : (List<?>) imageUrl) {
                    imageUrlList.add((String) o);
                }
                log.info("imageUrl:{}", imageUrl);
                blogInfo.setImageUrl(String.join(",", imageUrlList));
            }
        }
        int open = MapUtils.getIntValue(param, "open", 1);
        blogInfo.setOpen(open);
        int immediate = MapUtils.getIntValue(param, "immediate", 1);
        blogInfo.setImmediate(immediate);
        if (immediate == 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String modalTime = MapUtils.getString(param, "release_time");
            Date releaseTime = df.parse(modalTime);
            if (releaseTime.before(createTime)) {
                throw new CreateException("????????????????????????????????????");
            }
            blogInfo.setReleaseTime(releaseTime);
        }
        String summary = MapUtils.getString(param, "summary");
        //?????????????????????????????????100?????????
        if (StringUtils.isEmpty(summary)) {
            if (context.length() >= 100) {
                blogInfo.setSummary(context.substring(0, 100));
            } else {
                blogInfo.setSummary(context.substring(0, context.length() - 1));
            }

        } else {
            blogInfo.setSummary(summary);
        }

        Object tags = MapUtils.getObject(param, "tags");
        List<String> result = new ArrayList<>();
        if (tags instanceof List) {
            for (Object o : (List<?>) tags) {
                result.add((String) o);
            }
            log.info("tags:{}", tags);
            String tag = String.join(",", result);
            blogInfo.setTags(tag);
        }
        blogInfo.setBlogType(MapUtils.getIntValue(param, "blog_type", 1));
        blogInfo.setSupports(MapUtils.getIntValue(param, "supports", 0));
        blogInfo.setViews(MapUtils.getIntValue(param, "views", 0));
        ThreadPoolExecutor threadPoolExecutor = JucConfig.getThreadPoolExecutor(authorName);
        threadPoolExecutor.execute(() -> {
            //???????????????
            int insert = blogInfoMapper.insert(blogInfo);
            if (insert == 0) {
                throw new CreateException("?????????????????????");
            }

        });
//        QueryWrapper<BlogInfo> queryWrapper = new QueryWrapper<>();
//        blogInfo = blogInfoMapper.selectOne(queryWrapper.eq(param.get("blog_title") != null, "blog_title", param.get("blog_title")));

        //??????es
        GetIndexRequest getRequest = new GetIndexRequest(blog_index);
        boolean exists = restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
        log.info("exists:{}", exists);
        if (!exists) {
            throw new CreateException("es???????????????");
        }
        //????????????
        Map<String, Object> map = JSONObject.parseObject(JSON.toJSONString(blogInfo));
        map.put("blog_author_name", authorName);
        map.remove("blog_author_id");
        IndexRequest indexRequest = new IndexRequest(blog_index);
        indexRequest.id(String.valueOf(blogInfo.getBlogInfoId())).source(JSON.toJSONString(map), XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return index.status().getStatus() != 0;
    }

    /**
     * ??????????????????
     *
     * @param text
     * @return
     */
    @Override
    public List<Map<String, Object>> findBlogInfos(String text) {
        SearchRequest searchRequest = new SearchRequest(blog_index);
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(text)
                .field("context")
                .field("blog_title")
                .field("blog_author_name")
                .field("summary");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
        List<Map<String, Object>> result = new ArrayList<>();
        if (search != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                long createTime = MapUtils.getLongValue(sourceAsMap, "createTime", 0);
                sourceAsMap.put("createTime", df.format(new Date(createTime)));
                long releaseTime = MapUtils.getLongValue(sourceAsMap, "releaseTime", 0);
                if (releaseTime != 0) {
                    sourceAsMap.put("releaseTime", df.format(new Date(releaseTime)));
                } else {
                    sourceAsMap.put("releaseTime", null);
                }
                String imageUrls = MapUtils.getString(sourceAsMap, "imageUrl");
                List<String> imageUrlList = new ArrayList<>();
                if (!StringUtils.isEmpty(imageUrls)) {
                    String[] imageUrl = imageUrls.split(",");
                    imageUrlList = new ArrayList<>(Arrays.asList(imageUrl));
                }
                sourceAsMap.put("imageUrl", imageUrlList);
                String tags = MapUtils.getString(sourceAsMap, "tags");
                if (!StringUtils.isEmpty(tags)) {
                    String[] tag = tags.split(",");
                    List<String> tagList = new ArrayList<>(Arrays.asList(tag));
                    sourceAsMap.put("tags", tagList);
                }
                log.info("sourceAsMap:{}", sourceAsMap);
                result.add(sourceAsMap);
            }
        }
        return result;
    }

    /**
     * ??????????????????
     *
     * @param blogId
     * @return
     */
    @Override
    public Map<String, Object> findBlogInfo(long blogId, long userId) {
        BlogInfo blogInfo = blogInfoMapper.selectById(blogId);
        if (blogInfo == null) {
            return null;
        }
        User user = userMapper.selectById(blogInfo.getBlogAuthorId());
        if (user == null) {
            throw new CreateException("???????????????");
        }
        String tag = blogInfo.getTags();
        List<String> tagList = new ArrayList<>();
        if (!StringUtils.isEmpty(tag)) {
            String[] tags = tag.split(",");
            tagList = new ArrayList<>(Arrays.asList(tags));
        }
        String imageUrl = blogInfo.getImageUrl();
        List<String> imageUrlList = new ArrayList<>();
        if (!StringUtils.isEmpty(imageUrl)) {
            String[] imageUrls = imageUrl.split(",");
            imageUrlList = new ArrayList<>(Arrays.asList(imageUrls));
        }
        Map<String, Object> blogInfoMap = JSON.parseObject(JSONObject.toJSONStringWithDateFormat(blogInfo, "yyyy-MM-dd HH:mm:ss"), new TypeReference<Map<String, Object>>() {
        });
        if (userId != 0) {
            Boolean member = redisTemplate.opsForSet().isMember("blogSupport_" + blogId, "user_" + userId);
            blogInfoMap.put("support", member);
        }
        blogInfoMap.put("tags", tagList);
        blogInfoMap.put("imageUrl", imageUrlList);
        Map<String, Object> userInfoMap = JSON.parseObject(JSONObject.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss"), new TypeReference<Map<String, Object>>() {
        });
        blogInfoMap.putAll(userInfoMap);
        return blogInfoMap;
    }

    /**
     * ??????????????????
     *
     * @param text
     * @param from
     * @param size
     * @return
     */
    @Override
    public List<Map<String, Object>> findBlogInfosByPage(String text, int from, int size) {
        SearchRequest searchRequest = new SearchRequest(blog_index);
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(text)
                .field("context")
                .field("blog_title")
                .field("blog_author_name")
                .field("summary");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //??????????????????
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<font style='color:red'>");
        hiBuilder.postTags("</font>");
        hiBuilder.field("context")
                .field("blog_title")
                .field("blog_author_name")
                .field("summary");
        searchSourceBuilder.query(queryBuilder).from(from).size(size).highlighter(hiBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
        List<Map<String, Object>> result = new ArrayList<>();
        if (search != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                log.info("highlightFields:{}", highlightFields);
                HighlightField content = highlightFields.get("blog_author_name");
                log.info("blog_author_name:{}", content);
                if (content != null) {
                    Text[] fragments = content.fragments();
                    StringBuilder newTitle = new StringBuilder();
                    for (Text t : fragments) {
                        newTitle.append(t);
                    }
                    sourceAsMap.put("blog_author_name", newTitle.toString());
                }


                long createTime = MapUtils.getLongValue(sourceAsMap, "createTime", 0);
                sourceAsMap.put("createTime", df.format(new Date(createTime)));
                long releaseTime = MapUtils.getLongValue(sourceAsMap, "releaseTime", 0);
                if (releaseTime != 0) {
                    sourceAsMap.put("releaseTime", df.format(new Date(releaseTime)));
                } else {
                    sourceAsMap.put("releaseTime", null);
                }
                String imageUrls = MapUtils.getString(sourceAsMap, "imageUrl");
                if (!StringUtils.isEmpty(imageUrls)) {
                    String[] imageUrl = imageUrls.split(",");
                    List<String> imageUrlList = new ArrayList<>(Arrays.asList(imageUrl));
                    sourceAsMap.put("imageUrl", imageUrlList);
                }
                String tags = MapUtils.getString(sourceAsMap, "tags");
                if (!StringUtils.isEmpty(tags)) {
                    String[] tag = tags.split(",");
                    List<String> tagList = new ArrayList<>(Arrays.asList(tag));
                    sourceAsMap.put("tags", tagList);
                }
                result.add(sourceAsMap);
            }
        }
        return result;
    }

    /**
     * ???????????????
     *
     * @param blogInfoId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addBlogView(long blogInfoId) {
        ThreadPoolExecutor threadPoolExecutor = JucConfig.getThreadPoolExecutor(String.valueOf(blogInfoId));
        long view = redisTemplate.opsForHash().increment("view", String.valueOf(blogInfoId), 1L);
        //??????mysql
        threadPoolExecutor.execute(() -> {
            log.info("mysql-blogInfoId:{}", blogInfoId);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("mysql-view:{}", view);
            boolean b = blogInfoMapper.updateBlogViews(blogInfoId, view);
            if (!b) {
                throw new CreateException("??????mysql??????");
            }
        });
        //??????es
        threadPoolExecutor.execute(() -> {
            log.info("es-blogInfoId:{}", blogInfoId);
            log.info("es-view:{}", view);
            UpdateRequest updateRequest = new UpdateRequest();
            try {
                XContentBuilder mapping = XContentFactory.jsonBuilder();
                UpdateRequest doc = updateRequest.index(blog_index)
                        .id(String.valueOf(blogInfoId))
                        .doc(mapping.startObject().field("views", view).endObject());
                UpdateResponse update = restHighLevelClient.update(doc, RequestOptions.DEFAULT);
                int status = update.status().getStatus();
                log.info("status:{}", status);
            } catch (IOException e) {
                throw new CreateException("??????es??????");
            }
        });
        return view;
    }

    /**
     * ?????????????????????
     *
     * @param support
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long blogSupport(Support support) {
        ThreadPoolExecutor threadPoolExecutor = JucConfig.getThreadPoolExecutor(support.getUserId().toString());
        Boolean member = redisTemplate.opsForSet().isMember("blogSupport_" + support.getId(), "user_" + support.getUserId());
        assert member != null;
        if (member) {
            redisTemplate.opsForSet().remove("blogSupport_" + support.getId(), "user_" + support.getUserId());
        } else {
            redisTemplate.opsForSet().add("blogSupport_" + support.getId(), "user_" + support.getUserId());
        }
        Long size = redisTemplate.opsForSet().size("blogSupport_" + support.getId());
        assert size != null;
        threadPoolExecutor.execute(() -> {
            boolean b = blogInfoMapper.updateBlogSupports(support.getId(), size);
            if (!b) {
                throw new CreateException("??????mysql??????");
            }
        });
        threadPoolExecutor.execute(() -> {
            UpdateRequest updateRequest = new UpdateRequest();
            try {
                XContentBuilder mapping = XContentFactory.jsonBuilder();
                UpdateRequest doc = updateRequest.index(blog_index)
                        .id(String.valueOf(support.getId()))
                        .doc(mapping.startObject().field("supports", size).endObject());
                UpdateResponse update = restHighLevelClient.update(doc, RequestOptions.DEFAULT);
                int status = update.status().getStatus();
                log.info("status:{}", status);
            } catch (IOException e) {
                throw new CreateException("??????es??????");
            }
        });
        return size;
    }

    /**
     * ?????????????????????
     *
     * @param support
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long commentSupport(Support support) {
        Boolean member = redisTemplate.opsForSet().isMember("commentSupport_" + support.getId(), "user_" + support.getUserId());
        assert member != null;
        if (member) {
            redisTemplate.opsForSet().remove("commentSupport_" + support.getId(), "user_" + support.getUserId());
        } else {
            redisTemplate.opsForSet().add("commentSupport_" + support.getId(), "user_" + support.getUserId());
        }
        Long size = redisTemplate.opsForSet().size("commentSupport_" + support.getId());
        assert size != null;

        ThreadPoolExecutor threadPoolExecutor = JucConfig.getThreadPoolExecutor(support.getUserId().toString());

        threadPoolExecutor.execute(() -> {
            boolean b = blogInfoMapper.updateBlogComments(support.getId(), size);
            if (!b) {
                throw new CreateException("??????mysql??????");
            }
        });

        threadPoolExecutor.execute(() -> {
            UpdateRequest updateRequest = new UpdateRequest();
            try {
                XContentBuilder mapping = XContentFactory.jsonBuilder();
                UpdateRequest doc = updateRequest.index(blog_index)
                        .id(String.valueOf(support.getId()))
                        .doc(mapping.startObject().field("comments", size).endObject());
                UpdateResponse update = restHighLevelClient.update(doc, RequestOptions.DEFAULT);
                int status = update.status().getStatus();
                log.info("status:{}", status);
            } catch (IOException e) {
                throw new CreateException("??????es??????");
            }
        });
        return size;
    }


    /**
     * ??????????????????
     *
     * @param comment
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addBlogComment(Comment comment) {
        log.info("comment:{}",comment);
        comment.setCommentId(String.valueOf(IdWorker.getId()));
        BlogInfo blogInfo = blogInfoMapper.selectById(comment.getBlogId());
        if (blogInfo == null) {
            throw new CreateException("??????????????????");
        }
        User user = userMapper.selectById(blogInfo.getBlogAuthorId());
        if (user == null) {
            throw new CreateException("??????????????????");
        }
        if (comment.getNickName().equals(user.getUserName())) {
            comment.setAuthor(true);
        } else {
            comment.setAuthor(false);
        }
        Long size = redisTemplate.opsForSet().size("commentSupport_" + comment.getCommentId());
        comment.setSupportCount(size);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format = dateFormat.format(date);

        comment.setCommentDate(format);
        ThreadPoolExecutor threadPoolExecutor = JucConfig.getThreadPoolExecutor(comment.getNickName());
        threadPoolExecutor.execute(() -> {
            int insert = commentMapper.insertComment(comment);
            if (insert == 0) {
                throw new CreateException("????????????mysql??????");
            }
        });
        User userInfo = userMapper.getUserInfo(comment.getNickName());
        comment.setNickAvatar(userInfo.getAvatar());
        if (!StringUtils.isEmpty(comment.getParentName())){
            User parentInfo = userMapper.getUserInfo(comment.getParentName());
            comment.setParentAvatar(parentInfo.getAvatar());
        }
        String blogId = comment.getBlogId().toString();
        Long aLong = redisTemplate.opsForList().rightPush(blogId, comment);
        return aLong == null ? 0 : aLong;
    }

    /**
     * ??????????????????
     *
     * @param blogInfoId
     * @return
     */
    @Override
    public List<Comment> findBlogComment(long blogInfoId, long userId) {
        String blogId = String.valueOf(blogInfoId);
        List<Object> ranges = redisTemplate.opsForList().range(blogId, 0, -1);
        List<Comment> commentList = new ArrayList<>();
        log.info("ranges:{}",ranges);
        if (ranges != null) {
            for (Object comment : ranges) {
                commentList.add((Comment) comment);
            }
        }
        List<Comment> comments = getComments(commentList, "");
        /*if (comments.size()==0){
            return null;
        }*/
        return findParent(comments, userId);
    }
    private List<Comment> getComments(List<Comment> commentList, String pid) {
        if (commentList == null) {
            return null;
        }
        List<Comment> treeList = new ArrayList<>();
        for (Comment comment : commentList) {
            /*log.info("comment.getParentId():{}",comment.getParentId());
            if (StringUtils.isEmpty(comment.getParentId())){
                treeList.add(comment);
                continue;
            }*/
            log.info("comment.getParentId():{}",comment.getParentId());
            log.info("pid:{}",pid);
            log.info("equals:{}",pid.equals(comment.getParentId()));
            if (pid.equals(comment.getParentId()) ) {
                comment.setChildren(getComments(commentList, comment.getCommentId()));
                treeList.add(comment);
            }
        }
        return treeList;
    }
    private List<Comment> findParent(List<Comment> comments, long userId) {
        for (Comment comment : comments) {

            // ??????checkForComodification(),????????????????????????
            ArrayList<Comment> fatherChildren = new ArrayList<>();

            // ???????????????????????????????????????????????????
            findChildren(comment, fatherChildren, userId);

            // ???????????????????????????????????????????????????
            comment.setChildren(fatherChildren);
            Boolean member = redisTemplate.opsForSet().isMember("commentSupport_" + comment.getCommentId(), "user_" + userId);
            comment.setSupport(member != null);
            String commentDate = comment.getCommentDate();
            comment.setCommentDate(DateUtil.getPastTime(commentDate));
        }
        return comments;
    }


    private void findChildren(Comment parent, List<Comment> fatherChildren, long userId) {

        // ??????????????????
        List<Comment> comments = parent.getChildren();
        /*if (comments==null){
            return;
        }*/
        // ???????????????????????????
        for (Comment comment : comments) {

            // ????????????????????????????????????
            if (!comment.getChildren().isEmpty()) {
                findChildren(comment, fatherChildren, userId);
            }
            Boolean member = redisTemplate.opsForSet().isMember("commentSupport_" + comment.getCommentId(), "user_" + userId);
            comment.setSupport(member != null);
            comment.setCommentDate(DateUtil.getPastTime(comment.getCommentDate()));
            // ???????????????????????????????????????????????????????????????????????????
            fatherChildren.add(comment);

            // ??????????????????????????????????????????????????????????????????????????????
            // ???????????????????????????????????????????????????????????????????????????
            comment.setChildren(new ArrayList<>());
        }
    }



    @Override
    public long getBlogSupportCount(long blogId) {
        Long size = redisTemplate.opsForSet().size("blogSupport_" + blogId);
        return size == null ? 0 : size;
    }

    @Override
    public long getCommentSupportCount(long blogId) {
        Long size = redisTemplate.opsForSet().size("commentSupport_" + blogId);
        return size == null ? 0 : size;
    }






}
