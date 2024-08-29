package com.springboot.excel.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author Kevin
 * @date 2024/8/29 17:10
 */
@Data
public class User {
    @ExcelProperty("姓名")
    @ColumnWidth(30)
    private String name;

    @ExcelProperty("年龄")
    @ColumnWidth(18)
    private String age;
}
