package com.pagen.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pagen.entity.StuPageItem;
import com.pagen.entity.StuPages;
import com.pagen.service.StudyPageService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("stu")
public class StudyPageController {

    @Resource
    StudyPageService studyPageService;

    /**
     * 通过页面id获取学习页面信息项并跳转
     *
     * @param SQLS_Page
     * @param
     * @return
     */
    @RequestMapping("SQLStu/{SQLS_Page}")
    public String getStuPage(@PathVariable("SQLS_Page") String SQLS_Page) {

        return "forward:/page/SQL/" + SQLS_Page;
    }

    /**
     * 通过页面id获取学习页面信息项并跳转
     *
     * @param SQLS_Page
     * @param
     * @return
     */
    @RequestMapping("SQLStuI/{SQLS_Page}")
    @ResponseBody
    public JSONObject getStuPageI(@PathVariable("SQLS_Page") String SQLS_Page, @Param("pageNum") Integer pageNum) {
        //将页面信息传入model
        PageHelper.startPage(pageNum, 10);
        List<StuPageItem> stuPageItems = studyPageService.getStuPageItemByPageId(SQLS_Page);
        PageInfo<StuPageItem> itemPageInfo = new PageInfo<>(stuPageItems);
//        model.addAttribute("pageItems", stuPageItems);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageItems", itemPageInfo);
        return jsonObject;
    }

    /**
     * 获取所有学习页面
     *
     * @return
     */
    @RequestMapping("getAllSPages")
    @ResponseBody
    public JSONObject getAllStuPages() {
        JSONObject jsonObject = new JSONObject();
        //获取页面基本信息
        List<StuPages> stuPages = studyPageService.getAllStuPages();

        jsonObject.put("code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count", stuPages.size());
        jsonObject.put("data", stuPages);
        jsonObject.put("stuPages", stuPages);

        //将页面项信息也导入
        for (StuPages stuPage : stuPages) {
            List<StuPageItem> stuPageItems = studyPageService.getStuPageItemByPageId(stuPage.getId().toString());
            jsonObject.put(stuPage.getId().toString(), stuPageItems);
        }

        return jsonObject;
    }
}
