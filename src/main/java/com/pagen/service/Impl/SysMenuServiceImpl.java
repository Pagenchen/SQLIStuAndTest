package com.pagen.service.Impl;

import com.pagen.dao.SysMenuDao;
import com.pagen.entity.SysMenu;
import com.pagen.pojo.MenuVo;
import com.pagen.service.SysMenuService;
import com.pagen.util.TreeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    private SysMenuDao sysMenuDao;

    @Override
    public Map<String, Object> menu() {
        Map<String, Object> map = new HashMap<>(16);
        Map<String, Object> home = new HashMap<>(16);
        Map<String, Object> logo = new HashMap<>(16);

        //从数据库中调取目录信息
        List<SysMenu> menuList = sysMenuDao.findAllByStatusOrderBySort(1);

        List<MenuVo> menuInfo = new ArrayList<>();
        for (SysMenu e : menuList) {
            MenuVo menuVO = new MenuVo();
            menuVO.setId(e.getId());
            menuVO.setPid(e.getPid());
            menuVO.setHref(e.getHref());
            menuVO.setTitle(e.getTitle());
            menuVO.setIcon(e.getIcon());
            menuVO.setTarget(e.getTarget());
            menuInfo.add(menuVO);
        }
        //将生成的目录树添加进json
        map.put("menuInfo", TreeUtil.toTree(menuInfo, 0L));
        //配置首页
        home.put("title", "首页");
        home.put("href", "page/welcome");//控制器路由,自行定义
        map.put("homeInfo", home);
        //配置主页logo
        logo.put("title", "Pagen SQL");
        logo.put("image", "/images/logo.png");//静态资源文件路径,可使用默认的logo.png
        logo.put("href", "");
        map.put("logoInfo", logo);

        return map;
    }

    @Override
    public List<SysMenu> getAllMenus() {
        List<SysMenu> sysMenus1 = sysMenuDao.findAllByStatusOrderBySort(1);
        List<SysMenu> sysMenus0 = sysMenuDao.findAllByStatusOrderBySort(0);
        sysMenus1.addAll(sysMenus0);
        return sysMenus1;
    }

    /**
     * 根据ID获取目录
     *
     * @return
     */
    @Override
    public SysMenu getMenusByID(String id) {
        return sysMenuDao.getMenusByID(id);
    }

    @Override
    public int addPage(SysMenu sysMenu) {
        return sysMenuDao.addPage(sysMenu);
    }

    @Override
    public int updatePage(SysMenu sysMenu) {
        return sysMenuDao.updatePage(sysMenu);
    }

    /**
     * 删除系统页面
     *
     * @return
     */
    @Override
    public int deleteMenu(String id) {
        return sysMenuDao.deleteMenu(id);
    }

}
