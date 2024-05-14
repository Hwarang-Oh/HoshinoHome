package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoDao {

    UserInfo findByUsername(@Param("username") String username);
    void insertUser(UserInfo user);
    void updateUser(UserInfo user);
}
