package com.pagen.examination.InjectTest.Driver;

/**
 * 驱动类，用来对XML文件内容进行保存、获取模板
 */
public abstract class BaseDriver {
    public abstract void SetAttr(String tag, String str);

    public abstract String getTypeDriver();

    public String getPreventionAdvice() {
        return "应该注意对用户的输入进行特殊字符的过滤！";
    }
}