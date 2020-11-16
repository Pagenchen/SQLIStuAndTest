package com.pagen.controller;

import com.alibaba.fastjson.JSONObject;
import com.pagen.service.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/*
 * 系统目录控制类
 * */
@Controller
@RequestMapping("sys")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取主界面目录
     *
     * @return
     */
    @GetMapping("menu")
    @ResponseBody
    public JSONObject menu() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.fluentPutAll(sysMenuService.menu());
        return jsonObject;
    }

    /**
     * 服务端清理缓存
     *
     * @return
     */
    @RequestMapping("clear")
    @ResponseBody
    public JSONObject clear() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "服务端清理缓存成功");
        return jsonObject;
    }
}
