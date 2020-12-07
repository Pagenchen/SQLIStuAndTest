package com.pagen.controller;

import com.alibaba.fastjson.JSONObject;
import com.pagen.service.DriverServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("test")
public class TestController {
    @Resource
    DriverServer driverServer;

    @RequestMapping("test1")
    @ResponseBody
    public JSONObject test(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("all", driverServer.getAllBoundaries());
        return jsonObject;
    }
}
