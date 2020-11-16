package com.pagen.examination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;


/**
 * UML页面解析类
 * 功能：找出页面上的URL、输入点、可疑漏洞
 */
public class URL_Pages_Translation {

    private URL url;//当前URL

    private String pageInfo;//页面内容

    private HashMap<String, String> Param = new HashMap<String, String>();
//-----------------方法-----------------------------------------------------

    /**
     * 构造方法,调用时需传入一个URL
     * 如果URL无法打开，抛出异常
     * 如果URL没问题，初始化对象信息
     *
     * @param url URL 值
     */
    public URL_Pages_Translation(String url) throws MalformedURLException {
        this.url = new URL(url);
        this.Param = getParameter(url);
        if (setPageInfo(this.url))
            return;
        throw new MalformedURLException();
    }

    /**
     * 获取页面编码
     */
    public static String getUrlCharset(String url) {
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            System.out.println("Content-Type" + "--->" + map.get("Content-Type"));
            List<String> list = map.get("Content-Type");
            if (list.size() > 0) {
                String contentType = list.toString().toUpperCase();
                if (contentType.contains("UTF-8")) {
                    return "UTF-8";
                }
                if (contentType.contains("GB2312")) {
                    return "GB2312";
                }
                if (contentType.contains("GBK")) {
                    return "GBK";
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    //-----------------------------------------------------------------------------------

    /**
     * 获取网址的参数
     *
     * @param url 网址
     * @author cevencheng
     */
    public static HashMap<String, String> getParameter(String url) {
        try {
            HashMap<String, String> map = new HashMap<>();
            final String charset = "utf-8";
            url = URLDecoder.decode(url, charset);

            if (url.indexOf('?') != -1) {
                final String contents = url.substring(url.indexOf('?') + 1);
                String[] keyValues = contents.split("&");
                for (String keyValue : keyValues) {
                    String key = keyValue.substring(0, keyValue.indexOf("="));
                    String value = keyValue.substring(keyValue.indexOf("=") + 1);
                    map.put(key, value);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // ---------获取URL参数--------------------

    /**
     * 获取url上的页面信息，保存
     */
    public boolean setPageInfo(URL url) {

        this.pageInfo = "";
        try {
            URLConnection urlConnection = url.openConnection();//连接URL
            urlConnection.setConnectTimeout(5000);//设置超时时间

            //判断是否为http协议连接
            HttpURLConnection connection;
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;
            } else {
                return false;
            }

            //获取html页面内容
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            StringBuilder urlString = new StringBuilder();
            String current;

            while ((current = in.readLine()) != null) {
                urlString.append(current);
            }
            this.pageInfo = urlString.toString();
        } catch (IOException e) {
            return false;
        }
        return this.pageInfo.length() > 0;
    }


//------------------获取对象属性信息-----------------

    /**
     * 判断一个页面是否为动态网页
     * 原理：从网页header查看网页最后修改时间，若不同时间间隔取出的header中最后修改时间改变就是动态网页，否则不是
     *
     * @return 是否为动态页面
     */
    public boolean isDynamicPage() throws InterruptedException {
        //-------------通过检索关键代码判断------------------
        if (getPageInfo().contains("<script")) {
            return true;
        }
        //-------------通过网页修改时间判断------------------
        final String[] time = new String[2];
        Thread DynamicTest1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    URLConnection Connect = url.openConnection();
                    time[0] = Connect.getHeaderFields().get("Date").get(0);
//                    time[1] = Connect.getHeaderFields().get("Date").get(0);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread DynamicTest2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    time[1] = url.openConnection().getHeaderFields().get("Date").get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        DynamicTest2.start();
        DynamicTest1.start();
        DynamicTest1.join();
        DynamicTest2.join();
        return !time[0].equals(time[1]);
        //------------------------------------------------------------
    }

    /**
     * 获取URL
     */
    public URL getUrl() {
        return url;
    }

    /**
     * 获取页面信息
     */
    public String getPageInfo() {
        return pageInfo;
    }

    /**
     * 获取URL参数
     */
    public HashMap<String, String> getParam() {
        return Param;
    }
}
