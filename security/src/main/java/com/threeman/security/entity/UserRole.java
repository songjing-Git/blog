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
 * 用户-角色表(UserRole)表实体类
 *
 * @author songjing
 * @since 2021-10-29 11:46:01
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_role")
public class UserRole implements Serializable {

    /**
     * id
     */
    @TableId(type= IdType.AUTO)
    private Long userRoleId;

    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;

    /**
     * 角色编号
     */
    @TableField("role_id")
    private Long roleId;
}
