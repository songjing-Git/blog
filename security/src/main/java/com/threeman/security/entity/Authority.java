package com.threeman.security.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 权限表(Authority)表实体类
 *
 * @author songjing
 * @since 2021-10-29 11:43:57
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("authority")
public class Authority implements Serializable {


    /**
     * 权限编号
     */
    @TableId(type = IdType.AUTO)
    private Long authorityId;
    /**
     * 权限名称
     */
    @TableField("authority_name")
    private String authorityName;

    /**
     * 资源路径
     */
    @TableField("authority_url")
    private String authorityUrl;


}
