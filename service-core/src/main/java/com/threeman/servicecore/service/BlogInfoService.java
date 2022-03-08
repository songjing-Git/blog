package com.threeman.servicecore.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.threeman.servicecore.entity.BlogInfo;

import java.util.List;
import java.util.Map;

/**
 * 博文信息表(BlogInfo)表服务接口
 *
 * @author songjing
 * @since 2022-02-28 15:34:57
 */
public interface BlogInfoService extends IService<BlogInfo> {

    boolean insertBlogInfo(BlogInfo blogInfo);

    List<Map<String,Object>> findBlogInfos(String text);
}