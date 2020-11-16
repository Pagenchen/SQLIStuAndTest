package com.pagen.examination.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段信息
 */
class ColumnInformation {
    private final String name;    // 字段名
    private final ArrayList<String> data; // 包含数据

    public ColumnInformation(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    /**
     * 添加一个数据记录
     *
     * @param dataInput 数据
     */
    public void addData(List<String> dataInput) {
        data.addAll(dataInput);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getData() {
        return data;
    }
}