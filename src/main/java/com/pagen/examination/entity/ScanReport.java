package com.pagen.examination.entity;


import com.pagen.examination.InjectTest.Driver.BaseDriver;
import com.pagen.examination.InjectTest.Driver.Boundaries;

import java.util.ArrayList;
import java.util.List;

enum WebType {NULL, STATIC, DYNAMIC}         // 网页类型

/**
 * 扫描报告处理类
 */
public class ScanReport {
    private final String url;                       // URL 值
    private WebType webType = null;                 // 网页类型
    private String injectPoint = null;              // 注入点
    private Boundaries paramType = null;            // 参数类型
    private BaseDriver injectType = null;           // 注入类型

    private ArrayList<SqlInformation> SQLs = null;  // 数据库内容


    public ScanReport(String url) {
        this.url = url;
        SQLs = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<SqlInformation> getSQLs() {
        return SQLs;
    }

    public WebType getWebType() {
        return webType;
    }

    public void setWebType(WebType webType) {
        this.webType = webType;
    }

    public String getInjectPoint() {
        return injectPoint;
    }

    public void setInjectPoint(String injectPoint) {
        this.injectPoint = injectPoint;
    }

    public BaseDriver getInjectType() {
        return injectType;
    }

    public void setInjectType(BaseDriver injectType) {
        this.injectType = injectType;
    }

    public Boundaries getParamType() {
        return paramType;
    }

    public void setParamType(Boundaries paramType) {
        this.paramType = paramType;
    }

    /**
     * 添加一个数据库信息记录
     *
     * @param sqlName 数据库信息
     */
    public void addSql(List<String> sqlName) {
        for (String s : sqlName) {
            SqlInformation sql = new SqlInformation(s);
            SQLs.add(sql);
        }
    }

    public void addTable(String sqlName, List<String> tableName) {
        for (SqlInformation sql : SQLs) {
            if (sql.getName().equals(sqlName)) {
                sql.addTable(tableName);
                break;
            }
        }
    }

    public void addColumn(String sqlName, String tableName, List<String> columnName) {
        for (SqlInformation sql : SQLs) {
            if (sql.getName().equals(sqlName)) {
                sql.addColumn(columnName, tableName);
                break;
            }
        }
    }

    public void addData(String sqlName, String tableName, String columnName, List<String> data) {
        for (SqlInformation sql : SQLs) {
            if (sql.getName().equals(sqlName)) {
                sql.addData(data, tableName, columnName);
                break;
            }
        }
    }
}


