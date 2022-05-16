package com.threeman.servicecore.config.rabbitmq;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author songjing
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_count")
public class Entity<V> implements Serializable {

    private List<V> data;

    private String database;

    private String es;

    private String id;

    private String isDdl;

    private String mysqlType;

    private List<V> old;

    private List<String> pdNames;

    private String sql;

    private V sqlType;

    private String table;

    private String ts;

    private String type;


}
