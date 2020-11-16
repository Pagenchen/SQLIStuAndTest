package com.pagen.examination.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 表信息
 */
class TableInformation {
    private final String name;    // 表名
    private final ArrayList<ColumnInformation> columns;   // 字段信息

    public TableInformation(String name) {
        this.name = name;
        columns = new ArrayList<>();
    }

    /**
     * 添加一个字段信息记录
     *
     * @param columnsName 字段信息
     */
    public void addColumn(List<String> columnsName) {
        for (String c : columnsName) {
            ColumnInformation column = new ColumnInformation(c);
            this.columns.add(column);
        }
    }

    public void addData(String columnName, List<String> data) {
        for (ColumnInformation column : columns) {
            if (column.getName().equals(columnName)) {
                column.addData(data);
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<ColumnInformation> getColumns() {
        return columns;
    }
}

