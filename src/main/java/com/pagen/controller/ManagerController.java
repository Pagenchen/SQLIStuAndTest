package com.pagen.controller;

import com.alibaba.fastjson.JSONObject;
import com.pagen.entity.StuPageItem;
import com.pagen.entity.SysMenu;
import com.pagen.service.StudyPageService;
import com.pagen.service.SysMenuService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("manager")
public class ManagerController {
    @Resource
    StudyPageService studyPageService;

    @Resource
    SysMenuService sysMenuService;

    /**
     * 跳转进入管理员权限下的界面
     *
     * @param P
     * @return
     */
    @RequestMapping("page/{P}")
    public String jumpTo(@PathVariable("P") String P) {
        return "manager/" + P;
    }

    /**
     * 跳转进入管理员权限下的界面
     *
     * @param P
     * @return
     */
    @RequestMapping("update/{P}")
    public String jumpToUpdate(@PathVariable("P") String P) {
        return "manager/table/" + P;
    }

    /**
     * 添加系统页面
     *
     * @return
     */
    @PostMapping("addPage.do")
    @ResponseBody
    public JSONObject addPage(SysMenu sysMenu) {

        JSONObject jsonObject = new JSONObject();
        int code = sysMenuService.addPage(sysMenu);

        jsonObject.put("code", code);
        jsonObject.put("msg", code == 200 ? "添加成功" : "添加失败");

        return jsonObject;
    }

    /**
     * 修改系统页面
     *
     * @return
     */
    @PostMapping("updatePage.do")
    @ResponseBody
    public JSONObject updatePage(SysMenu sysMenu) {

        JSONObject jsonObject = new JSONObject();
        int code = sysMenuService.updatePage(sysMenu);

        jsonObject.put("code", code);
        jsonObject.put("msg", code == 200 ? "添加成功" : "添加失败");

        return jsonObject;
    }

    /**
     * 删除系统页面
     *
     * @return
     */
    @GetMapping("deleteMenu.do")
    @ResponseBody
    public JSONObject deleteMenu(String id) {
        JSONObject jsonObject = new JSONObject();

        int code = sysMenuService.deleteMenu(id);

        jsonObject.put("code", code);
        jsonObject.put("msg", code == 200 ? "添加成功" : "添加失败");

        return jsonObject;
    }

    /**
     * 添加页面信息项
     *
     * @param stuPageItem
     * @return
     */
    @PostMapping("addPageItem")
    @ResponseBody
    public JSONObject addPageItems(@Param("stuPageItem") StuPageItem stuPageItem) {
        JSONObject jsonObject = new JSONObject();

        int code = studyPageService.addPageItems(stuPageItem);
        jsonObject.put("code", code);
        if (code == 200) {
            jsonObject.put("msg", "添加成功");
        } else if (code == 300) {
            jsonObject.put("msg", "添加失败");
        }

        return jsonObject;
    }


    /**
     * 获取所有目录
     *
     * @return
     */
    @RequestMapping("getMenus")
    @ResponseBody
    public JSONObject getMenus() {
        JSONObject jsonObject = new JSONObject();

        List<SysMenu> menus = sysMenuService.getAllMenus();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", menus.size());
        jsonObject.put("data", menus);

        return jsonObject;
    }

    /**
     * 根据ID获取目录
     *
     * @return
     */
    @RequestMapping("getMenusByID")
    @ResponseBody
    public JSONObject getMenusByID(@Param("id") String id) {
        JSONObject jsonObject = new JSONObject();

        SysMenu menus = sysMenuService.getMenusByID(id);
        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("title", menus.getTitle());
        jsonObject.put("remark", menus.getRemark());
        jsonObject.put("href", menus.getHref());
        jsonObject.put("icon", menus.getIcon());
        jsonObject.put("status", menus.getStatus());

        return jsonObject;
    }
}
