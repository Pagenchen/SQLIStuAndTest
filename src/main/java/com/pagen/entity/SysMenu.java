package com.pagen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/*
 * 系统菜单
 * */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu implements Serializable {
    private Long id;//目录项编号
    private String title;//标题
    private String href;//连接
    private Long pid;//父目录ID
    private String icon;//图标
    private String target;//
    private Integer sort;//排序
    private Integer status;//
    private String remark;//备注信息
    private Date create_at;//创建时间
    private Date update_at;//修改时间
    private Date delete_at;//删除时间

}