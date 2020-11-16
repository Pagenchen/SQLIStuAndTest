package com.pagen.examination.InjectTest;

import com.pagen.examination.InjectTest.Driver.BaseDriver;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取xml文件，利用反射技术
 * 加载驱动
 */
public class readDriver extends DefaultHandler {

    private List<BaseDriver> payloads;//payload列表

    //-------------读取-------------------------------------------
    private BaseDriver payload;
    private String className;
    private String tag;         // 记录标签名

    @Override
    public void startDocument() {
        System.out.println("处理文档开始");
        payloads = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        // TODO Auto-generated method stub
        if (null != qName) {
            tag = qName;
        }
        if (null != qName && qName.equals("test")) {
            try {
                Class<?> clazz = Class.forName(className);
                payload = (BaseDriver) clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String str = new String(ch, start, length);
        if (null != tag && payload != null) {
            payload.SetAttr(tag, str);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("test")) {
            this.payloads.add(payload);
        }
        tag = null;
    }

    @Override
    public void endDocument() {
        // TODO Auto-generated method stub
        System.out.println("文档处理结束");
    }

    //-------------------获取payload列表---------------------------------------
    public List<BaseDriver> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<BaseDriver> payloads) {
        this.payloads = payloads;
    }

    public void setClassName(String className) {
        this.className = "com.pagen.examination.InjectTest.Driver." + className;
    }
}