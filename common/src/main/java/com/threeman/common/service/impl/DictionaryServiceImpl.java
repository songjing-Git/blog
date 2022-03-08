package com.threeman.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threeman.common.entity.Dictionary;
import com.threeman.common.mapper.DictionaryMapper;
import com.threeman.common.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Dictionary)表服务实现类
 *
 * @author songjing
 * @since 2022-03-07 17:02:27
 */
@Service
@Slf4j
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    public List<Dictionary> findLabels(String name) {
        return dictionaryMapper.findLabelTypeNames(name);
    }
}
