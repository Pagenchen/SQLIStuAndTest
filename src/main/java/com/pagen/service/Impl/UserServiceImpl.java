package com.pagen.service.Impl;

import com.pagen.dao.UserDao;
import com.pagen.entity.Permissions;
import com.pagen.entity.User;
import com.pagen.service.UserService;
import com.pagen.util.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;

    @Override
    public int hasUserAndPwd(String userName, String password, String admin) {
        int isAdmin = admin.equals("true") ? 1 : 0;
        if (userDao.hasUserAndPwd(userName, password, admin) > 0) {
            if (isAdmin == 1) {
                return 1;
            } else {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public void register(User user) {
        //处理业务调用dao
        //1.生成随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
        user.setSalt(salt);
        //3.明文密码进行md5 + salt + hash散列
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(md5Hash.toHex());
        userDao.register(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public User findRolesByUserName(String username) {
        return userDao.findRolesByUserName(username);
    }

    @Override
    public List<Permissions> findPermsByRoleId(String id) {
        return findPermsByRoleId(id);
    }
}
