package com.threeman.servicecore.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 博客评论表(Comment)表实体类
 *
 * @author songjing
 * @since 2022-03-31 10:47:14
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = -86517920968916051L;


    /**
     * 评论Id
     */
    @TableId
    private String commentId;

    /**
     * 回复人昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 回复人头像
     */
    private String nickAvatar;
    /**
     * 被回复人昵称
     */
    @TableField("parent_name")
    private String parentName;

    /**
     * 被回复人头像
     */
    private String parentAvatar;

    /**
     * 评论
     */
    @TableField("comment")
    private String comment;

    /**
     * 博客id
     */
    @TableField("blog_id")
    private Long blogId;

    /**
     * 回复评论Id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 是否为作者
     */
    private boolean author;

    /**
     * 评论点赞
     */
    @TableField("support_count")
    private Long supportCount;

    /**
     * 根据userid判断是否点赞过该评论
     */
    private boolean support;
    /**
     * 评论时间
     */
    @TableField("comment_date")
    private String commentDate;

    /**
     * 相对时间
     */
    private String time;

    private List<Comment> children;
}
