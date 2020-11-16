package com.pagen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 学习页面信息项类
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StuPageItem {
    private Integer id; //编号
    private Integer p_num; //页面项序号
    private Integer pid;//页面编号
    private String title;//标题
    private String type;//类型（text：文字  html：标签 SQL：数据库语句 code：代码）
    private String content;//内容
    private String descript;//描述
    private String remark;//备注
    private String example;//示例
}
