package com.pagen.examination;

import com.pagen.examination.InjectTest.BoolBlindTest;
import com.pagen.examination.InjectTest.BoundariesTest;
import com.pagen.examination.InjectTest.Driver.*;
import com.pagen.examination.InjectTest.ErrorBasedTest;
import com.pagen.examination.InjectTest.UnionSelectTest;
import com.pagen.examination.entity.ScanReport;

import java.net.MalformedURLException;
import java.util.List;

/**
 * 注入漏洞模块控制
 * SQL注入漏洞扫描器类
 */
public class SQLIPScanner {

    private final URL_Pages_Translation page;
    private final ScanReport report;

    /**
     * 构造方法
     *
     * @param , InputP
     *          初始化对象属性值
     */
    public SQLIPScanner(URL_Pages_Translation page, ScanReport report) {
        this.page = page;
        this.report = report;
    }

    /**
     * 运行扫描，
     * 将结果保存于SQLIP
     */
    void runCheckInject() throws MalformedURLException {

        if (report.getParamType() == null) {
            BoundariesTest boundariesTest = new BoundariesTest(page);//判断边界
            report.setParamType(boundariesTest.runTestPayload());

            if (report.getParamType() == null) {
                return;
            }
        }

        //union注入
        UnionSelectTest unionSelectTest = new UnionSelectTest(page, report.getParamType());
        UnionSelectDriver unionDriver = unionSelectTest.runTestPayload();
        if (unionDriver != null) {
            report.setInjectType(unionDriver);
            return;
        }

        //报错注入
        ErrorBasedTest errorBasedTest = new ErrorBasedTest(page, report.getParamType());
        ErrorBaseDriver errorDriver;
        try {
            errorDriver = errorBasedTest.runTestPayload();
            if (errorDriver != null) {
                report.setInjectType(errorDriver);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BoolBlindTest boolBlindTest = new BoolBlindTest(page, report.getParamType());
        BoolBlindDriver boolBlindDriver = boolBlindTest.runTestPayload();
        if (boolBlindDriver != null) {
            report.setInjectType(boolBlindDriver);
            return;
        }

    }

    public void getSQLName() {
        BaseDriver driver = report.getInjectType();
        Boundaries boundary = report.getParamType();

        if (report.getParamType() != null) {
            System.out.println("here is not null!!");
        }

        switch (driver.getTypeDriver()) {
            case "UNION_SELECT": {
                UnionSelectTest unionSelectTest = new UnionSelectTest(page, boundary);
                List<String> sqlName = unionSelectTest.getSQLsName((UnionSelectDriver) driver);

                if (sqlName != null) {
                    report.addSql(sqlName);
                } else
                    System.out.println("未能检索到数据库名！！");
                break;
            }

            //如果是报错注入
            case "ERROR_BASE": {
                ErrorBasedTest errorBasedTest = new ErrorBasedTest(page, boundary);
                List<String> sqlName = errorBasedTest.getSQLsName((ErrorBaseDriver) driver);

                if (sqlName != null) {
                    report.addSql(sqlName);
                } else
                    System.out.println("未能检索到数据库名！！");
                break;
            }

            // 如果是布尔盲注
            case "Bool_Blind": {
                BoolBlindTest boolBlindTest = new BoolBlindTest(page, boundary);
                try {
                    List<String> sqlName = boolBlindTest.getDatabaseName();
                    if (sqlName != null) {
                        report.addSql(sqlName);
                    } else
                        System.out.println("未能检索到数据库名！！");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void getTableName(String sqlName) {
        BaseDriver driver = report.getInjectType();
        Boundaries boundary = report.getParamType();

        if (driver.getTypeDriver().equals("UNION_SELECT")) {
            UnionSelectTest unionSelectTest = new UnionSelectTest(page, boundary);
            List<String> tableName = unionSelectTest.getTableName((UnionSelectDriver) driver, sqlName);

            if (tableName != null) {
                report.addTable(sqlName, tableName);
            } else
                System.out.println("未能检索到表名！！");
        }

        //
        if (driver.getTypeDriver().equals("ERROR_BASE")) {
            ErrorBasedTest errorBasedTest = new ErrorBasedTest(page, boundary);
            List<String> tableName = errorBasedTest.getTableName((ErrorBaseDriver) driver, sqlName);

            if (sqlName != null) {
                report.addTable(sqlName, tableName);
            } else
                System.out.println("未能检索到表名！！");
        }

        // 如果是布尔盲注
        else if (driver.getTypeDriver().equals("Bool_Blind")) {
            BoolBlindTest boolBlindTest = new BoolBlindTest(page, boundary);
            try {
                List<String> tableName = boolBlindTest.getTableNames(sqlName);
                if (tableName != null) {
                    report.addTable(sqlName, tableName);
                } else
                    System.out.println("未能检索到表名！！");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getColumnName(String sqlName, String tableName) {
        BaseDriver driver = report.getInjectType();
        Boundaries boundary = report.getParamType();

        if (driver.getTypeDriver().equals("UNION_SELECT")) {
            UnionSelectTest unionSelectTest = new UnionSelectTest(page, boundary);
            List<String> columnName = unionSelectTest.getColumnName((UnionSelectDriver) driver, sqlName, tableName);

            if (tableName != null) {
                report.addColumn(sqlName, tableName, columnName);
            } else
                System.out.println("未能检索到列名！！");
        }
        //
        else if (driver.getTypeDriver().equals("ERROR_BASE")) {
            ErrorBasedTest errorBasedTest = new ErrorBasedTest(page, boundary);
            List<String> columnName = errorBasedTest.getColumnName((ErrorBaseDriver) driver, sqlName, tableName);
            if (sqlName != null) {
                report.addColumn(sqlName, tableName, columnName);
            } else
                System.out.println("未能检索到列名！！");
        }

        // 如果是布尔盲注
        else if (driver.getTypeDriver().equals("Bool_Blind")) {
            BoolBlindTest boolBlindTest = new BoolBlindTest(page, boundary);
            try {
                List<String> columnName = boolBlindTest.getColumnNames(sqlName, tableName);
                if (tableName != null) {
                    report.addColumn(sqlName, tableName, columnName);
                } else
                    System.out.println("未能检索到列名！！");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getData(String sqlName, String tableName, String[] columnsName) {
        BaseDriver driver = report.getInjectType();
        Boundaries boundary = report.getParamType();

        if (driver.getTypeDriver().equals("UNION_SELECT")) {
            UnionSelectTest unionSelectTest = new UnionSelectTest(page, boundary);
            for (String column : columnsName) {
                List<String> data = unionSelectTest.getData((UnionSelectDriver) driver, sqlName, tableName, column);

                if (tableName != null) {
                    report.addData(sqlName, tableName, column, data);
                } else
                    System.out.println("未能检索到列名！！");
            }
        }
        //
        if (driver.getTypeDriver().equals("ERROR_BASE")) {
            ErrorBasedTest errorBasedTest = new ErrorBasedTest(page, boundary);
            for (String column : columnsName) {
                List<String> data = errorBasedTest.getData((ErrorBaseDriver) driver, sqlName, tableName, column);
                if (sqlName != null) {
                    report.addData(sqlName, tableName, column, data);
                } else
                    System.out.println("未能检索到数据！！");
            }
        }

        // 如果是布尔盲注
        else if (driver.getTypeDriver().equals("Bool_Blind")) {
            BoolBlindTest boolBlindTest = new BoolBlindTest(page, boundary);
            try {
                for (String column : columnsName) {
                    List<String> data = boolBlindTest.getDatas(sqlName, tableName, column);
                    if (tableName != null) {
                        report.addData(sqlName, tableName, column, data);
                    } else
                        System.out.println("未能检索到列名！！");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
