package com.threeman.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.common.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Dictionary)表服务接口
 *
 * @author songjing
 * @since 2022-03-07 17:02:27
 */
@Mapper
@Repository
public interface DictionaryMapper extends BaseMapper<Dictionary> {


    /**
     * 模糊搜索标签
     * @param typeName 标签名
     * @return
     */
    @Select("select type_code,type_value,type_name from dictionary where type_code = 'label' " +
            "and type_name like '${typeName}%'")
    List<Dictionary> findLabelTypeNames(@Param("typeName") String typeName);
}
