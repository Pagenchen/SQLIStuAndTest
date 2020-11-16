package com.pagen.dao;

import com.pagen.entity.Permissions;
import com.pagen.entity.User;

import java.util.List;

public interface UserDao {
    /**
     * @param userName
     * @param password
     * @param admin
     * @return
     */
    int hasUserAndPwd(String userName, String password, String admin);

    /**
     * 注册用户方法
     *
     * @param user
     */
    void register(User user);

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 根据用户名查询所有角色
     *
     * @param username
     * @return
     */
    User findRolesByUserName(String username);

    /**
     * 根据角色id查询权限集合
     *
     * @param id
     * @return
     */
    List<Permissions> findPermsByRoleId(String id);
}
