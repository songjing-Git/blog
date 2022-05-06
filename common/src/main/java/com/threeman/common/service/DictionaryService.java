package com.threeman.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.threeman.common.entity.Dictionary;

import java.util.List;

/**
 * (Dictionary)表服务接口
 *
 * @author songjing
 * @since 2022-03-07 17:02:27
 */
public interface DictionaryService extends IService<Dictionary> {


    /**
     * 模糊搜索标签
     *
     * @param typeName 名称
     * @return List<Dictionary>
     */
    List<Dictionary> findLabels(String typeName);
}
