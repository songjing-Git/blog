package com.threeman.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threeman.security.entity.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限表(Authority)表服务接口
 *
 * @author songjing
 * @since 2021-10-29 11:43:57
 */
@Mapper
@Repository
public interface AuthorityMapper extends BaseMapper<Authority> {

    /**
     * 根据authorityId获取权限资源信息
     * @param authorityId 资源id
     * @return Authority
     */
    @Select("select * from authority where authority_id = #{authorityId}")
    Authority getAuthorityInfoByAuthorityId(@Param("authorityId")long authorityId);

    /**
     * 获取权限对应资源信息
     * @return List<Authority>
     */
    @Select("select * from authority ")
    List<Authority> getAuthorityInfo();
}
