package com.pagen.examination.InjectTest;

import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.URL_Pages_Translation;

import java.io.IOException;
import java.net.URL;

/**
 * 测试类
 */
public class ParseDemo01 {
    @org.junit.Test
    public void test1() {
        try {
            String originUrl = "http://www.baidu.com";
            URL_Pages_Translation page = new URL_Pages_Translation(originUrl);

            String originPage = page.getPageInfo();

            BoundariesTest boundariesTest = new BoundariesTest(page);
            Boundaries boundaries = boundariesTest.runTestPayload();

            if (boundaries == null) {
                System.out.println("未找到对应边界 ！！");
                return;
            }
            System.out.println("边界：" + boundaries.getPrefix() + "    " + boundaries.getSuffix());

            BoolBlindTest boolBlindTest = new BoolBlindTest(page, boundaries);

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