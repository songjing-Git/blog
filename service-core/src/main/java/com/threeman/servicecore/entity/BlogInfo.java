package com.threeman.servicecore.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "blog_index")
public class BlogInfo implements Serializable {

    /**
     * 博文id
     */
    @TableField("blog_info_id")
    @Field(type= FieldType.Integer)
    private Integer blogInfoId;

    /**
     * 博文作者编号
     */
    @TableField("blog_author_id")
    @Field(type= FieldType.Integer)
    private Integer blogAuthorId;

    /**
     * 博文标题
     */
    @TableField("blog_title")
    @Field(type= FieldType.Text)
    private String blogTitle;

    /**
     * 阅读量
     */
    @TableField("views")
    @Field(type= FieldType.Integer)
    private Integer views;

    /**
     * 点赞数
     */
    @TableField("supports")
    @Field(type= FieldType.Integer)
    private Integer supports;

    /**
     * 评论数
     */
    @TableField("comments")
    @Field(type= FieldType.Integer)
    private Integer comments;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @Field(format= DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss",type= FieldType.Date)
    private Date createTime;

    /**
     * 作者地址
     */
    @TableField("author_url")
    private String authorUrl;

    /**
     * 图片地址
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 内容
     */
    @TableField("context")
    @Field(type= FieldType.Text)
    private String context;

    /**
     * 摘要
     */
    @TableField("summary")
    @Field(type= FieldType.Text)
    private String summary;
}
