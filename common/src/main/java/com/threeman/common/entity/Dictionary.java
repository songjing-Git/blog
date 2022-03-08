package com.threeman.common.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (Dictionary)表实体类
 *
 * @author songjing
 * @since 2022-03-07 17:02:27
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("dictionary")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 518281890667937385L;


    /**
     * 类型编码
     */
    @TableField("type_code")
    private String typeCode;

    /**
     * 值
     */
    @TableField("type_value")
    private Long typeValue;

    /**
     * 名称
     */
    @TableField("type_name")
    private String typeName;
}
