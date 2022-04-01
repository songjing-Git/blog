package com.threeman.servicecore.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 博客统计表(BlogCount)表实体类
 *
 * @author songjing
 * @since 2022-03-30 15:53:51
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_count")
public class BlogCount implements Serializable {

    private static final long serialVersionUID = -45193503890871461L;


    /**
     * 博客统计id
     */
    @TableField("blog_count_id")
    private String blogCountId;

    /**
     * 阅读量
     */
    @TableField("read_count")
    private Integer readCount;

    /**
     * 点赞数
     */
    @TableField("support_count")
    private Integer supportCount;

    /**
     * 评论数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 文章数
     */
    @TableField("article_count")
    private Integer articleCount;

    /**
     * 字数
     */
    @TableField("words")
    private Integer words;
}
