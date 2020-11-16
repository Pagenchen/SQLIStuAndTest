package com.pagen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用于页面跳转
 */
@Controller
@RequestMapping("page")
public class JumpController {
    /**
     * 跳转到主页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "redirect:/";
    }

    /**
     * 跳转到登陆界面
     */
    @RequestMapping("register")
    public String register() {
        return "register";
    }

    /**
     * 跳转到login请求
     */
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 跳转至欢迎界面
     *
     * @return
     */
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 跳转至错误界面
     *
     * @return
     */
    @RequestMapping("error")
    public String error() {
        return "404";
    }

    /**
     * 跳转到测试界面
     *
     * @param path
     * @return
     */
    @RequestMapping("test/{path}")
    public String jumpTo(@PathVariable("path") String path) {
        return "page/pages/" + path;
    }

    /**
     * 跳转到学SQL习界面
     *
     * @param SQLS_Page
     * @return
     */
    @RequestMapping("SQL/{SQLS_Page}")
    public String toSQLStudy(@PathVariable("SQLS_Page") String SQLS_Page) {

        return "SQLStu/" + SQLS_Page;
    }

    /**
     * 跳转到SQL注入学习界面
     *
     * @param SQLIS_Page
     * @return
     */
    @RequestMapping("SQLI/{SQLIS_Page}")
    public String toSQLIStudy(@PathVariable("SQLIS_Page") String SQLIS_Page) {

        return "SQLIStu/" + SQLIS_Page;
    }

    /**
     * 跳转到SQL注入学习界面
     *
     * @param Exam_Page
     * @return
     */
    @RequestMapping("exam/{Exam_Page}")
    public String toExamination(@PathVariable("Exam_Page") String Exam_Page) {

        return "examination/" + Exam_Page;
    }


}
