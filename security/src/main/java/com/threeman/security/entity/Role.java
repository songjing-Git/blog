package com.threeman.security.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色表(Role)表实体类
 *
 * @author songjing
 * @since 2021-10-29 11:41:20
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("role")
public class Role implements Serializable {

    /**
     * 角色编号
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
}
