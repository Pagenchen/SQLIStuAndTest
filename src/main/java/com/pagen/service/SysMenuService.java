package com.pagen.service;

import com.pagen.entity.SysMenu;

import java.util.List;
import java.util.Map;

public interface SysMenuService {
    /**
     * 获取主页目录
     *
     * @return
     */
    Map<String, Object> menu();

    /**
     * 获取所有目录项
     *
     * @return
     */
    List<SysMenu> getAllMenus();

    /**
     * 根据ID获取目录
     *
     * @return
     */
    SysMenu getMenusByID(String id);

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
     * 删除系统页面
     *
     * @return
     */
    int deleteMenu(String id);
}
