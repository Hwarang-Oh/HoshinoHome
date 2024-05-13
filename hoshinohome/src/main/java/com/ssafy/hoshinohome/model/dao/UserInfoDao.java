package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.UserInfo;

public interface UserInfoDao {

    String login(UserInfo userInfo);

    void register(UserInfo userInfo);
}
