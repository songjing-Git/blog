package com.threeman.servicecore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.servicecore.entity.BlogCount;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 博客统计表(BlogCount)表服务接口
 *
 * @author songjing
 * @since 2022-03-30 15:53:52
 */
@Mapper
@Repository
public interface BlogCountMapper extends BaseMapper<BlogCount> {

}
