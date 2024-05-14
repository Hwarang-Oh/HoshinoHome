package com.ssafy.hoshinohome.model.service;


import com.ssafy.hoshinohome.model.dao.UserInfoDao;
import com.ssafy.hoshinohome.model.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfo getUserByUsername(String username) {
        return userInfoDao.findByUsername(username);
    }

    public boolean registerUser(UserInfo user) {
        user.setUser_password(passwordEncoder.encode(user.getUser_password()));
        try {
            userInfoDao.insertUser(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateUser(UserInfo user) {
        userInfoDao.updateUser(user);
    }
}
