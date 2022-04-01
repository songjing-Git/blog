package com.threeman.servicecore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.servicecore.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 博客评论表(Comment)表服务接口
 *
 * @author songjing
 * @since 2022-03-31 10:47:14
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}
