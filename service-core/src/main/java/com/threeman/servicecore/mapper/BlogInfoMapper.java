package com.threeman.servicecore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.servicecore.entity.BlogInfo;
import org.apache.ibatis.annotations.Mapper;
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

}
