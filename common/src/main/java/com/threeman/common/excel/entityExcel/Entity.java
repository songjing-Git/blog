package com.threeman.common.excel.entityExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/19 9:57
 */
@Data
@ColumnWidth(50)
public class Entity {

    @ExcelProperty("用户名")
    private String nickname;
    @ExcelProperty("真实姓名")
    private String realName0;
    @ExcelProperty("联系方式")
    private String mobile1;
    @ExcelProperty("收货地址")
    private String address3;
}
