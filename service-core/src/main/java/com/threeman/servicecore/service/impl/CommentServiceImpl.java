package com.threeman.servicecore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.servicecore.entity.Comment;
import com.threeman.servicecore.mapper.CommentMapper;
import com.threeman.servicecore.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 博客评论表(Comment)表服务实现类
 *
 * @author songjing
 * @since 2022-03-31 10:47:14
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
