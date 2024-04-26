package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.UserInfo;

public interface UserInfoDao {

    int insertUserInfo(UserInfo userInfo);

    int updateUserInfo(UserInfo userInfo);

    int deleteUserInfo(String id);

    UserInfo selecUserInfo(String id);
}
