package com.pagen.service.Impl;

import com.pagen.dao.StudyPageDao;
import com.pagen.entity.StuPageItem;
import com.pagen.entity.StuPages;
import com.pagen.service.StudyPageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("studyPageService")
public class StudyPageServiceImpl implements StudyPageService {
    @Resource
    StudyPageDao studyPageDao;

    @Override
    public List<StuPageItem> getStuPageItemByPageId(String pageId) {
        return studyPageDao.getStuPageItemByPageId(pageId);
    }

    @Override
    public List<StuPages> getAllStuPages() {
        return studyPageDao.getAllStuPages();
    }

    @Override
    public int addPageItems(StuPageItem stuPageItem) {
        int code = studyPageDao.addPageItems(stuPageItem);

        return code >= 1 ? 200 : 300;
    }
}
