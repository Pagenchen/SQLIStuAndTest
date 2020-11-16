package com.pagen.examination.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL信息
 */
class SqlInformation {
    private final String name;    // 数据库名
    private final ArrayList<TableInformation> tables; // 表信息

    public SqlInformation(String name) {
        this.name = name;
        tables = new ArrayList<>();
    }

    /**
     * 添加一个表信息记录
     *
     * @param tables 表信息
     */
    public void addTable(List<String> tables) {
        for (String t : tables) {
            TableInformation table = new TableInformation(t);
            this.tables.add(table);
        }
    }

    public void addColumn(List<String> columns, String tableName) {
        for (TableInformation table : tables) {
            if (table.getName().equals(tableName)) {
                table.addColumn(columns);
                break;
            }
        }
    }

    public void addData(List<String> data, String tableName, String columnName) {
        for (TableInformation table : tables) {
            if (table.getName().equals(tableName)) {
                table.addData(columnName, data);
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<TableInformation> getTables() {
        return tables;
    }
}

