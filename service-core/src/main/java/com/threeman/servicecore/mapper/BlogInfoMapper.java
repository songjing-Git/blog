package com.threeman.servicecore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.servicecore.entity.BlogInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 博文信息表(BlogInfo)表服务接口
 *
 * @author songjing
 * @since 2022-02-28 15:34:57
 */
@Mapper
@Repository
public interface BlogInfoMapper extends BaseMapper<BlogInfo> {


    /**
     * 更新浏览数
     *
     * @param blogInfoId 博客id
     * @param views      浏览总数
     * @return boolean
     */
    @Update("update blog_info set views = #{views} where blog_info_id = #{blogInfoId}")
    boolean updateBlogViews(long blogInfoId, long views);

    /**
     * 更新博客评论数
     *
     * @param blogInfoId 博客id
     * @param supports   点赞总数
     * @return boolean
     */
    @Update("update blog_info set supports = #{supports} where blog_info_id = #{blogInfoId}")
    boolean updateBlogSupports(long blogInfoId, long supports);

    /**
     * 更新博客评论数
     *
     * @param blogInfoId 博客id
     * @param comments   评论总数
     * @return boolean
     */
    @Update("update blog_info set comments = #{comments} where blog_info_id = #{blogInfoId}")
    boolean updateBlogComments(long blogInfoId, long comments);
}
