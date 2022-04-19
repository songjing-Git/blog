package com.threeman.servicecore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.common.exception.CreateException;
import com.threeman.common.utils.DateUtil;
import com.threeman.servicecore.entity.BlogInfo;
import com.threeman.servicecore.entity.Comment;
import com.threeman.servicecore.entity.Support;
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
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    /**
     * 插入博客文章
     * @param param
     * @return
     */
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

    /**
     * 搜索博客文章
     * @param text
     * @return
     */
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

    /**
     * 查找博客文章
     * @param blogId
     * @return
     */
    @Override
    public Map<String, Object> findBlogInfo(long blogId,long userId) {
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
        if (userId!=0){
            Boolean member = redisTemplate.opsForSet().isMember("blogSupport_" + blogId, "user_" + userId);
            blogInfoMap.put("support",member);
        }

        blogInfoMap.put("tags",tagList);
        blogInfoMap.put("imageUrl",imageUrlList);
        Map<String, Object> userInfoMap = JSON.parseObject(JSONObject.toJSONStringWithDateFormat(user,"yyyy-MM-dd HH:mm:ss"), new TypeReference<Map<String, Object>>() {
        });
        blogInfoMap.putAll(userInfoMap);
        return blogInfoMap;
    }

    /**
     * 分页搜索文章
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

    /**
     * 添加浏览量
     * @param blogInfoId
     * @return
     */
    @Override
    public long addBlogView(long blogInfoId) {
        return redisTemplate.opsForHash().increment("view",String.valueOf(blogInfoId),1L);
    }

    /*@Autowired
    RedisTemplate<String,String> strRedisTemplate;*/

    /**
     * 博客点赞和取消
     *
     * @param support
     * @return
     */
    @Override
    public long blogSupport(Support support) {
        Boolean member = redisTemplate.opsForSet().isMember("blogSupport_" + support.getId(),"user_"+support.getUserId());
        assert member!=null;
        if (member){
            redisTemplate.opsForSet().remove("blogSupport_" + support.getId(), "user_"+support.getUserId());
        }else {
            redisTemplate.opsForSet().add("blogSupport_"+support.getId(),"user_"+support.getUserId());
        }
        Long size = redisTemplate.opsForSet().size("blogSupport_" + support.getId());
        return  size==null?0:size;
    }

    /**
     * 评论点赞和取消
     * @param support
     * @return
     */
    @Override
    public long commentSupport(Support support) {
        Boolean member = redisTemplate.opsForSet().isMember("commentSupport_" + support.getId(), "user_" + support.getUserId());
        assert member!=null;
        if (member){
            redisTemplate.opsForSet().remove("commentSupport_" + support.getId(), "user_" + support.getUserId());
        }else {
            redisTemplate.opsForSet().add("commentSupport_" + support.getId(), "user_" + support.getUserId());
        }
        Long size = redisTemplate.opsForSet().size("commentSupport_" + support.getId());
        return size==null?0:size;

    }


    /**
     * 添加博客评论
     * @param comment
     * @return
     */
    @Override
    public long addBlogComment(Comment comment) {
        comment.setCommentId(IdWorker.getId());
        BlogInfo blogInfo = blogInfoMapper.selectById(comment.getBlogId());
        if (blogInfo==null){
            throw new CreateException("该博文不存在");
        }
        User user = userMapper.selectById(blogInfo.getBlogAuthorId());
        if (user==null){
            throw new CreateException("该作者不存在");
        }
        if(comment.getNickName().equals(user.getUserName())){
            comment.setAuthor(true);
        }else {
            comment.setAuthor(false);
        }
        comment.setCommentDate(DateUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        String blogId = comment.getBlogId().toString();
        Long aLong = redisTemplate.opsForList().rightPush(blogId, comment);
        return aLong==null?0:aLong;
    }

    /**
     * 查看博客评论
     * @param blogInfoId
     * @return
     */
    @Override
    public List<Comment> findBlogComment(long blogInfoId,long userId) {
        String blogId = String.valueOf(blogInfoId);
        List<Object> ranges = redisTemplate.opsForList().range(blogId, 0, -1);
        List <Comment> commentList=new ArrayList<>();
        if (ranges!=null){
            for (Object comment:ranges){
                commentList.add((Comment)comment);
            }
        }
        List<Comment> comments = getComments(commentList, 0);
        return findParent(comments,userId);
    }

    @Override
    public long getBlogSupportCount(long blogId) {
        Long size = redisTemplate.opsForSet().size("blogSupport_" + blogId);
        return size==null?0:size;
    }

    @Override
    public long getCommentSupportCount(long blogId) {
        Long size = redisTemplate.opsForSet().size("commentSupport_" + blogId);
        return size==null?0:size;
    }


    private List<Comment> getComments(List<Comment> commentList,long pid){
        if (commentList==null){
            return null;
        }
        List<Comment> treeList=new ArrayList<>();
        for (Comment comment : commentList) {
            if (pid==comment.getParentId()){
                comment.setChildren(getComments(commentList, comment.getCommentId()));
                treeList.add(comment);
            }
        }
        return treeList;
    }

    public List<Comment> findParent(List<Comment> comments,long userId) {

        for (Comment comment : comments) {

            // 防止checkForComodification(),而建立一个新集合
            ArrayList<Comment> fatherChildren = new ArrayList<>();

            // 递归处理子级的回复，即回复内有回复
            findChildren(comment, fatherChildren,userId);

            // 将递归处理后的集合放回父级的孩子中
            comment.setChildren(fatherChildren);
            Long size = redisTemplate.opsForSet().size("commentSupport_" + comment.getCommentId());
            comment.setSupportCount(size);
            User userInfo = userMapper.getUserInfo(comment.getNickName());
            comment.setNickAvater(userInfo.getAvater());
            User parentInfo = userMapper.getUserInfo(comment.getParentName());
            Boolean member = redisTemplate.opsForSet().isMember("commentSupport_" + comment.getCommentId(), "user_"+userId);
            comment.setSupport(member != null);
            comment.setParentAvater(parentInfo.getAvater());
        }
        return comments;
    }


    public void findChildren(Comment parent, List<Comment> fatherChildren,long userId) {

        // 找出直接子级
        List<Comment> comments = parent.getChildren();

        // 遍历直接子级的子级
        for (Comment comment : comments) {

            // 若非空，则还有子级，递归
            if (!comment.getChildren().isEmpty()) {
                findChildren(comment, fatherChildren,userId);
            }
            User userInfo = userMapper.getUserInfo(comment.getNickName());
            comment.setNickAvater(userInfo.getAvater());
            User parentInfo = userMapper.getUserInfo(comment.getParentName());
            comment.setParentAvater(parentInfo.getAvater());
            Long size = redisTemplate.opsForSet().size("commentSupport_" + comment.getCommentId());
            comment.setSupportCount(size);
            Boolean member = redisTemplate.opsForSet().isMember("commentSupport_" + comment.getCommentId(), "user_"+userId);
            comment.setSupport(member);
            // 已经到了最底层的嵌套关系，将该回复放入新建立的集合
            fatherChildren.add(comment);

            // 容易忽略的地方：将相对底层的子级放入新建立的集合之后
            // 则表示解除了嵌套关系，对应的其父级的子级应该设为空
            comment.setChildren(new ArrayList<>());
        }
    }

}
