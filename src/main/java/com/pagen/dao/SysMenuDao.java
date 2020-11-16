package com.pagen.dao;

import com.pagen.entity.SysMenu;

import java.util.List;

/*
 * 系统目录
 * */
public interface SysMenuDao {

//    public List<SystemMenu> getSystemMenuByStatusAndSort(Long status, Integer sort);

    /**
     * 获取主系统目录
     *
     * @return
     */
    List<SysMenu> findAllByStatusOrderBySort(Integer status);

    /**
     * 添加系统页面
     *
     * @return
     */
    int addPage(SysMenu sysMenu);

    /**
     * 修改系统页面
     *
     * @return
     */
    int updatePage(SysMenu sysMenu);

    /**
     * 根据ID获取目录
     *
     * @return
     */
    SysMenu getMenusByID(String id);

    /**
     * 删除系统页面
     *
     * @return
     */
    int deleteMenu(String id);
}
