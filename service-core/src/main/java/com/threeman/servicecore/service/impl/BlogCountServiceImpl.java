package com.threeman.servicecore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.servicecore.entity.BlogCount;
import com.threeman.servicecore.mapper.BlogCountMapper;
import com.threeman.servicecore.service.BlogCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 博客统计表(BlogCount)表服务实现类
 *
 * @author songjing
 * @since 2022-03-30 15:53:51
 */
@Service
@Slf4j
public class BlogCountServiceImpl extends ServiceImpl<BlogCountMapper, BlogCount> implements BlogCountService {

}
