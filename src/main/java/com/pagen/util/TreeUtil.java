package com.pagen.util;

import com.pagen.pojo.MenuVo;

import java.util.ArrayList;
import java.util.List;

/*
 * 生成目录树
 * */
public class TreeUtil {

    /**
     * 确定根目录
     *
     * @param treeList
     * @param pid
     * @return
     */
    public static List<MenuVo> toTree(List<MenuVo> treeList, Long pid) {
        List<MenuVo> retList = new ArrayList<MenuVo>();
        for (MenuVo parent : treeList) {
            if (pid.equals(parent.getPid())) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }

    /**
     * 用id和父id作为依据，递归找到子节点，生成树
     *
     * @param parent
     * @param treeList
     * @return
     */
    private static MenuVo findChildren(MenuVo parent, List<MenuVo> treeList) {
        for (MenuVo child : treeList) {
            //若id等于下一节点的父id，将下一节点添加进去
            if (parent.getId().equals(child.getPid())) {
                if (parent.getChild() == null) {
                    parent.setChild(new ArrayList<>());
                }
                parent.getChild().add(findChildren(child, treeList));
            }
        }
        return parent;
    }
}
