package com.threeman.servicecore.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
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
    private Long commentId;

    /**
     * 回复人昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 回复人头像
     */
    private String nickAvater;
    /**
     * 被回复人昵称
     */
    @TableField("parent_name")
    private String parentName;

    /**
     * 被回复人头像
     */
    @TableField("parent_avater")
    private String parentAvater;

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
    private Long parentId;

    /**
     * 是否为作者
     */
    @TableField("author")
    private boolean author;

    /**
     * 评论点赞
     */
    private Long supportCount;

    /**
     * 评论时间
     */
    @TableField("comment_date")
    private Date commentDate;

    private List<Comment> children;
}
