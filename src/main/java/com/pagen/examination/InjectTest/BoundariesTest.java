package com.pagen.examination.InjectTest;

import com.pagen.controller.DriverController;
import com.pagen.examination.InjectTest.Driver.BaseDriver;
import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.URL_Pages_Translation;
import com.pagen.service.DriverServer;
import com.pagen.service.Impl.DriverServerImpl;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取边界实现类
 */
public class BoundariesTest {
    private final double similarThreshold = 0.98;   // 页面相似度阈值
    private List<Boundaries> payloads;      // 测试用的 payload 列表
    private BaseOperation opera;            // 基本操作类
    private URL_Pages_Translation page;     // 页面解析器

    DriverController driverController = new DriverController();

    /**
     * 构造方法
     *
     * @param page： 原始页面解析器
     */
    public BoundariesTest(URL_Pages_Translation page) {
        try {
            UploadPayload();
            this.page = page;
            opera = new BaseOperation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行测试用例，检测 SQL 注入中 URL 的边界信息
     *
     * @return URL 边界
     */
    public Boundaries runTestPayload() throws MalformedURLException {
        String originUrl = page.getUrl().toString();    // 原始 URL
        String originPage = page.getPageInfo();         // 模板页面
        double similarity;      // 页面相似度

        // 对 payload 库进行遍历，寻找最符合的边界
        for (Boundaries payload : payloads) {
            // 正请求 URL
            String combUrlTrue = originUrl + payload.getPrefix() + payload.getPayloadTrue() + payload.getSuffix();
            // 负请求 URL
            String combUrlFalse = originUrl + payload.getPrefix() + payload.getPayloadFalse() + payload.getSuffix();

            page.setPageInfo(new URL(combUrlTrue));     // 获取正请求页面
            String pageTrue = page.getPageInfo();

            similarity = opera.Leven(originPage, pageTrue); // 计算正请求页面与模板页面的相似度
            if (similarity < similarThreshold) {         // 如不相似，则测试下一个 payload
                continue;
            }

            page.setPageInfo(new URL(combUrlFalse));    // 获取负请求页面
            String pageFalse = page.getPageInfo();

            similarity = opera.Leven(pageTrue, pageFalse);  // 计算正请求页面与负请求页面的相似度
            if (similarity < similarThreshold) {         // 如不相似，则说明该 payload 符合要求
                payload.setFalsePage(pageFalse);
                return payload;
            }
        }
        return null;    // 未找到符合要求的 payload
    }

    /**
     * 加载测试用例
     */
    private void UploadPayload() throws ParserConfigurationException, SAXException, IOException {
//        //1、获取解析工厂   SAXParserFactory是protect类型，所以用他的静态方法
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        //2、从解析工厂获取解析器
//        SAXParser parse = factory.newSAXParser();
//        //3、加载文档 Document 注册处理器
//        //4、编写处理器
//        readDriver handler = new readDriver();
//        handler.setClassName("Boundaries");
//        parse.parse("src/main/java/com/pagen/examination/InjectTest/xml/boundaries.xml", handler);
//
//        List<BaseDriver> base = handler.getPayloads();     //得到一个List对象，里面包含两个Persond类型的对象<person1,person2>
//        payloads = new ArrayList<>();
//        for (BaseDriver b : base) {
//            payloads.add((Boundaries) b);
//        }
        payloads = driverController.getAllBoundaries();
    }

    public void setPage(URL_Pages_Translation page) {
        this.page = page;
    }
}