package com.threeman.servicecore.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.threeman.servicecore.entity.BlogInfo;
import com.threeman.servicecore.entity.Comment;
import com.threeman.servicecore.entity.Support;

import java.util.List;
import java.util.Map;

/**
 * 博文信息表(BlogInfo)表服务接口
 *
 * @author songjing
 * @since 2022-02-28 15:34:57
 */
public interface BlogInfoService extends IService<BlogInfo> {

    boolean insertBlogInfo(Map<String, Object> param);

    List<Map<String, Object>> findBlogInfos(String text);

    Map<String, Object> findBlogInfo(long blogId, long userId);

    List<Map<String, Object>> findBlogInfosByPage(String text, int from, int size);

    long addBlogView(long blogInfoId);

    long blogSupport(Support support);

    long commentSupport(Support support);

    long addBlogComment(Comment comment);


    List<Comment> findBlogComment(long blogInfoId, long userId);

    long getBlogSupportCount(long blogId);

    long getCommentSupportCount(long blogId);
}
