package com.threeman.servicecore.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户信息表(User)表实体类
 *
 * @author songjing
 * @since 2022-02-16 10:46:18
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {

    /**
     * 用户id
     */
    @TableId("user_id")
    private Long userId;

    /**
     * 用户昵称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 地址
     */
    @TableField("area")
    private String area;

    /**
     * 头像
     */
    @TableField("avater")
    private String avater;

    /**
     * 个人简介
     */
    @TableField("profile")
    private String profile;

    /**
     * 博文统计id
     */
    @TableField("blog_count_id")
    private Integer blogCountId;
}
