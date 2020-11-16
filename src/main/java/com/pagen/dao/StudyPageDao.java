package com.pagen.dao;

import com.pagen.entity.StuPageItem;
import com.pagen.entity.StuPages;

import java.util.List;

/**
 * 学习页面DAO
 */
public interface StudyPageDao {
    /**
     * 通过页面ID获取页面信息项
     *
     * @param pageId
     * @return
     */
    List<StuPageItem> getStuPageItemByPageId(String pageId);

    /**
     * 获取所有页面信息
     */
    List<StuPages> getAllStuPages();

    /**
     * 添加页面信息项
     *
     * @param stuPageItem 页面信息
     * @return code代码
     */
    int addPageItems(StuPageItem stuPageItem);
}
