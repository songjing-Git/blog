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
 * 角色权限表(RoleAuthority)表实体类
 *
 * @author songjing
 * @since 2021-10-29 11:46:21
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("role_authority")
public class RoleAuthority implements Serializable {

    /**
     * id
     */
    @TableId(type= IdType.AUTO)
    private Long roleAuthority;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 权限id
     */
    @TableField("authority_id")
    private Long authorityId;
}
