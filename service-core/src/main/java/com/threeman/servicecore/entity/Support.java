package com.threeman.servicecore.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/4/12 17:00
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("support")
public class Support {


    @TableField("id")
    private Long id;


    @TableField("user_id")
    private Long userId;

}

