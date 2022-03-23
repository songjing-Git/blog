package com.threeman.servicecore.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * 博文信息表(BlogInfo)表实体类
 *
 * @author songjing
 * @since 2022-02-28 15:34:57
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_info")
public class BlogInfo implements Serializable {

    /**
     * 博文id
     */
    @TableId(type = IdType.AUTO)
    private long blogInfoId;

    /**
     * 博文作者编号
     */
    @TableField("blog_author_id")
    private long blogAuthorId;

    /**
     * 博文标题
     */
    @TableField("blog_title")
    private String blogTitle;

    /**
     * 阅读量
     */
    @TableField("views")
    private long views;

    /**
     * 点赞数
     */
    @TableField("supports")
    private long supports;

    /**
     * 评论数
     */
    @TableField("comments")
    private long comments;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 发布形式 1-公开 0-私密
     */
    @TableField("open")
    private int open;

    /**
     * 发布时间
     */
    @TableField("release_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseTime;

    /**
     * 图片地址
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 内容
     */
    @TableField("context")
    private String context;

    /**
     * 摘要
     */
    @TableField("summary")
    private String summary;

    /**
     * 1立刻发布 0定时发布
     */
    @TableField("immediate")
    private int immediate;

    /**
     * 博文类型 1-html 0-markdown
     */
    @TableField("blog_type")
    private int blogType;

}
