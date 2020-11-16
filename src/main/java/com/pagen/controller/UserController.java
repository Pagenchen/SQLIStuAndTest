package com.pagen.controller;

import com.alibaba.fastjson.JSONObject;
import com.pagen.entity.User;
import com.pagen.service.UserService;
import com.pagen.util.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("register")
    @ResponseBody
    public JSONObject register(User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            userService.register(user);

            jsonObject.put("code", 200);
            jsonObject.put("msg", "注册成功，是否登陆？");
            jsonObject.put("src", "/page/login");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 400);
            jsonObject.put("msg", "注册失败，是否重新注册？");
            jsonObject.put("src", "/page/register");
            return jsonObject;
        }
    }

    /**
     * 验证码方法
     */
    @RequestMapping("getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        //生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //验证码放入session
        session.setAttribute("code", code);
        //验证码存入图片
        ServletOutputStream os = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220, 60, os, code);
    }

    /*
     * 登录操作
     * */
    @PostMapping("login")
    @ResponseBody
    public JSONObject login(@RequestParam("username") String userName, @RequestParam("password") String password,
                            @RequestParam("rememberMe") String rememberMe, @RequestParam("captcha") String captcha, HttpSession session) {

        JSONObject jsonObject = new JSONObject();

        String true_code = (String) session.getAttribute("code");

        try {
            //判断验证码
            if (captcha.equalsIgnoreCase(true_code)) {
                //设置令牌
                UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
                if (rememberMe.equals("true")) {
                    token.setRememberMe(true);
                }
                //获取主体对象
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);

                jsonObject.put("code", 1000);
                jsonObject.put("message", "登陆成功");

                return jsonObject;
            } else {
                throw new RuntimeException("验证码错误!");
            }
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误!");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        jsonObject.put("code", 2222);
        jsonObject.put("message", "用户名或者密码错误");
        return jsonObject;
    }

    /**
     * 退出登录
     */
    @RequestMapping("logout")
    @ResponseBody
    public JSONObject logout() {
        JSONObject jsonObject = new JSONObject();

        Subject subject = SecurityUtils.getSubject();
        subject.logout();//退出用户

        return jsonObject;
    }
}
